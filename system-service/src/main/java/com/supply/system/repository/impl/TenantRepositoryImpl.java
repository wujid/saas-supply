package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.system.mapper.TenantMapper;
import com.supply.system.model.po.TenantPo;
import com.supply.system.model.request.TenantRequest;
import com.supply.system.repository.ITenantRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
@Repository
public class TenantRepositoryImpl extends ServiceImpl<TenantMapper, TenantPo> implements ITenantRepository {

    private final TenantMapper tenantMapper;

    public TenantRepositoryImpl(TenantMapper tenantMapper) {
        this.tenantMapper = tenantMapper;
    }


    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(TenantPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<TenantPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(TenantPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<TenantPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public TenantPo getByParams(TenantRequest request) {
        final LambdaQueryWrapper<TenantPo> queryWrapper = this.getQueryWrapper(request);
        return tenantMapper.selectOne(queryWrapper);
    }

    @Override
    public List<TenantPo> getListByParams(TenantRequest request) {
        final LambdaQueryWrapper<TenantPo> queryWrapper = this.getQueryWrapper(request);
        return tenantMapper.selectList(queryWrapper);
    }

    @Override
    public Long getCountByParams(TenantRequest request) {
        final LambdaQueryWrapper<TenantPo> queryWrapper = this.getQueryWrapper(request);
        return tenantMapper.selectCount(queryWrapper);
    }

    @Override
    public Page<TenantPo> getPageByParams(Page<TenantPo> page, TenantRequest request) {
        final LambdaQueryWrapper<TenantPo> queryWrapper = this.getQueryWrapper(request);
        return tenantMapper.selectPage(page, queryWrapper);
    }


    /**
     * @description 通用非空查询条件.
     * @author wjd
     * @date 2022/7/27
     * @param request 条件
     * @return 查询条件组装结果
     */
    private LambdaQueryWrapper<TenantPo> getQueryWrapper(TenantRequest request) {
        LambdaQueryWrapper<TenantPo> queryWrapper = Wrappers.lambdaQuery(TenantPo.class);
        queryWrapper.eq(null != request.getId(), TenantPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getCode()), TenantPo::getCode, request.getCode());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), TenantPo::getName, request.getName());
        queryWrapper.eq(null != request.getType(), TenantPo::getType, request.getType());
        queryWrapper.eq(null != request.getBusinessStatus(), TenantPo::getBusinessStatus, request.getBusinessStatus());
        queryWrapper.eq(StrUtil.isNotBlank(request.getClientId()), TenantPo::getClientId, request.getClientId());
        queryWrapper.eq(null != request.getStatus(), TenantPo::getStatus, request.getStatus());
        queryWrapper.like(StrUtil.isNotBlank(request.getLikeName()), TenantPo::getName, request.getLikeName());
        queryWrapper.apply(StrUtil.isNotBlank(request.getEndTimeStart()), "DATE_FORMAT(end_time, '%Y-%m-%d') >= DATE_FORMAT({0}, '%Y-%m-%d')", request.getEndTimeStart());
        queryWrapper.apply(StrUtil.isNotBlank(request.getEndTimeEnd()), "DATE_FORMAT(end_time, '%Y-%m-%d') <= DATE_FORMAT({0}, '%Y-%m-%d')", request.getEndTimeEnd());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
