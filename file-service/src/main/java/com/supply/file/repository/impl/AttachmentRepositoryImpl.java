package com.supply.file.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.file.mapper.AttachmentMapper;
import com.supply.file.model.po.AttachmentPo;
import com.supply.file.model.request.AttachmentRequest;
import com.supply.file.repository.IAttachmentRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-19
 */
@Repository
public class AttachmentRepositoryImpl extends ServiceImpl<AttachmentMapper, AttachmentPo> implements IAttachmentRepository {

    private final AttachmentMapper attachmentMapper;

    public AttachmentRepositoryImpl(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(AttachmentPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<AttachmentPo> entityList, int batchSize) {
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(AttachmentPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<AttachmentPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(AttachmentPo dataScopeRangePo, @IgnoreFill AttachmentRequest request) {
        final LambdaQueryWrapper<AttachmentPo> queryWrapper = this.getQueryWrapper(request);
        return attachmentMapper.update(dataScopeRangePo, queryWrapper);
    }

    @Override
    public AttachmentPo getByParams(AttachmentRequest request) {
        final LambdaQueryWrapper<AttachmentPo> queryWrapper = this.getQueryWrapper(request);
        return attachmentMapper.selectOne(queryWrapper);
    }

    @Override
    public List<AttachmentPo> getListByParams(AttachmentRequest request) {
        final LambdaQueryWrapper<AttachmentPo> queryWrapper = this.getQueryWrapper(request);
        return attachmentMapper.selectList(queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/9/19
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<AttachmentPo> getQueryWrapper(AttachmentRequest request) {
        LambdaQueryWrapper<AttachmentPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), AttachmentPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), AttachmentPo::getName, request.getName());
        queryWrapper.eq(StrUtil.isNotBlank(request.getPathGroup()), AttachmentPo::getPathGroup, request.getPathGroup());
        queryWrapper.eq(StrUtil.isNotBlank(request.getPath()), AttachmentPo::getPath, request.getPath());
        queryWrapper.eq(null != request.getSize(), AttachmentPo::getSize, request.getSize());
        queryWrapper.eq(null != request.getBusinessId(), AttachmentPo::getBusinessId, request.getBusinessId());
        queryWrapper.eq(null != request.getBusinessStatus(), AttachmentPo::getBusinessStatus, request.getBusinessStatus());
        queryWrapper.eq(null != request.getStatus(), AttachmentPo::getStatus, request.getStatus());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getIds()), AttachmentPo::getId, request.getIds());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
