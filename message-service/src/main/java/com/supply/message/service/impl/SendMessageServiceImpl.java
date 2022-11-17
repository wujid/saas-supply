package com.supply.message.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.model.request.sys.SysUserRequest;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.util.CommonUtil;
import com.supply.common.util.SystemUserUtil;
import com.supply.message.constant.MessageConstant;
import com.supply.message.constant.MsgTypeEnum;
import com.supply.message.constant.NotifyTypeEnum;
import com.supply.message.constant.PublishMsgNotifyTypeEnum;
import com.supply.message.model.po.ContentInfoPo;
import com.supply.message.model.po.TemplateInfoPo;
import com.supply.message.model.po.TemplateNotifyPo;
import com.supply.message.model.po.TemplateTenantPo;
import com.supply.message.model.request.SendMessageRequest;
import com.supply.message.model.request.TemplateInfoRequest;
import com.supply.message.model.request.TemplateNotifyRequest;
import com.supply.message.model.request.TemplateTenantRequest;
import com.supply.message.repository.IContentInfoRepository;
import com.supply.message.repository.ITemplateInfoRepository;
import com.supply.message.repository.ITemplateNotifyRepository;
import com.supply.message.repository.ITemplateTenantRepository;
import com.supply.message.service.ISendMessageService;
import com.supply.message.service.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description 发送消息服务层接口实现.
 * @date 2022-10-13
 */
@Service
public class SendMessageServiceImpl implements ISendMessageService {
    private static final Logger logger = LoggerFactory.getLogger(SendMessageServiceImpl.class);

    private final ITemplateInfoRepository templateInfoRepository;

    private final ITemplateNotifyRepository templateNotifyRepository;

    private final ITemplateTenantRepository templateTenantRepository;

    private final IContentInfoRepository contentInfoRepository;

    private final SystemUserUtil systemUserUtil;

    public SendMessageServiceImpl(ITemplateInfoRepository templateInfoRepository, ITemplateNotifyRepository templateNotifyRepository,
                                  ITemplateTenantRepository templateTenantRepository, IContentInfoRepository contentInfoRepository,
                                  SystemUserUtil systemUserUtil) {
        this.templateInfoRepository = templateInfoRepository;
        this.templateNotifyRepository = templateNotifyRepository;
        this.templateTenantRepository = templateTenantRepository;
        this.contentInfoRepository = contentInfoRepository;
        this.systemUserUtil = systemUserUtil;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage(SendMessageRequest request) {
        logger.info("[发送消息]---消息体为{}", JSON.toJSONString(request));
        final String templateCode = request.getTemplateCode();
        // 根据模板编号查询模板
        TemplateInfoRequest templateInfoRequest = new TemplateInfoRequest();
        templateInfoRequest.setCode(templateCode);
        templateInfoRequest.setStatus(Constant.STATUS_NOT_DEL);
        final TemplateInfoPo templateInfoPo = templateInfoRepository.getByParams(templateInfoRequest);
        if (null == templateInfoPo) {
            final String errorMessage = StrUtil.format("根据模板编码{}未查询到模板", templateCode);
            logger.error(errorMessage);
            throw new ApiException(errorMessage);
        }
        final Long templateId = templateInfoPo.getId();
        // 根据模板ID查询消息通知方式
        TemplateNotifyRequest templateNotifyRequest = new TemplateNotifyRequest();
        templateNotifyRequest.setTemplateId(templateId);
        templateNotifyRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<TemplateNotifyPo> templateNotifyPoList = templateNotifyRepository.getListByParams(templateNotifyRequest);
        if (CollectionUtil.isEmpty(templateNotifyPoList)) {
            logger.info("消息编码{}无对应的通知方式", templateCode);
            return;
        }
        // 消息通知
        final Integer msgType = templateInfoPo.getMsgType();
        // 公共消息
        if (msgType == MsgTypeEnum.PUBLISH.getType()) {
            // 判断是否指定租户,未指定则通知全系统人员
            this.publishMessage(request, templateInfoPo, templateNotifyPoList);
        }
        // 个人消息
        if (msgType == MsgTypeEnum.OWNER.getType()) {
            this.ownerMessage(request, templateInfoPo, templateNotifyPoList);
        }
    }

    /**
      * @description 公共消息通知.
      * @author wjd
      * @date 2022/11/1
      * @param request 发送消息体
      * @param templateInfoPo 消息模板
      * @param templateNotifyPoList 消息通知方式
      */
    private void publishMessage(SendMessageRequest request, TemplateInfoPo templateInfoPo, List<TemplateNotifyPo> templateNotifyPoList) {
        // 判断消息通知类型
        final Integer publishMsgNotifyType = templateInfoPo.getPublishMsgNotifyType();

        // 系统通知则通告系统内的所有成员
        if (publishMsgNotifyType == PublishMsgNotifyTypeEnum.NOTIFY_SYSTEM.getType()) {
            // 查询出系统所有未删除的成员
            SysUserRequest sysUserRequest = new SysUserRequest();
            sysUserRequest.setStatus(Constant.STATUS_NOT_DEL);
            final List<SysUserResponse> users = systemUserUtil.getUsersByParams(sysUserRequest);
            this.notifyUser(request, templateInfoPo, templateNotifyPoList, users);
        }
        // 租户则通知租户对应的成员
        if (publishMsgNotifyType == PublishMsgNotifyTypeEnum.NOTIFY_TENANT.getType()) {
            // 查询出该消息模板对应的租户,租户下对应的所有未删除成员
            final Long templateId = templateInfoPo.getId();
            TemplateTenantRequest templateTenantRequest = new TemplateTenantRequest();
            templateTenantRequest.setTemplateId(templateId);
            templateTenantRequest.setStatus(Constant.STATUS_NOT_DEL);
            final List<TemplateTenantPo> templateTenantPoList = templateTenantRepository.getListByParams(templateTenantRequest);
            if (CollectionUtil.isEmpty(templateTenantPoList)) {
                final String errorMessage = StrUtil.format("消息模板{}未找到对应的租户关联关系!", templateId);
                logger.warn(errorMessage);
            }
            final Set<Long> tenantIds = templateTenantPoList.stream().map(TemplateTenantPo::getTenantId).collect(Collectors.toSet());
            final List<SysUserResponse> users = systemUserUtil.getUsersByTenantIds(tenantIds);
            this.notifyUser(request, templateInfoPo, templateNotifyPoList, users);
        }
    }

    /**
     * @description 个人消息通知.
     * @author wjd
     * @date 2022/11/1
     * @param request 发送消息体
     * @param templateInfoPo 消息模板
     * @param templateNotifyPoList 消息通知方式
     */
    private void ownerMessage(SendMessageRequest request, TemplateInfoPo templateInfoPo, List<TemplateNotifyPo> templateNotifyPoList) {
        final Set<Long> receiverUserIds = request.getReceiverUserIds();
        final List<SysUserResponse> users = systemUserUtil.getUsersByIds(receiverUserIds);
        this.notifyUser(request, templateInfoPo, templateNotifyPoList, users);
    }

    /**
      * @description 消息下发至用户.
      * @author wjd
      * @date 2022/11/1
      * @param request 发送消息体
      * @param templateInfoPo 消息模板
      * @param templateNotifyPoList  消息通知方式
      * @param users 待通知的用户
      */
    private void notifyUser(SendMessageRequest request, TemplateInfoPo templateInfoPo, List<TemplateNotifyPo> templateNotifyPoList, List<SysUserResponse> users) {
        if (CollectionUtil.isEmpty(users)) {
            final String errorMessage = StrUtil.format("模板ID{}未指定消息通知人,请检查参数或模板配置项!", templateInfoPo.getId());
            logger.error(errorMessage);
            throw new ApiException(errorMessage);
        }
        final Map<Long, Map<String, Object>> paramsMap = request.getParamsMap();
        // 模板标题信息
        String title = null;
        // 模板跳转路径
        String url = null;
        // 是否所有待通知的用户参数相同
        boolean isCommonParams = CollectionUtil.isEmpty(paramsMap) || paramsMap.containsKey(MessageConstant.COMMON_PARAMS_KEY);
        Map<String, Object> commonParams = null;
        if (isCommonParams) {
            commonParams = paramsMap.get(MessageConstant.COMMON_PARAMS_KEY);
            title = CommonUtil.getContentByRule(templateInfoPo.getTitle(), commonParams);
            if (StrUtil.isNotBlank(templateInfoPo.getDetailUrl())) {
                url = CommonUtil.getContentByRule(templateInfoPo.getDetailUrl(), commonParams);
            }
        }
        List<ContentInfoPo> contentInfoPoList = new ArrayList<>();
        for (TemplateNotifyPo notifyPo : templateNotifyPoList) {
            final Integer notifyType = notifyPo.getNotifyType();
            final String notifyTemplate = notifyPo.getNotifyTemplate();
            String content = null;
            if (isCommonParams) {
                content = CommonUtil.getContentByRule(notifyTemplate, commonParams);
            }
            for (SysUserResponse user : users) {
                final Long userId = user.getId();
                ContentInfoPo contentInfoPo = new ContentInfoPo();
                // 非公共参数且有对应的用户参数则使用特定参数
                if (!isCommonParams && paramsMap.containsKey(userId)) {
                    final Map<String, Object> userParams = paramsMap.get(userId);
                    final String userTitle = CommonUtil.getContentByRule(templateInfoPo.getTitle(), userParams);
                    final String userUrl = CommonUtil.getContentByRule(templateInfoPo.getDetailUrl(), userParams);
                    final String userContent = CommonUtil.getContentByRule(notifyTemplate, userParams);
                    contentInfoPo.setTitle(userTitle);
                    contentInfoPo.setDetailUrl(userUrl);
                    contentInfoPo.setContent(userContent);
                } else {
                    // 否则使用公共参数
                    contentInfoPo.setTitle(title);
                    contentInfoPo.setDetailUrl(url);
                    contentInfoPo.setContent(content);
                }
                contentInfoPo.setTemplateId(templateInfoPo.getId());
                contentInfoPo.setMsgType(templateInfoPo.getMsgType());
                contentInfoPo.setNotifyType(notifyType);
                contentInfoPo.setReceiverUserId(userId);
                contentInfoPo.setBusinessStatus(BusinessStatusEnum.UN_READER.getStatus());
                contentInfoPoList.add(contentInfoPo);
                // 系统消息
                if (notifyType == NotifyTypeEnum.SYSTEM.getType()) {
                    final String senMessage = JSON.toJSONString(contentInfoPo);
                    WebSocketServer.sendMessageToUser(senMessage, userId);
                }
                // 邮件消息
                if (notifyType == NotifyTypeEnum.EMAIL.getType()) {

                }
                // 短信消息
                if (notifyType == NotifyTypeEnum.SMS.getType()) {

                }
            }
        }
        contentInfoRepository.saveBatch(contentInfoPoList);
    }
}
