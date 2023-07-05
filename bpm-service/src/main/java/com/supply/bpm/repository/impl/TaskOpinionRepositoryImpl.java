package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.TaskOpinionMapper;
import com.supply.bpm.model.po.TaskOpinionPo;
import com.supply.bpm.model.request.TaskOpinionRequest;
import com.supply.bpm.repository.ITaskOpinionRepository;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-07-04
 */
@Repository
public class TaskOpinionRepositoryImpl extends ServiceImpl<TaskOpinionMapper, TaskOpinionPo> implements ITaskOpinionRepository {

    private final TaskOpinionMapper taskOpinionMapper;

    public TaskOpinionRepositoryImpl(TaskOpinionMapper taskOpinionMapper) {
        this.taskOpinionMapper = taskOpinionMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(TaskOpinionPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<TaskOpinionPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(TaskOpinionPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<TaskOpinionPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(TaskOpinionPo taskOpinionPo, @IgnoreFill TaskOpinionRequest request) {
        final LambdaQueryWrapper<TaskOpinionPo> queryWrapper = this.getQueryWrapper(request);
        return taskOpinionMapper.update(taskOpinionPo, queryWrapper);
    }

    @Override
    public TaskOpinionPo getByParams(TaskOpinionRequest request) {
        final LambdaQueryWrapper<TaskOpinionPo> queryWrapper = this.getQueryWrapper(request);
        return taskOpinionMapper.selectOne(queryWrapper);
    }

    @Override
    public List<TaskOpinionPo> getListByParams(TaskOpinionRequest request) {
        final LambdaQueryWrapper<TaskOpinionPo> queryWrapper = this.getQueryWrapper(request);
        return taskOpinionMapper.selectList(queryWrapper);
    }

    @Override
    public Page<TaskOpinionPo> getPageByParams(Page<TaskOpinionPo> page, TaskOpinionRequest request) {
        final LambdaQueryWrapper<TaskOpinionPo> queryWrapper = this.getQueryWrapper(request);
        return taskOpinionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public TaskOpinionPo getByTaskId(String taskId) {
        TaskOpinionRequest request = new TaskOpinionRequest();
        request.setTaskId(taskId);
        final LambdaQueryWrapper<TaskOpinionPo> queryWrapper = this.getQueryWrapper(request);
        return taskOpinionMapper.selectOne(queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<TaskOpinionPo> getQueryWrapper(TaskOpinionRequest request) {
        LambdaQueryWrapper<TaskOpinionPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), TaskOpinionPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getInstanceId()), TaskOpinionPo::getInstanceId, request.getInstanceId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getTaskId()), TaskOpinionPo::getTaskId, request.getTaskId());
        queryWrapper.eq(null != request.getTenantId(), TaskOpinionPo::getTenantId, request.getTenantId());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.eq(null != request.getStatus(), TaskOpinionPo::getStatus, request.getStatus());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
