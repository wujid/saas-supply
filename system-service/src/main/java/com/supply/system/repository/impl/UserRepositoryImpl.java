package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.system.mapper.UserMapper;
import com.supply.system.model.po.UserPo;
import com.supply.system.model.request.UserRequest;
import com.supply.system.repository.IUserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 用户信息数据仓储层实现类.
 * @author wjd
 * @date 2022/4/1
 */
@Repository
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserPo> implements IUserRepository {

    private final UserMapper userMapper;

    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(UserPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(UserPo entity) {
        return super.updateById(entity);
    }

    @Override
    public Long getCountByParams(UserRequest request) {
        final LambdaQueryWrapper<UserPo> queryWrapper = this.getQueryWrapper(request);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public UserPo getByParams(UserRequest request) {
        final LambdaQueryWrapper<UserPo> queryWrapper = this.getQueryWrapper(request);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserPo> getListByParams(UserRequest request) {
        final LambdaQueryWrapper<UserPo> queryWrapper = this.getQueryWrapper(request);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public Page<UserPo> getPageByParams(Page<UserPo> page, UserRequest request) {
        final LambdaQueryWrapper<UserPo> queryWrapper = this.getQueryWrapper(request);
        return userMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Map<Long, UserPo> getMapByParams(UserRequest request) {
        Map<Long, UserPo> map = new HashMap<>();
        final List<UserPo> list = this.getListByParams(request);
        if (CollectionUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(UserPo::getId, e -> e, (k1, k2) -> k1));
        }
        return map;
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/4/13
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<UserPo> getQueryWrapper(UserRequest request) {
        LambdaQueryWrapper<UserPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), UserPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getWorkNumber()), UserPo::getWorkNumber, request.getWorkNumber());
        queryWrapper.eq(StrUtil.isNotBlank(request.getAccount()), UserPo::getAccount, request.getAccount());
        queryWrapper.eq(StrUtil.isNotBlank(request.getTelephone()), UserPo::getTelephone, request.getTelephone());
        queryWrapper.eq(null != request.getStatus(), UserPo::getStatus, request.getStatus());
        queryWrapper.eq(null != request.getIsManage(), UserPo::getIsManage, request.getIsManage());
        queryWrapper.eq(null != request.getTenantId(), UserPo::getTenantId, request.getTenantId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), UserPo::getName, request.getName());
        queryWrapper.eq(StrUtil.isNotBlank(request.getEmail()), UserPo::getEmail, request.getEmail());
        queryWrapper.eq(null != request.getBusinessStatus(), UserPo::getBusinessStatus, request.getBusinessStatus());
        queryWrapper.eq(null != request.getOrgId(), UserPo::getOrgId, request.getOrgId());
        queryWrapper.eq(null != request.getDepartId(), UserPo::getDepartId, request.getDepartId());

        queryWrapper.in(CollectionUtil.isNotEmpty(request.getUserIds()), UserPo::getId, request.getUserIds());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getTenantIds()), UserPo::getTenantId, request.getTenantIds());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getDeptIds()), UserPo::getDepartId, request.getDeptIds());
        queryWrapper.like(StrUtil.isNotBlank(request.getLikeWorkNumber()), UserPo::getWorkNumber, request.getLikeWorkNumber());
        queryWrapper.like(StrUtil.isNotBlank(request.getLikeAccount()), UserPo::getAccount, request.getLikeAccount());
        queryWrapper.like(StrUtil.isNotBlank(request.getLikeName()), UserPo::getName, request.getLikeName());
        queryWrapper.like(StrUtil.isNotBlank(request.getLikeTelephone()), UserPo::getTelephone, request.getLikeTelephone());
        queryWrapper.like(StrUtil.isNotBlank(request.getLikeEmail()), UserPo::getEmail, request.getLikeEmail());
        queryWrapper.ne(null != request.getNeId(), UserPo::getId, request.getNeId());
        queryWrapper.apply(null != request.getRoleId(), "id IN ( SELECT user_id FROM sys_user_role WHERE `status` = 0 AND role_id = {0} )", request.getRoleId());

        queryWrapper.apply(StrUtil.isNotBlank(request.getAuthSql()), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
