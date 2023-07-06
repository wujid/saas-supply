package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.TaskRequest;
import com.supply.bpm.model.response.TaskResponse;

/**
 * @author wjd
 * @description .
 * @date 2023-06-19
 */
public interface ITaskManageService {

    /**
      * @description 我的待办列表.
      * @author wjd
      * @date 2023/6/25
      * @param request 查询条件
      * @return 我的待办
      */
    IPage<TaskResponse> getMyTask(TaskRequest request);

    /**
     * @description 我发起的流程列表.
     * @author wjd
     * @date 2023/6/25
     * @param request 查询条件
     * @return 我的待办
     */
    IPage<TaskResponse> getMyStart(TaskRequest request);

    /**
     * @description 我参与的流程.
     * @author wjd
     * @date 2023/6/25
     * @param request 查询条件
     * @return 我参与的流程
     */
    IPage<TaskResponse> getMyAttend(TaskRequest request);
}
