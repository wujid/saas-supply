package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.supply.bpm.constant.BpmConstant;
import com.supply.bpm.model.po.UserNodePo;
import com.supply.bpm.model.po.ProcessDefinitionPo;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.repository.IProcessDefinitionRepository;
import com.supply.bpm.service.IProcessDefinitionService;
import com.supply.bpm.util.ActivityUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
@Service
public class ProcessDefinitionServiceImpl implements IProcessDefinitionService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessDefinitionServiceImpl.class);

    private final RepositoryService repositoryService;

    private final IProcessDefinitionRepository processDefinitionRepository;

    public ProcessDefinitionServiceImpl(RepositoryService repositoryService, IProcessDefinitionRepository processDefinitionRepository) {
        this.repositoryService = repositoryService;
        this.processDefinitionRepository = processDefinitionRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDeployment(ProcessDefinitionRequest request) {
        logger.info("[新增流程部署xml]---待新增的实体信息为{}", JSON.toJSONString(request));

        // 保存流程部署xml信息
        final String resourceName = StrUtil.concat(true, request.getProcessName(), ".bpmn");
        final Deployment deployment = repositoryService.createDeployment()
                .addString(resourceName, request.getBpmnXml()).deploy();
        // 根据流程部署ID查询出流程定义信息
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        // 保存流程定义信息
        // 一个分类中有多个流程实例但仅有一个默认流程(由isDefault控制); 一个流程允许多次修改存在多个版本但仅有一个为使用生效状态(由isGroupUse控制)
        ProcessDefinitionPo processDefinitionPo = new ProcessDefinitionPo();
        processDefinitionPo.setCategoryId(request.getCategoryId());
        processDefinitionPo.setTitle(BpmConstant.DEFAULT_TITLE_RULE);
        processDefinitionPo.setDeploymentId(deployment.getId());
        processDefinitionPo.setProcessDefinitionId(processDefinition.getId());
        processDefinitionPo.setProcessKey(processDefinition.getKey());
        processDefinitionPo.setProcessName(processDefinition.getName());
        processDefinitionPo.setIsDefault(true);
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        long groupId = snowflake.nextId();
        processDefinitionPo.setGroupId(groupId);
        processDefinitionPo.setVersion(BpmConstant.DEFAULT_VERSION);
        processDefinitionPo.setIsGroupUse(true);
        processDefinitionPo.setTenantId(request.getTenantId());
        processDefinitionRepository.save(processDefinitionPo);
    }

    private void userNode(String processDefinitionId) {
        // 获取流程节点上的所有用户任务节点
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Process process = bpmnModel.getProcesses().get(bpmnModel.getProcesses().size()-1);
        Collection<FlowElement> flowElements = process.getFlowElements();
        final List<UserTask> userTaskList = ActivityUtil.getUserTaskList(flowElements);
        if (CollectionUtil.isEmpty(userTaskList)) {
            return;
        }
        for (UserTask userTask : userTaskList) {
            UserNodePo userNodePo = new UserNodePo();
        }
    }



}
