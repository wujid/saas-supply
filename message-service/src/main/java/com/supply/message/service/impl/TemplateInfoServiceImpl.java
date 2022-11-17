package com.supply.message.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.util.CommonUtil;
import com.supply.message.constant.MsgTypeEnum;
import com.supply.message.constant.NotifyTypeEnum;
import com.supply.message.constant.PublishMsgNotifyTypeEnum;
import com.supply.message.cvt.TemplateInfoCvt;
import com.supply.message.model.po.TemplateInfoPo;
import com.supply.message.model.po.TemplateNotifyPo;
import com.supply.message.model.po.TemplateTenantPo;
import com.supply.message.model.request.TemplateInfoRequest;
import com.supply.message.model.request.TemplateNotifyRequest;
import com.supply.message.model.request.TemplateTenantRequest;
import com.supply.message.model.response.TemplateInfoResponse;
import com.supply.message.repository.ITemplateInfoRepository;
import com.supply.message.repository.ITemplateNotifyRepository;
import com.supply.message.repository.ITemplateTenantRepository;
import com.supply.message.service.ITemplateInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-10-13
 */
@Service
public class TemplateInfoServiceImpl implements ITemplateInfoService {
    private static final Logger logger = LoggerFactory.getLogger(TemplateInfoServiceImpl.class);

    private final ITemplateInfoRepository templateInfoRepository;

    private final ITemplateNotifyRepository templateNotifyRepository;

    private final ITemplateTenantRepository templateTenantRepository;


    public TemplateInfoServiceImpl(ITemplateInfoRepository templateInfoRepository, ITemplateNotifyRepository templateNotifyRepository,
                                   ITemplateTenantRepository templateTenantRepository) {
        this.templateInfoRepository = templateInfoRepository;
        this.templateNotifyRepository = templateNotifyRepository;
        this.templateTenantRepository = templateTenantRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTemplate(TemplateInfoRequest request) {
        logger.info("[新增消息模板]---消息体为{}", JSON.toJSONString(request));
        // 唯一性验证
        this.validate(request);
        // 转换并保存
        final TemplateInfoPo templateInfo = TemplateInfoCvt.INSTANCE.requestToPo(request);
        templateInfoRepository.save(templateInfo);
        final Long templateId = templateInfo.getId();

        // 保存消息通知方式
        this.saveTemplateNotifyBatch(request, templateId);

        // 保存消息模板和租户关联关系
        this.saveTemplateTenantBatch(request, templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplate(TemplateInfoRequest request) {
        logger.info("[修改消息模板]---消息体为{}", JSON.toJSONString(request));
        // 唯一性验证
        this.validate(request);
        // 转换并修改信息
        final TemplateInfoPo templateInfo = TemplateInfoCvt.INSTANCE.requestToPo(request);
        templateInfoRepository.updateById(templateInfo);
        final Long templateId = templateInfo.getId();
        // 删除历史消息通知方式并保存新通知方式
        TemplateNotifyPo templateNotifyPo = new TemplateNotifyPo();
        templateNotifyPo.setStatus(Constant.STATUS_DEL);

        TemplateNotifyRequest templateNotifyRequest = new TemplateNotifyRequest();
        templateNotifyRequest.setTemplateId(templateId);
        templateNotifyRequest.setStatus(Constant.STATUS_NOT_DEL);

        templateNotifyRepository.updateByParams(templateNotifyPo, templateNotifyRequest);
        this.saveTemplateNotifyBatch(request, templateId);

        // 删除历史消息模板和租户关联关系
        TemplateTenantPo templateTenantPo = new TemplateTenantPo();
        templateTenantPo.setStatus(Constant.STATUS_DEL);

        TemplateTenantRequest templateTenantRequest = new TemplateTenantRequest();
        templateTenantRequest.setTemplateId(templateId);
        templateTenantRequest.setStatus(Constant.STATUS_NOT_DEL);

        templateTenantRepository.updateByParams(templateTenantPo, templateTenantRequest);
        this.saveTemplateTenantBatch(request, templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delTemplate(Long templateId) {
        logger.info("[删除消息模板]---其中消息模板ID为{}", templateId);
        final TemplateInfoPo templateInfo = new TemplateInfoPo();
        templateInfo.setId(templateId);
        templateInfo.setStatus(Constant.STATUS_DEL);
        templateInfoRepository.updateById(templateInfo);
    }

    @Override
    public IPage<TemplateInfoResponse> getPageByParams(TemplateInfoRequest request) {
        Page<TemplateInfoPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<TemplateInfoPo> poPage = templateInfoRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<TemplateInfoPo> poList = page.getRecords();
        final List<TemplateInfoResponse> responseList = TemplateInfoCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }


    @Override
    public TemplateInfoResponse getTemplateInfoById(Long templateId) {
        final TemplateInfoPo templateInfoPo = templateInfoRepository.getById(templateId);
        final TemplateInfoResponse response = TemplateInfoCvt.INSTANCE.poToResponse(templateInfoPo);
        // 查询消息通知类型
        TemplateNotifyRequest request = new TemplateNotifyRequest();
        request.setTemplateId(templateId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<TemplateNotifyPo> templateNotifyPoList = templateNotifyRepository.getListByParams(request);
        if (CollectionUtil.isEmpty(templateNotifyPoList)) {
            return response;
        }
        Set<Integer> notifyTypes = new HashSet<>();
        for (TemplateNotifyPo templateNotifyPo : templateNotifyPoList) {
            final Integer notifyType = templateNotifyPo.getNotifyType();
            final String notifyTemplate = templateNotifyPo.getNotifyTemplate();
            notifyTypes.add(notifyType);
            if (notifyType == NotifyTypeEnum.SYSTEM.getType()) {
                response.setNotifyTemplateSystem(notifyTemplate);
            }
            if (notifyType == NotifyTypeEnum.EMAIL.getType()) {
                response.setNotifyTemplateEmail(notifyTemplate);
            }
            if (notifyType == NotifyTypeEnum.SMS.getType()) {
                response.setNotifyTemplateSms(notifyTemplate);
            }
        }
        response.setNotifyTypes(notifyTypes);
        // 查询租户
        TemplateTenantRequest templateTenantRequest = new TemplateTenantRequest();
        templateTenantRequest.setTemplateId(templateId);
        templateTenantRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<TemplateTenantPo> templateTenantPoList = templateTenantRepository.getListByParams(templateTenantRequest);
        if (CollectionUtil.isEmpty(templateNotifyPoList)) {
            return response;
        }
        final Set<Long> tenantIds = templateTenantPoList.stream().map(TemplateTenantPo::getTenantId).collect(Collectors.toSet());
        response.setTenantIds(tenantIds);
        return response;
    }



    /**
      * @description 新增/修改唯一性验证.
      * @author wjd
      * @date 2022/10/13
      * @param request 待验证的实体
      */
    public void validate(TemplateInfoRequest request) {
        TemplateInfoRequest templateInfoRequest = new TemplateInfoRequest();
        templateInfoRequest.setCode(request.getCode());
        templateInfoRequest.setNeId(request.getId());
        templateInfoRequest.setStatus(Constant.STATUS_NOT_DEL);
        final Long count = templateInfoRepository.getCountByParams(templateInfoRequest);
        if (count > 0) {
            final String message = "该模板已存在!";
            logger.error(message);
            throw new ApiException(message);
        }
    }

    /**
      * @description 批量保存消息通知类型.
      * @author wjd
      * @date 2022/10/29
      * @param request 消息模板请求体
      * @param templateId 模板ID
      */
    private void saveTemplateNotifyBatch(TemplateInfoRequest request, Long templateId) {
        List<TemplateNotifyPo> notifyPoList = new ArrayList<>();
        for (Integer notifyType : request.getNotifyTypes()) {
            TemplateNotifyPo templateNotifyPo = new TemplateNotifyPo();
            templateNotifyPo.setTemplateId(templateId);
            templateNotifyPo.setNotifyType(notifyType);
            // 系统消息
            if (notifyType == NotifyTypeEnum.SYSTEM.getType()) {
                templateNotifyPo.setNotifyTemplate(request.getNotifyTemplateSystem());
            }
            // 邮件消息
            if (notifyType == NotifyTypeEnum.EMAIL.getType()) {
                templateNotifyPo.setNotifyTemplate(request.getNotifyTemplateEmail());
            }
            // 短信消息
            if (notifyType == NotifyTypeEnum.SMS.getType()) {
                templateNotifyPo.setNotifyTemplate(request.getNotifyTemplateSms());
            }
            notifyPoList.add(templateNotifyPo);
        }
        templateNotifyRepository.saveBatch(notifyPoList);
    }

    /**
      * @description 保存消息模板同租户关联关系.
      * @author wjd
      * @date 2022/10/31
      * @param request 消息模板请求体
      * @param templateId 模板ID
      */
    private void saveTemplateTenantBatch(TemplateInfoRequest request, Long templateId) {
        final Integer msgType = request.getMsgType();
        final Integer publishMsgNotifyType = request.getPublishMsgNotifyType();
        // 消息类型为公告并且通知方式为指定租户
        if (msgType == MsgTypeEnum.PUBLISH.getType() &&  publishMsgNotifyType == PublishMsgNotifyTypeEnum.NOTIFY_TENANT.getType()) {
            if (CollectionUtil.isEmpty(request.getTenantIds())) {
                String errorMessage = "消息类型为公告且指定为租户时未指定租户!";
                logger.error(errorMessage);
                throw new ApiException(errorMessage);
            }
            List<TemplateTenantPo> templateTenantPoList = new ArrayList<>();
            for (Long tenantId : request.getTenantIds()) {
                TemplateTenantPo templateTenantPo = new TemplateTenantPo();
                templateTenantPo.setTemplateId(templateId);
                templateTenantPo.setTenantId(tenantId);
                templateTenantPoList.add(templateTenantPo);
            }
            templateTenantRepository.saveBatch(templateTenantPoList);
        }
    }
}
