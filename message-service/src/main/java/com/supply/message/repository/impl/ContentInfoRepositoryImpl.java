package com.supply.message.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.message.mapper.ContentInfoMapper;
import com.supply.message.model.po.ContentInfoPo;
import com.supply.message.model.request.ContentInfoRequest;
import com.supply.message.repository.IContentInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-10-10
 */
@Repository
public class ContentInfoRepositoryImpl extends ServiceImpl<ContentInfoMapper, ContentInfoPo> implements IContentInfoRepository {

    private final ContentInfoMapper contentInfoMapper;

    public ContentInfoRepositoryImpl(ContentInfoMapper contentInfoMapper) {
        this.contentInfoMapper = contentInfoMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(ContentInfoPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<ContentInfoPo> entityList) {
        return super.saveBatch(entityList, 1000);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(ContentInfoPo entity) {
        return super.updateById(entity);
    }

    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(ContentInfoPo contentInfoPo, @IgnoreFill ContentInfoRequest request) {
        final LambdaQueryWrapper<ContentInfoPo> queryWrapper = this.getQueryWrapper(request);
        return contentInfoMapper.update(contentInfoPo, queryWrapper);
    }

    public ContentInfoPo getByParams(ContentInfoRequest request) {
        final LambdaQueryWrapper<ContentInfoPo> queryWrapper = this.getQueryWrapper(request);
        return contentInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ContentInfoPo> getListByParams(ContentInfoRequest request) {
        final LambdaQueryWrapper<ContentInfoPo> queryWrapper = this.getQueryWrapper(request);
        return contentInfoMapper.selectList(queryWrapper);
    }

    @Override
    public Page<ContentInfoPo> getPageByParams(Page<ContentInfoPo> page, ContentInfoRequest request) {
        final LambdaQueryWrapper<ContentInfoPo> queryWrapper = this.getQueryWrapper(request);
        return contentInfoMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<ContentInfoPo> getQueryWrapper(ContentInfoRequest request) {
        LambdaQueryWrapper<ContentInfoPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), ContentInfoPo::getId, request.getId());
        queryWrapper.eq(null != request.getTemplateId(), ContentInfoPo::getTemplateId, request.getTemplateId());
        queryWrapper.eq(null != request.getMsgType(), ContentInfoPo::getMsgType, request.getMsgType());
        queryWrapper.eq(null != request.getNotifyType(), ContentInfoPo::getNotifyType, request.getNotifyType());
        queryWrapper.eq(StrUtil.isNotBlank(request.getTitle()), ContentInfoPo::getTitle, request.getTitle());
        queryWrapper.eq(null != request.getReceiverUserId(), ContentInfoPo::getReceiverUserId, request.getReceiverUserId());
        queryWrapper.eq(null != request.getBusinessStatus(), ContentInfoPo::getBusinessStatus, request.getBusinessStatus());
        queryWrapper.eq(null != request.getStatus(), ContentInfoPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
