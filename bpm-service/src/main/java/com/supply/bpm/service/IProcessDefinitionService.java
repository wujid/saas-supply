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
      * @description 根据自定义条件查询带流程定义信息集.
      * @author wjd
      * @date 2022/12/20
      * @param request 查询条件
      * @return 带分页的流程定义信息集
      */
    IPage<ProcessDefinitionResponse> getProcessDefinitionPage(ProcessDefinitionRequest request);
}
