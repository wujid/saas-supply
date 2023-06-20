package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.NodeSetMapper;
import com.supply.bpm.model.po.NodeSetPo;
import com.supply.bpm.model.request.NodeSetRequest;
import com.supply.bpm.repository.INodeSetRepository;
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
public class NodeSetRepositoryImpl extends ServiceImpl<NodeSetMapper, NodeSetPo> implements INodeSetRepository {

    private final NodeSetMapper nodeSetMapper;

    public NodeSetRepositoryImpl(NodeSetMapper nodeSetMapper) {
        this.nodeSetMapper = nodeSetMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(NodeSetPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<NodeSetPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(NodeSetPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<NodeSetPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public int updateByParams(NodeSetPo nodeSetPo, @IgnoreFill NodeSetRequest request) {
        final LambdaQueryWrapper<NodeSetPo> queryWrapper = this.getQueryWrapper(request);
        return nodeSetMapper.update(nodeSetPo, queryWrapper);
    }

    @Override
    public NodeSetPo getByParams(NodeSetRequest request) {
        final LambdaQueryWrapper<NodeSetPo> queryWrapper = this.getQueryWrapper(request);
        return nodeSetMapper.selectOne(queryWrapper);
    }

    @Override
    public List<NodeSetPo> getListByParams(NodeSetRequest request) {
        final LambdaQueryWrapper<NodeSetPo> queryWrapper = this.getQueryWrapper(request);
        return nodeSetMapper.selectList(queryWrapper);
    }

    @Override
    public Page<NodeSetPo> getPageByParams(Page<NodeSetPo> page, NodeSetRequest request) {
        final LambdaQueryWrapper<NodeSetPo> queryWrapper = this.getQueryWrapper(request);
        return nodeSetMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<NodeSetPo> getQueryWrapper(NodeSetRequest request) {
        LambdaQueryWrapper<NodeSetPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), NodeSetPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getDefinitionId()), NodeSetPo::getDefinitionId, request.getDefinitionId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNodeId()), NodeSetPo::getNodeId, request.getNodeId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNodeName()), NodeSetPo::getNodeName, request.getNodeName());
        queryWrapper.eq(null != request.getNodeType(), NodeSetPo::getNodeType, request.getNodeType());
        queryWrapper.eq(null != request.getSort(), NodeSetPo::getSort, request.getSort());
        queryWrapper.eq(null != request.getTenantId(), NodeSetPo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), NodeSetPo::getStatus, request.getStatus());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getNodeTypes()), NodeSetPo::getNodeType, request.getNodeTypes());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
