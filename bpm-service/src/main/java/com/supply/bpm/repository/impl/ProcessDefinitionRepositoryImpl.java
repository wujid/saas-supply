package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.ProcessDefinitionMapper;
import com.supply.bpm.model.po.ProcessDefinitionPo;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.repository.IProcessDefinitionRepository;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
@Repository
public class ProcessDefinitionRepositoryImpl extends ServiceImpl<ProcessDefinitionMapper, ProcessDefinitionPo> implements IProcessDefinitionRepository {

    private final ProcessDefinitionMapper processDefinitionMapper;

    public ProcessDefinitionRepositoryImpl(ProcessDefinitionMapper processDefinitionMapper) {
        this.processDefinitionMapper = processDefinitionMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(ProcessDefinitionPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<ProcessDefinitionPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(ProcessDefinitionPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<ProcessDefinitionPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public int updateByParams(ProcessDefinitionPo processDefinitionPo, @IgnoreFill ProcessDefinitionRequest request) {
        final LambdaQueryWrapper<ProcessDefinitionPo> queryWrapper = this.getQueryWrapper(request);
        return processDefinitionMapper.update(processDefinitionPo, queryWrapper);
    }

    @Override
    public ProcessDefinitionPo getByParams(ProcessDefinitionRequest request) {
        final LambdaQueryWrapper<ProcessDefinitionPo> queryWrapper = this.getQueryWrapper(request);
        return processDefinitionMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ProcessDefinitionPo> getListByParams(ProcessDefinitionRequest request) {
        final LambdaQueryWrapper<ProcessDefinitionPo> queryWrapper = this.getQueryWrapper(request);
        return processDefinitionMapper.selectList(queryWrapper);
    }

    @Override
    public Page<ProcessDefinitionPo> getPageByParams(Page<ProcessDefinitionPo> page, ProcessDefinitionRequest request) {
        final LambdaQueryWrapper<ProcessDefinitionPo> queryWrapper = this.getQueryWrapper(request);
        return processDefinitionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long getCountByParams(ProcessDefinitionRequest request) {
        final LambdaQueryWrapper<ProcessDefinitionPo> queryWrapper = this.getQueryWrapper(request);
        return processDefinitionMapper.selectCount(queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<ProcessDefinitionPo> getQueryWrapper(ProcessDefinitionRequest request) {
        LambdaQueryWrapper<ProcessDefinitionPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), ProcessDefinitionPo::getId, request.getId());
        queryWrapper.eq(null != request.getCategoryId(), ProcessDefinitionPo::getCategoryId, request.getCategoryId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getDeploymentId()), ProcessDefinitionPo::getDeploymentId, request.getDeploymentId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getDefinitionId()), ProcessDefinitionPo::getDefinitionId, request.getDefinitionId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getProcessName()), ProcessDefinitionPo::getProcessName, request.getProcessName());
        queryWrapper.eq(StrUtil.isNotBlank(request.getProcessKey()), ProcessDefinitionPo::getProcessKey, request.getProcessKey());
        queryWrapper.eq(null != request.getVersion(), ProcessDefinitionPo::getVersion, request.getVersion());
        queryWrapper.eq(null != request.getIsDefault(), ProcessDefinitionPo::getIsDefault, request.getIsDefault());
        queryWrapper.eq(null != request.getBusinessStatus(), ProcessDefinitionPo::getBusinessStatus, request.getBusinessStatus());
        queryWrapper.eq(null != request.getTenantId(), ProcessDefinitionPo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), ProcessDefinitionPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
