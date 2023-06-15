package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.UserNodeButtonMapper;
import com.supply.bpm.model.po.UserNodeButtonPo;
import com.supply.bpm.model.request.UserNodeButtonRequest;
import com.supply.bpm.repository.IUserNodeButtonRepository;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-04
 */
@Repository
public class UserNodeUserRepositoryImpl extends ServiceImpl<UserNodeButtonMapper, UserNodeButtonPo> implements IUserNodeButtonRepository {

    private final UserNodeButtonMapper userNodeButtonMapper;

    public UserNodeUserRepositoryImpl(UserNodeButtonMapper userNodeButtonMapper) {
        this.userNodeButtonMapper = userNodeButtonMapper;
    }


    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(UserNodeButtonPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<UserNodeButtonPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(UserNodeButtonPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<UserNodeButtonPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public int updateByParams(UserNodeButtonPo nodeUserPo, @IgnoreFill UserNodeButtonRequest request) {
        final LambdaQueryWrapper<UserNodeButtonPo> queryWrapper = this.getQueryWrapper(request);
        return userNodeButtonMapper.update(nodeUserPo, queryWrapper);
    }

    @Override
    public UserNodeButtonPo getByParams(UserNodeButtonRequest request) {
        final LambdaQueryWrapper<UserNodeButtonPo> queryWrapper = this.getQueryWrapper(request);
        return userNodeButtonMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserNodeButtonPo> getListByParams(UserNodeButtonRequest request) {
        final LambdaQueryWrapper<UserNodeButtonPo> queryWrapper = this.getQueryWrapper(request);
        return userNodeButtonMapper.selectList(queryWrapper);
    }

    @Override
    public Page<UserNodeButtonPo> getPageByParams(Page<UserNodeButtonPo> page, UserNodeButtonRequest request) {
        final LambdaQueryWrapper<UserNodeButtonPo> queryWrapper = this.getQueryWrapper(request);
        return userNodeButtonMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<UserNodeButtonPo> getQueryWrapper(UserNodeButtonRequest request) {
        LambdaQueryWrapper<UserNodeButtonPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), UserNodeButtonPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getUserNodeId()), UserNodeButtonPo::getUserNodeId, request.getUserNodeId());
        queryWrapper.eq(null != request.getButtonType(), UserNodeButtonPo::getButtonType, request.getButtonType());
        queryWrapper.eq(null != request.getSort(), UserNodeButtonPo::getSort, request.getSort());
        queryWrapper.eq(null != request.getTenantId(), UserNodeButtonPo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), UserNodeButtonPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
