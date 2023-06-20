package com.supply.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.business.model.request.WorkLeaveRequest;
import com.supply.business.model.response.WorkLeaveResponse;

/**
 * @author wjd
 * @description .
 * @date 2023-06-19
 */
public interface IWorkLeaveService {
    void addWorkLeave(WorkLeaveRequest request);

    IPage<WorkLeaveResponse> getPageByParams(WorkLeaveRequest request);

    WorkLeaveResponse getInfoById(Long id);
}
