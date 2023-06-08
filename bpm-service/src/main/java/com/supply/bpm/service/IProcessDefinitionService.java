package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.model.response.ProcessDefinitionResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
      * @description 流程挂起.
      * @author wjd
      * @date 2023/6/7
      * @param defId 流程定义拓展表主键ID
      */
    void suspendProcess(Long defId);

    /**
     * @description 流程激活.
     * @author wjd
     * @date 2023/6/7
     * @param defId 流程定义拓展表主键ID
     */
    void activeProcess(Long defId);

    /**
      * @description 流程删除.
      * @author wjd
      * @date 2023/6/7
      * @param defId 流程定义拓展表主键ID
      * @return void
      */
    void delProcess(Long defId);

    /**
      * @description 修改流程标题.
      * @author wjd
      * @date 2023/6/8
      * @param request 待修改的实体信息
      */
    void updateProcessTitle(ProcessDefinitionRequest request);

    /**
     * @description 根据自定义条件查询带流程定义信息集.
     * @author wjd
     * @date 2023/6/7
     * @param request 查询条件
     * @return 带分页的流程定义信息集
     */
    IPage<ProcessDefinitionResponse> getProcessDefinitionPage(ProcessDefinitionRequest request);

    /**
      * @description 获取流程定义xml.
      * @author wjd
      * @date 2023/6/8
      * @param deploymentId 流程部署ID
      * @param processName 流程名称
      */
    void getProcessDefinitionXml(String deploymentId, String processName, HttpServletResponse response) throws IOException;
}
