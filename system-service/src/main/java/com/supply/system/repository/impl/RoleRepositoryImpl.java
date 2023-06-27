package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.system.mapper.RoleMapper;
import com.supply.system.model.po.RolePo;
import com.supply.system.model.request.RoleRequest;
import com.supply.system.repository.IRoleRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
@Repository
public class RoleRepositoryImpl extends ServiceImpl<RoleMapper, RolePo> implements IRoleRepository {

    private final RoleMapper roleMapper;

    public RoleRepositoryImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(RolePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean saveBatch(Collection<RolePo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(RolePo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<RolePo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public List<RolePo> getRoleByUserId(Long userId) {
        return roleMapper.getRoleByUserId(userId);
    }

    @Override
    public Long getCountByParams(RoleRequest request) {
        final LambdaQueryWrapper<RolePo> queryWrapper = this.getQueryWrapper(request);
        queryWrapper.ne(null != request.getNeId(), RolePo::getId, request.getNeId());
        return roleMapper.selectCount(queryWrapper);
    }

    @Override
    public RolePo getByParams(RoleRequest request) {
        final LambdaQueryWrapper<RolePo> queryWrapper = this.getQueryWrapper(request);
        return roleMapper.selectOne(queryWrapper);
    }

    @Override
    public List<RolePo> getListByParams(RoleRequest request) {
        final LambdaQueryWrapper<RolePo> queryWrapper = this.getQueryWrapper(request);
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public Page<RolePo> getPageByParams(Page<RolePo> page, RoleRequest request) {
        final LambdaQueryWrapper<RolePo> queryWrapper = this.getQueryWrapper(request);
        return roleMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Map<Long, RolePo> getMapByParams(RoleRequest request) {
        Map<Long, RolePo> map = new HashMap<>();
        final List<RolePo> list = this.getListByParams(request);
        if (CollectionUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(RolePo::getId, e -> e, (k1, k2) -> k1));
        }
        return map;
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/8/3
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<RolePo> getQueryWrapper(RoleRequest request) {
        LambdaQueryWrapper<RolePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), RolePo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getCode()), RolePo::getCode, request.getCode());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), RolePo::getName, request.getName());
        queryWrapper.eq(null != request.getTenantId(), RolePo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getIsMain(), RolePo::getIsMain, request.getIsMain());
        queryWrapper.eq(null != request.getIsSystem(), RolePo::getIsSystem, request.getIsSystem());
        queryWrapper.eq(null != request.getBusinessStatus(), RolePo::getBusinessStatus, request.getBusinessStatus());
        queryWrapper.eq(null != request.getStatus(), RolePo::getStatus, request.getStatus());
        queryWrapper.ne(null != request.getNeId(), RolePo::getId, request.getNeId());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getRoleIds()), RolePo::getId, request.getRoleIds());
        queryWrapper.notIn(CollectionUtil.isNotEmpty(request.getNotInRoleIds()), RolePo::getId, request.getNotInRoleIds());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
