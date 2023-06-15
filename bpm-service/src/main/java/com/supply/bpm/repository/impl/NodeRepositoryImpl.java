package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.NodeButtonMapper;
import com.supply.bpm.model.po.NodeButtonPo;
import com.supply.bpm.model.request.NodeButtonRequest;
import com.supply.bpm.repository.INodeButtonRepository;
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
public class NodeRepositoryImpl extends ServiceImpl<NodeButtonMapper, NodeButtonPo> implements INodeButtonRepository {

    private final NodeButtonMapper nodeButtonMapper;

    public NodeRepositoryImpl(NodeButtonMapper nodeButtonMapper) {
        this.nodeButtonMapper = nodeButtonMapper;
    }


    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(NodeButtonPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<NodeButtonPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(NodeButtonPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<NodeButtonPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public int updateByParams(NodeButtonPo nodeUserPo, @IgnoreFill NodeButtonRequest request) {
        final LambdaQueryWrapper<NodeButtonPo> queryWrapper = this.getQueryWrapper(request);
        return nodeButtonMapper.update(nodeUserPo, queryWrapper);
    }

    @Override
    public NodeButtonPo getByParams(NodeButtonRequest request) {
        final LambdaQueryWrapper<NodeButtonPo> queryWrapper = this.getQueryWrapper(request);
        return nodeButtonMapper.selectOne(queryWrapper);
    }

    @Override
    public List<NodeButtonPo> getListByParams(NodeButtonRequest request) {
        final LambdaQueryWrapper<NodeButtonPo> queryWrapper = this.getQueryWrapper(request);
        return nodeButtonMapper.selectList(queryWrapper);
    }

    @Override
    public Page<NodeButtonPo> getPageByParams(Page<NodeButtonPo> page, NodeButtonRequest request) {
        final LambdaQueryWrapper<NodeButtonPo> queryWrapper = this.getQueryWrapper(request);
        return nodeButtonMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<NodeButtonPo> getQueryWrapper(NodeButtonRequest request) {
        LambdaQueryWrapper<NodeButtonPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), NodeButtonPo::getId, request.getId());
        queryWrapper.eq(null != request.getNodeSetId(), NodeButtonPo::getNodeSetId, request.getNodeSetId());
        queryWrapper.eq(null != request.getButtonType(), NodeButtonPo::getButtonType, request.getButtonType());
        queryWrapper.eq(null != request.getSort(), NodeButtonPo::getSort, request.getSort());
        queryWrapper.eq(null != request.getTenantId(), NodeButtonPo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), NodeButtonPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
