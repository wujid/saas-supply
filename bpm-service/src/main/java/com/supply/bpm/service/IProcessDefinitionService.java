package com.supply.bpm.service;

import com.supply.bpm.model.request.ProcessDefinitionRequest;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
public interface IProcessDefinitionService {

    /**
      * @description 新增流程部署.
      * @author wjd
      * @date 2023/6/7
      * @param request 待新增的流程实体信息
      */
    void addDeployment(ProcessDefinitionRequest request);
}
