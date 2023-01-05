package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.NodeUserMapper;
import com.supply.bpm.model.po.NodeUserPo;
import com.supply.bpm.model.request.NodeUserRequest;
import com.supply.bpm.repository.INodeUserRepository;
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
public class NodeUserRepositoryImpl extends ServiceImpl<NodeUserMapper, NodeUserPo> implements INodeUserRepository {

    private final NodeUserMapper nodeUserMapper;

    public NodeUserRepositoryImpl(NodeUserMapper nodeUserMapper) {
        this.nodeUserMapper = nodeUserMapper;
    }


    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(NodeUserPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<NodeUserPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(NodeUserPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<NodeUserPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public int updateByParams(NodeUserPo nodeUserPo, @IgnoreFill NodeUserRequest request) {
        final LambdaQueryWrapper<NodeUserPo> queryWrapper = this.getQueryWrapper(request);
        return nodeUserMapper.update(nodeUserPo, queryWrapper);
    }

    @Override
    public NodeUserPo getByParams(NodeUserRequest request) {
        final LambdaQueryWrapper<NodeUserPo> queryWrapper = this.getQueryWrapper(request);
        return nodeUserMapper.selectOne(queryWrapper);
    }

    @Override
    public List<NodeUserPo> getListByParams(NodeUserRequest request) {
        final LambdaQueryWrapper<NodeUserPo> queryWrapper = this.getQueryWrapper(request);
        return nodeUserMapper.selectList(queryWrapper);
    }

    @Override
    public Page<NodeUserPo> getPageByParams(Page<NodeUserPo> page, NodeUserRequest request) {
        final LambdaQueryWrapper<NodeUserPo> queryWrapper = this.getQueryWrapper(request);
        return nodeUserMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<NodeUserPo> getQueryWrapper(NodeUserRequest request) {
        LambdaQueryWrapper<NodeUserPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), NodeUserPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNodeId()), NodeUserPo::getNodeId, request.getNodeId());
        queryWrapper.eq(null != request.getNodeUserType(), NodeUserPo::getNodeUserType, request.getNodeUserType());
        queryWrapper.eq(null != request.getRelationId(), NodeUserPo::getRelationId, request.getRelationId());
        queryWrapper.eq(null != request.getTenantId(), NodeUserPo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), NodeUserPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
