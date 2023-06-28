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

    /**
      * @description 流程结束回调修改业务状态.
      * @author wjd
      * @date 2023/6/28
      * @param businessId 业务ID
     * @param approvalType 流程最终状态 1同意 2反对 3驳回到发起人
      */
    void updateBusinessStatus(String businessId, Integer approvalType);
}
