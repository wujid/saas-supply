package com.supply.bpm.service;

import com.supply.common.web.model.BpmRequestEntity;

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
}
