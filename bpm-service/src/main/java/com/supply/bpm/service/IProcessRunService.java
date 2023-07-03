package com.supply.bpm.service;

import com.supply.bpm.model.request.TaskHandleRequest;
import com.supply.common.web.model.BpmRequestEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wjd
 * @description .
 * @date 2023-06-20
 */
public interface IProcessRunService {

    /**
      * @description 流程发起.
      * @author wjd
      * @date 2023/6/21
      * @param request 发起流程参数
      */
    void startProcess(BpmRequestEntity request);

    /**
      * @description 完成任务.
      * @author wjd
      * @date 2023/6/27
      * @param request 条件信息
      */
    void completeTask(TaskHandleRequest request);

    /**
      * @description 流程运行图.
      * @author wjd
      * @date 2023/6/29
      * @param instanceId 流程运行实例
      */
    void getActImage(String instanceId, HttpServletResponse response);
}
