package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.ProcessRunPo;
import com.supply.bpm.model.request.ProcessRunRequest;
import com.supply.bpm.model.request.TaskRequest;
import com.supply.bpm.model.response.TaskResponse;
import com.supply.common.web.annotation.IgnoreFill;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-06-20
 */
public interface IProcessRunRepository extends IService<ProcessRunPo> {

    int updateByParams(ProcessRunPo processRunPo, @IgnoreFill ProcessRunRequest request);

    ProcessRunPo getByParams(ProcessRunRequest request);

    List<ProcessRunPo> getListByParams(ProcessRunRequest request);

    Page<ProcessRunPo> getPageByParams(Page<ProcessRunPo> page, ProcessRunRequest request);

    ProcessRunPo getByInstanceId(String instanceId);

    ProcessRunPo getByBusinessId(String businessId);

    /**
      * @description 我的待办列表.
      * @author wjd
      * @date 2023/6/25
      * @param request 查询条件
      * @return 我的待办
      */
    Page<TaskResponse> getMyTask(TaskRequest request);

    /**
     * @description 我发起的流程.
     * @author wjd
     * @date 2023/7/4
     * @param request 条件信息
     * @return 我发起的流程
     */
    Page<TaskResponse> getMyStart(TaskRequest request);

    /**
     * @description 我参与的流程.
     * @author wjd
     * @date 2023/7/4
     * @param request 条件信息
     * @return 我参与的流程
     */
    Page<TaskResponse> getMyAttend(TaskRequest request);
}
