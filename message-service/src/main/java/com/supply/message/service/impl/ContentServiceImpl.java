package com.supply.message.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.util.CommonUtil;
import com.supply.message.cvt.ContentInfoCvt;
import com.supply.message.model.po.ContentInfoPo;
import com.supply.message.model.request.ContentInfoRequest;
import com.supply.message.model.response.ContentInfoResponse;
import com.supply.message.repository.IContentInfoRepository;
import com.supply.message.service.IContentService;
import com.supply.message.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-11-01
 */
@Service
public class ContentServiceImpl implements IContentService {
    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

    private final IContentInfoRepository contentInfoRepository;

    public ContentServiceImpl(IContentInfoRepository contentInfoRepository) {
        this.contentInfoRepository = contentInfoRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReaderById(Long contentId) {
        logger.info("[消息内容已读]---其中待变更的ID为{}", contentId);
        ContentInfoPo contentInfoPo = new ContentInfoPo();
        contentInfoPo.setBusinessStatus(BusinessStatusEnum.READER.getStatus());
        contentInfoPo.setId(contentId);
        contentInfoRepository.updateById(contentInfoPo);
        // 查询该消息
        final ContentInfoPo contentInfo = contentInfoRepository.getById(contentId);
        if (null != contentInfo) {
            // 通知系统刷新消息
            MessageUtil.refreshWebSocketMessageByUserId(contentInfo.getReceiverUserId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAllReader(Long userId) {
        ContentInfoPo contentInfoPo = new ContentInfoPo();
        contentInfoPo.setBusinessStatus(BusinessStatusEnum.READER.getStatus());

        ContentInfoRequest request = new ContentInfoRequest();
        request.setReceiverUserId(userId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        contentInfoRepository.updateByParams(contentInfoPo, request);
        // 通知系统刷新消息
        MessageUtil.refreshWebSocketMessageByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delContentById(Long contentId) {
        logger.info("[消息内容]---其中待删除的ID为{}", contentId);
        // 查询该消息
        final ContentInfoPo contentInfo = contentInfoRepository.getById(contentId);
        ContentInfoPo contentInfoPo = new ContentInfoPo();
        contentInfoPo.setStatus(Constant.STATUS_DEL);
        contentInfoPo.setId(contentId);
        contentInfoRepository.updateById(contentInfoPo);
        // 是否是未读消息,如果是未读消息则通知系统刷新消息
        if (null != contentInfo && contentInfo.getBusinessStatus() == BusinessStatusEnum.UN_READER.getStatus()) {
            MessageUtil.refreshWebSocketMessageByUserId(contentInfo.getReceiverUserId());
        }
    }

    @Override
    public IPage<ContentInfoResponse> getContentPage(ContentInfoRequest request) {
        Page<ContentInfoPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<ContentInfoPo> poPage = contentInfoRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<ContentInfoPo> poList = poPage.getRecords();
        final List<ContentInfoResponse> responseList = ContentInfoCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }
}
