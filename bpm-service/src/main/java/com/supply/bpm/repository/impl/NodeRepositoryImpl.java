package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.NodeMapper;
import com.supply.bpm.model.po.NodePo;
import com.supply.bpm.model.request.NodeRequest;
import com.supply.bpm.repository.INodeRepository;
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
public class NodeRepositoryImpl extends ServiceImpl<NodeMapper, NodePo> implements INodeRepository {

    private final NodeMapper nodeMapper;

    public NodeRepositoryImpl(NodeMapper nodeMapper) {
        this.nodeMapper = nodeMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(NodePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<NodePo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(NodePo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<NodePo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public int updateByParams(NodePo nodePo, @IgnoreFill NodeRequest request) {
        final LambdaQueryWrapper<NodePo> queryWrapper = this.getQueryWrapper(request);
        return nodeMapper.update(nodePo, queryWrapper);
    }

    @Override
    public NodePo getByParams(NodeRequest request) {
        final LambdaQueryWrapper<NodePo> queryWrapper = this.getQueryWrapper(request);
        return nodeMapper.selectOne(queryWrapper);
    }

    @Override
    public List<NodePo> getListByParams(NodeRequest request) {
        final LambdaQueryWrapper<NodePo> queryWrapper = this.getQueryWrapper(request);
        return nodeMapper.selectList(queryWrapper);
    }

    @Override
    public Page<NodePo> getPageByParams(Page<NodePo> page, NodeRequest request) {
        final LambdaQueryWrapper<NodePo> queryWrapper = this.getQueryWrapper(request);
        return nodeMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<NodePo> getQueryWrapper(NodeRequest request) {
        LambdaQueryWrapper<NodePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), NodePo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getDefinitionId()), NodePo::getDefinitionId, request.getDefinitionId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNodeId()), NodePo::getNodeId, request.getNodeId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNodeName()), NodePo::getNodeName, request.getNodeName());
        queryWrapper.eq(null != request.getTenantId(), NodePo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), NodePo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
