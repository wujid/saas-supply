package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.model.response.ProcessDefinitionResponse;

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


    /**
     * @description 根据自定义条件查询带流程定义信息集.
     * @author wjd
     * @date 2023/6/7
     * @param request 查询条件
     * @return 带分页的流程定义信息集
     */
    IPage<ProcessDefinitionResponse> getProcessDefinitionPage(ProcessDefinitionRequest request);
}
