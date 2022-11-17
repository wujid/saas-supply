package com.supply.message.util;

import com.alibaba.fastjson.JSON;
import com.supply.message.model.response.ContentInfoResponse;
import com.supply.message.service.WebSocketServer;

/**
 * @author wjd
 * @description
 * @date 2022-11-04
 */
public class MessageUtil {

    /**
      * @description 根据用户ID向websocket发送一条刷新消息内容的通知.
      * @author wjd
      * @date 2022/11/4
      * @param userId 用户ID
      */
    public static void refreshWebSocketMessageByUserId(Long userId) {
        if (null == userId) {
            return;
        }
        ContentInfoResponse contentInfoResponse = new ContentInfoResponse();
        contentInfoResponse.setIsRefresh(true);
        final String message  = JSON.toJSONString(contentInfoResponse);
        WebSocketServer.sendMessageToUser(message, userId);
    }
}
