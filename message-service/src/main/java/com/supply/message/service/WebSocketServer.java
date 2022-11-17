package com.supply.message.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.supply.common.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author wjd
 * @description websocket消息接收/推送.
 * @date 2022-10-08
 */
@Component
@ServerEndpoint("/websocket/{tenantId}/{uuId}/{userId}")
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private Long tenantId;

    private Long userId;

    private String uuId;

    private static final String keyFormat = "{}&{}&{}";

    private static final CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<>();

    // 租户对应用户集
    private static final ConcurrentHashMap<Long, Set<Long>> tenantToUserMap = new ConcurrentHashMap<>();

    // 用户对应UUID集
    private static final ConcurrentHashMap<Long, Set<String>> userToUUIdMap = new ConcurrentHashMap<>();

    // 已存在的sessions
    private static final ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    /**
     * @description webSocket接收一个来自客户端的新连接.
     * @author wjd
     * @date 2022/6/13
     * @param session 当前会话
     * @param tenantId 租户ID
     * @param uuId 唯一ID
     * @param userId 当前登录的客户端用户ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("tenantId") Long tenantId, @PathParam("uuId") String uuId, @PathParam("userId") Long userId) {
        logger.info("[webSocket消息]----有新的连接，连接UUID为{}用户为{}", uuId, userId);
        this.tenantId = tenantId;
        this.userId = userId;
        this.uuId = uuId;
        webSockets.add(this);
        // 租户和用户映射关系
        if (tenantToUserMap.containsKey(tenantId)) {
            tenantToUserMap.get(tenantId).add(userId);
        } else {
            Set<Long> userIds = new HashSet<>();
            userIds.add(userId);
            tenantToUserMap.put(tenantId, userIds);
        }


        // 用户ID和UUID映射关系<userId, Set<UUID>>
        if (userToUUIdMap.containsKey(userId)) {
            userToUUIdMap.get(userId).add(uuId);
        } else {
            Set<String> uuIds = new HashSet<>();
            uuIds.add(uuId);
            userToUUIdMap.put(userId, uuIds);
        }
        // 唯一值和session映射关系
        String key = StrUtil.format(keyFormat, tenantId, uuId, userId);
        sessionPools.putIfAbsent(key, session);
        logger.info("[websocket消息]----有新的连接，总数为:{}", sessionPools.size());
    }

    /**
     * @description webSocket关闭.
     * @author wjd
     * @date 2022/6/13
     */
    @OnClose
    public void onClose() {
        logger.info("[webSocket消息]----租户ID{},uuId{}对应的用户id为{}将关闭连接", this.tenantId, this.uuId, this.userId);

        if (!tenantToUserMap.containsKey(this.tenantId)) {
            logger.warn("[webSocket消息]----关闭消息时根据租户ID{}未找到对应的用户集映射", this.tenantId);
        }

        if (!userToUUIdMap.containsKey(this.userId)) {
            logger.warn("[webSocket消息]----关闭消息时根据用户ID{}未找到对应的UUID集映射", this.userId);
        }

        final Set<Long> userIds = tenantToUserMap.get(this.tenantId);
        if (CollUtil.isEmpty(userIds)) {
            logger.warn("[webSocket消息]----关闭消息时租户ID{}无用户集映射", this.tenantId);
        }

        final Set<String> uuIds = userToUUIdMap.get(this.userId);
        if (CollUtil.isEmpty(uuIds)) {
            logger.warn("[webSocket消息]----关闭消息时用户ID{}无UUID集映射", this.userId);
        }


        String key = StrUtil.format(keyFormat, this.tenantId, this.uuId, this.userId);
        sessionPools.remove(key);
        webSockets.remove(this);
    }

    /**
     * @description webSocket接收来自客户端消息
     * @author wjd
     * @date 2022/6/13
     * @param message 待接收的消息体
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info("[webSocket消息]----接收到来自客户端的消息,其中租户ID:{},userID:{},报文:{}",  this.tenantId, this.userId, message);
    }

    /**
     * @description webSocket异常.
     * @author wjd
     * @date 2022/6/13
     * @param session 异常session
     * @param throwable 对应的异常
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        final String message = "[webSocket消息]---连接异常";
        logger.error(message, throwable);
        throw new ApiException(message, throwable);
    }

    /**
     * @description 群发消息.
     * @author wjd
     * @date 2022/6/13
     * @param message 待发送的消息体
     */
    public static synchronized void sendMessage(String message) {
        logger.info("[websocket群发消息]---报文:{}", message);
        try {
            final Collection<Session> sessions = sessionPools.values();
            if (CollUtil.isEmpty(sessions)) {
                return;
            }
            for (Session session : sessions) {
                session.getAsyncRemote().sendText(message);
            }
        } catch (Exception e) {
            final String errorMessage = "[websocket消息]---群发消息异常";
            logger.error(errorMessage, e);
            throw new ApiException(errorMessage, e);
        }
    }

    /**
     * @description 发送消息到指定用户上.
     * @author wjd
     * @date 2022/6/13
     * @param message 待发送的消息体
     * @param userId 用户ID
     */
    public static synchronized void sendMessageToUser(String message, Long userId) {
        logger.info("[websocket单点发送消息至指定用户]----userID:{}报文:{}", userId, message);
        if (userId == null) {
            return;
        }
        if (!userToUUIdMap.containsKey(userId)) {
            logger.warn("[websocket单点发送消息至指定用户]----发送消息时根据用户ID{}未找到对应的UUID集映射", userId);
            return;
        }
        final Set<String> uuIds = userToUUIdMap.get(userId);
        if (CollUtil.isEmpty(uuIds)) {
            logger.warn("[websocket单点发送消息至指定用户]----发送消息时用户ID{}无UUID集映射", userId);
            return;
        }
        Long tenant = null;
        // 查询出对应的租户ID
        for (Long tenantId : tenantToUserMap.keySet()) {
            final Set<Long> userIds = tenantToUserMap.get(tenantId);
            if (userIds.contains(userId)) {
                tenant = tenantId;
                break;
            }
        }
        if (null == tenant) {
            logger.warn("[websocket单点发送消息至指定用户]----发送消息时根据用户ID{}未找到对应的租户ID", userId);
            return;
        }

        // 发送到该用户登录的所有客户端上
        for (String uuId : uuIds) {
            String key = StrUtil.format(keyFormat, tenant, uuId, userId);
            if (sessionPools.containsKey(key)) {
                sessionPools.get(key).getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     * @description 群发送消息到指定用户集上.
     * @author wjd
     * @date 2022/6/13
     * @param message 待发送的消息体
     * @param userIds 待接收消息的用户集
     */
    public static synchronized void sendMessageToUsers(String message, Set<Long> userIds) {
        for (Long userId : userIds) {
            sendMessageToUser(message, userId);
        }
    }

    /**
      * @description 发送消息到指定租户上的所有用户.
      * @author wjd
      * @date 2022/10/31
      * @param message 待发送的消息体
      * @param tenantId 租户ID
      */
    public static synchronized void sendMessageToTenant(String message, Long tenantId) {
        logger.info("[websocket单点发送消息至租户]----租户ID:{}报文:{}", tenantId, message);
        if (null == tenantId) {
            return;
        }
        if (!tenantToUserMap.containsKey(tenantId)) {
            logger.warn("[websocket单点发送消息至租户]----发送消息时根据租户ID{}未找到对应的用户ID集映射", tenantId);
            return;
        }
        final Set<Long> userIds = tenantToUserMap.get(tenantId);
        for (Long userId : userIds) {
            if (!userToUUIdMap.containsKey(userId)) {
                continue;
            }
            final Set<String> uuId = userToUUIdMap.get(userId);
            String key = StrUtil.format(keyFormat, tenantId, uuId, userId);
            if (sessionPools.containsKey(key)) {
                sessionPools.get(key).getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     * @description 群发送消息到指定租户集上.
     * @author wjd
     * @date 2022/6/13
     * @param message 待发送的消息体
     * @param tenantIds 待接收消息的租户集
     */
    public static synchronized void sendMessageToTenants(String message, Set<Long> tenantIds) {
        for (Long tenantId : tenantIds) {
            sendMessageToUser(message, tenantId);
        }
    }
}
