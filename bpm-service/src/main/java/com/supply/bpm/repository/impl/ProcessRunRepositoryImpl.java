package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.ProcessRunMapper;
import com.supply.bpm.model.po.ProcessRunPo;
import com.supply.bpm.model.request.ProcessRunRequest;
import com.supply.bpm.model.request.TaskRequest;
import com.supply.bpm.model.response.TaskResponse;
import com.supply.bpm.repository.IProcessRunRepository;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-06-20
 */
@Repository
public class ProcessRunRepositoryImpl extends ServiceImpl<ProcessRunMapper, ProcessRunPo> implements IProcessRunRepository {

    private final ProcessRunMapper processRunMapper;

    public ProcessRunRepositoryImpl(ProcessRunMapper processRunMapper) {
        this.processRunMapper = processRunMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(ProcessRunPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<ProcessRunPo> entityList, int batchSize) {
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(ProcessRunPo entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean updateBatchById(Collection<ProcessRunPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public ProcessRunPo getByParams(ProcessRunRequest request) {
        final LambdaQueryWrapper<ProcessRunPo> queryWrapper = this.getQueryWrapper(request);
        return processRunMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ProcessRunPo> getListByParams(ProcessRunRequest request) {
        final LambdaQueryWrapper<ProcessRunPo> queryWrapper = this.getQueryWrapper(request);
        return processRunMapper.selectList(queryWrapper);
    }

    @Override
    public Page<ProcessRunPo> getPageByParams(Page<ProcessRunPo> page, ProcessRunRequest request) {
        final LambdaQueryWrapper<ProcessRunPo> queryWrapper = this.getQueryWrapper(request);
        return processRunMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<TaskResponse> getMyTask(TaskRequest request) {
        Page<TaskRequest> page = new Page<>(request.getPageIndex(), request.getPageSize());
        return processRunMapper.getMyTask(page, request);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<ProcessRunPo> getQueryWrapper(ProcessRunRequest request) {
        LambdaQueryWrapper<ProcessRunPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), ProcessRunPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getDefinitionId()), ProcessRunPo::getDefinitionId, request.getDefinitionId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getInstanceId()), ProcessRunPo::getInstanceId, request.getInstanceId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getBusinessId()), ProcessRunPo::getBusinessId, request.getBusinessId());
        queryWrapper.eq(null != request.getStartUserId(), ProcessRunPo::getStartUserId, request.getStartUserId());
        queryWrapper.eq(null != request.getStatus(), ProcessRunPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.eq(null != request.getTenantId(), ProcessRunPo::getTenantId, request.getTenantId());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
