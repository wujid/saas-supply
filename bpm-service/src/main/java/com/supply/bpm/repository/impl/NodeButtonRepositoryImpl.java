package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
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
public class NodeButtonRepositoryImpl extends ServiceImpl<NodeButtonMapper, NodeButtonPo> implements INodeButtonRepository {

    private final NodeButtonMapper nodeButtonMapper;

    public NodeButtonRepositoryImpl(NodeButtonMapper nodeButtonMapper) {
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
    public int updateByParams(NodeButtonPo nodeButtonPo, @IgnoreFill NodeButtonRequest request) {
        final LambdaQueryWrapper<NodeButtonPo> queryWrapper = this.getQueryWrapper(request);
        return nodeButtonMapper.update(nodeButtonPo, queryWrapper);
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
     * @description ????????????????????????.
     * @date 2022/10/12
     * @param request ??????
     * @return ????????????????????????
     **/
    private LambdaQueryWrapper<NodeButtonPo> getQueryWrapper(NodeButtonRequest request) {
        LambdaQueryWrapper<NodeButtonPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), NodeButtonPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNodeId()), NodeButtonPo::getNodeId, request.getNodeId());
        queryWrapper.eq(null != request.getButtonType(), NodeButtonPo::getButtonType, request.getButtonType());
        queryWrapper.eq(StrUtil.isNotBlank(request.getButtonName()), NodeButtonPo::getButtonName, request.getButtonName());
        queryWrapper.eq(null != request.getSort(), NodeButtonPo::getSort, request.getSort());
        queryWrapper.eq(null != request.getTenantId(), NodeButtonPo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), NodeButtonPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
