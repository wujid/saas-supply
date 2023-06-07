package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.UserNodeMapper;
import com.supply.bpm.model.po.UserNodePo;
import com.supply.bpm.model.request.UserNodeRequest;
import com.supply.bpm.repository.IUserNodeRepository;
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
public class UserNodeRepositoryImpl extends ServiceImpl<UserNodeMapper, UserNodePo> implements IUserNodeRepository {

    private final UserNodeMapper userNodeMapper;

    public UserNodeRepositoryImpl(UserNodeMapper userNodeMapper) {
        this.userNodeMapper = userNodeMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(UserNodePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<UserNodePo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(UserNodePo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<UserNodePo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public int updateByParams(UserNodePo userNodePo, @IgnoreFill UserNodeRequest request) {
        final LambdaQueryWrapper<UserNodePo> queryWrapper = this.getQueryWrapper(request);
        return userNodeMapper.update(userNodePo, queryWrapper);
    }

    @Override
    public UserNodePo getByParams(UserNodeRequest request) {
        final LambdaQueryWrapper<UserNodePo> queryWrapper = this.getQueryWrapper(request);
        return userNodeMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserNodePo> getListByParams(UserNodeRequest request) {
        final LambdaQueryWrapper<UserNodePo> queryWrapper = this.getQueryWrapper(request);
        return userNodeMapper.selectList(queryWrapper);
    }

    @Override
    public Page<UserNodePo> getPageByParams(Page<UserNodePo> page, UserNodeRequest request) {
        final LambdaQueryWrapper<UserNodePo> queryWrapper = this.getQueryWrapper(request);
        return userNodeMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<UserNodePo> getQueryWrapper(UserNodeRequest request) {
        LambdaQueryWrapper<UserNodePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), UserNodePo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getDefinitionId()), UserNodePo::getDefinitionId, request.getDefinitionId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNodeId()), UserNodePo::getNodeId, request.getNodeId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNodeName()), UserNodePo::getNodeName, request.getNodeName());
        queryWrapper.eq(null != request.getTenantId(), UserNodePo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), UserNodePo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
