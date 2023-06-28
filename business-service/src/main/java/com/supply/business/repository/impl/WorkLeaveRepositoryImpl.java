package com.supply.business.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.business.mapper.WorkLeaveMapper;
import com.supply.business.model.po.WorkLeavePo;
import com.supply.business.model.request.WorkLeaveRequest;
import com.supply.business.repository.IWorkLeaveRepository;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import org.springframework.stereotype.Repository;

/**
 * @author wjd
 * @description .
 * @date 2023-06-19
 */
@Repository
public class WorkLeaveRepositoryImpl extends ServiceImpl<WorkLeaveMapper, WorkLeavePo> implements IWorkLeaveRepository {

    private final WorkLeaveMapper workLeaveMapper;

    public WorkLeaveRepositoryImpl(WorkLeaveMapper workLeaveMapper) {
        this.workLeaveMapper = workLeaveMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(WorkLeavePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(WorkLeavePo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public void updateByBusinessId(WorkLeavePo workLeavePo, @IgnoreFill String businessId) {
        WorkLeaveRequest request = new WorkLeaveRequest();
        request.setBusinessId(businessId);
        final LambdaQueryWrapper<WorkLeavePo> queryWrapper = this.getQueryWrapper(request);
        workLeaveMapper.update(workLeavePo, queryWrapper);
    }

    @Override
    public WorkLeavePo getByParams(WorkLeaveRequest request) {
        final LambdaQueryWrapper<WorkLeavePo> queryWrapper = this.getQueryWrapper(request);
        return workLeaveMapper.selectOne(queryWrapper);
    }


    @Override
    public Page<WorkLeavePo> getPageByParams(Page<WorkLeavePo> page, WorkLeaveRequest request) {
        final LambdaQueryWrapper<WorkLeavePo> queryWrapper = this.getQueryWrapper(request);
        return workLeaveMapper.selectPage(page, queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/9/8
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<WorkLeavePo> getQueryWrapper(WorkLeaveRequest request) {
        LambdaQueryWrapper<WorkLeavePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), WorkLeavePo::getId, request.getId());
        queryWrapper.eq(null != request.getBusinessId(), WorkLeavePo::getBusinessId, request.getBusinessId());
        queryWrapper.eq(null != request.getBusinessStatus(), WorkLeavePo::getBusinessStatus, request.getStatus());
        queryWrapper.eq(null != request.getStatus(), WorkLeavePo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(), request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
