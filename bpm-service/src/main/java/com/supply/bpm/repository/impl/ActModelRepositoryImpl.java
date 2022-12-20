package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.ActModelMapper;
import com.supply.bpm.model.po.ActModelPo;
import com.supply.bpm.model.request.ActModelRequest;
import com.supply.bpm.repository.IActModelRepository;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-19
 */
@Repository
public class ActModelRepositoryImpl extends ServiceImpl<ActModelMapper, ActModelPo> implements IActModelRepository {

    private final ActModelMapper actModelMapper;

    public ActModelRepositoryImpl(ActModelMapper actModelMapper) {
        this.actModelMapper = actModelMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(ActModelPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<ActModelPo> entityList) {
        return super.saveBatch(entityList, 1000);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(ActModelPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<ActModelPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public int updateByParams(ActModelPo actModelPo, @IgnoreFill ActModelRequest request) {
        final LambdaQueryWrapper<ActModelPo> queryWrapper = this.getQueryWrapper(request);
        return actModelMapper.update(actModelPo, queryWrapper);
    }

    @Override
    public ActModelPo getByParams(ActModelRequest request) {
        final LambdaQueryWrapper<ActModelPo> queryWrapper = this.getQueryWrapper(request);
        return actModelMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ActModelPo> getListByParams(ActModelRequest request) {
        final LambdaQueryWrapper<ActModelPo> queryWrapper = this.getQueryWrapper(request);
        return actModelMapper.selectList(queryWrapper);
    }

    @Override
    public Page<ActModelPo> getPageByParams(Page<ActModelPo> page, ActModelRequest request) {
        final LambdaQueryWrapper<ActModelPo> queryWrapper = this.getQueryWrapper(request);
        return actModelMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<ActModelPo> getQueryWrapper(ActModelRequest request) {
        LambdaQueryWrapper<ActModelPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), ActModelPo::getId, request.getId());
        queryWrapper.eq(null != request.getCategoryId(), ActModelPo::getCategoryId, request.getCategoryId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getModelId()), ActModelPo::getModelId, request.getModelId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getModelKey()), ActModelPo::getModelKey, request.getModelKey());
        queryWrapper.eq(StrUtil.isNotBlank(request.getDescription()), ActModelPo::getDescription, request.getDescription());
        queryWrapper.eq(null != request.getVersion(), ActModelPo::getVersion, request.getVersion());
        queryWrapper.eq(null != request.getStatus(), ActModelPo::getStatus, request.getStatus());
        queryWrapper.eq(null != request.getBusinessStatus(), ActModelPo::getBusinessStatus, request.getBusinessStatus());
        queryWrapper.eq(null != request.getTenantId(), ActModelPo::getTenantId, request.getTenantId());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
