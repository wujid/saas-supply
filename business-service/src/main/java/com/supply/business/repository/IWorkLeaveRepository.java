package com.supply.business.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.business.model.po.WorkLeavePo;
import com.supply.business.model.request.WorkLeaveRequest;

/**
 * @author wjd
 * @description .
 * @date 2023-06-19
 */
public interface IWorkLeaveRepository extends IService<WorkLeavePo> {
    WorkLeavePo getByParams(WorkLeaveRequest request);

    Page<WorkLeavePo> getPageByParams(Page<WorkLeavePo> page, WorkLeaveRequest request);
}
