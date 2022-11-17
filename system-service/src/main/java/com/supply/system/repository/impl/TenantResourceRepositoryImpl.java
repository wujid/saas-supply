package com.supply.system.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.system.mapper.TenantResourceMapper;
import com.supply.system.model.po.TenantResourcePo;
import com.supply.system.model.request.TenantResourceRequest;
import com.supply.system.repository.ITenantResourceRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-09
 */
@Repository
public class TenantResourceRepositoryImpl extends ServiceImpl<TenantResourceMapper, TenantResourcePo> implements ITenantResourceRepository {

    private final TenantResourceMapper tenantResourceMapper;

    public TenantResourceRepositoryImpl(TenantResourceMapper tenantResourceMapper) {
        this.tenantResourceMapper = tenantResourceMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(TenantResourcePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<TenantResourcePo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<TenantResourcePo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateByParams(TenantResourcePo tenantResourcePo, @IgnoreFill TenantResourceRequest request) {
        final LambdaQueryWrapper<TenantResourcePo> queryWrapper = this.getQueryWrapper(request);
        return this.update(tenantResourcePo, queryWrapper);
    }

    @Override
    public List<TenantResourcePo> getListByParams(TenantResourceRequest request) {
        final LambdaQueryWrapper<TenantResourcePo> queryWrapper = this.getQueryWrapper(request);
        return tenantResourceMapper.selectList(queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/4/13
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<TenantResourcePo> getQueryWrapper(TenantResourceRequest request) {
        LambdaQueryWrapper<TenantResourcePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), TenantResourcePo::getId, request.getId());
        queryWrapper.eq(null != request.getTenantId(), TenantResourcePo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getResourceId(), TenantResourcePo::getResourceId, request.getResourceId());
        queryWrapper.eq(null != request.getStatus(), TenantResourcePo::getStatus, request.getStatus());
        return queryWrapper;
    }
}
