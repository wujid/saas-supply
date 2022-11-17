package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.system.mapper.UserRoleMapper;
import com.supply.system.model.po.UserRolePo;
import com.supply.system.model.request.UserRoleRequest;
import com.supply.system.repository.IUserRoleRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
@Repository
public class UserRoleRepositoryImpl extends ServiceImpl<UserRoleMapper, UserRolePo> implements IUserRoleRepository {

    private final UserRoleMapper userRoleMapper;

    public UserRoleRepositoryImpl(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(UserRolePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<UserRolePo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateByParams(UserRolePo userRolePo, @IgnoreFill UserRoleRequest request) {
        final LambdaQueryWrapper<UserRolePo> queryWrapper = this.getQueryWrapper(request);
        return this.update(userRolePo, queryWrapper);
    }

    @Override
    public List<UserRolePo> getListByParams(UserRoleRequest request) {
        final LambdaQueryWrapper<UserRolePo> queryWrapper = this.getQueryWrapper(request);
        return userRoleMapper.selectList(queryWrapper);
    }

    @Override
    public Long getCountByParams(UserRoleRequest request) {
        final LambdaQueryWrapper<UserRolePo> queryWrapper = this.getQueryWrapper(request);
        return userRoleMapper.selectCount(queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/4/13
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<UserRolePo> getQueryWrapper(UserRoleRequest request) {
        LambdaQueryWrapper<UserRolePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), UserRolePo::getId, request.getId());
        queryWrapper.eq(null != request.getUserId(), UserRolePo::getUserId, request.getUserId());
        queryWrapper.eq(null != request.getRoleId(), UserRolePo::getRoleId, request.getRoleId());
        queryWrapper.eq(null != request.getStatus(), UserRolePo::getStatus, request.getStatus());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getUserIds()), UserRolePo::getUserId, request.getUserIds());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getRoleIds()), UserRolePo::getRoleId, request.getRoleIds());
        return queryWrapper;
    }
}
