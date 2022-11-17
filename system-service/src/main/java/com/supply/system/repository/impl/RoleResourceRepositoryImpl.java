package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.system.mapper.RoleResourceMapper;
import com.supply.system.model.po.RoleResourcePo;
import com.supply.system.model.request.RoleResourceRequest;
import com.supply.system.repository.IRoleResourceRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
@Repository
public class RoleResourceRepositoryImpl extends ServiceImpl<RoleResourceMapper, RoleResourcePo> implements IRoleResourceRepository {

    private final RoleResourceMapper roleResourceMapper;


    public RoleResourceRepositoryImpl(RoleResourceMapper roleResourceMapper) {
        this.roleResourceMapper = roleResourceMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(RoleResourcePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<RoleResourcePo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(RoleResourcePo roleResourcePo, @IgnoreFill RoleResourceRequest request) {
        final LambdaQueryWrapper<RoleResourcePo> queryWrapper = this.getQueryWrapper(request);
        return roleResourceMapper.update(roleResourcePo, queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/4/13
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<RoleResourcePo> getQueryWrapper(RoleResourceRequest request) {
        LambdaQueryWrapper<RoleResourcePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), RoleResourcePo::getId, request.getId());
        queryWrapper.eq(null != request.getRoleId(), RoleResourcePo::getRoleId, request.getRoleId());
        queryWrapper.eq(null != request.getResourceId(), RoleResourcePo::getResourceId, request.getResourceId());
        queryWrapper.eq(null != request.getStatus(), RoleResourcePo::getStatus, request.getStatus());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getRoleIds()), RoleResourcePo::getRoleId, request.getRoleIds());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getResourceIds()), RoleResourcePo::getResourceId, request.getResourceIds());
        return queryWrapper;
    }
}
