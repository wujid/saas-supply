package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.supply.bpm.model.po.BusinessVariablePo;
import com.supply.bpm.repository.IBusinessVariableRepository;
import com.supply.bpm.repository.IProcessRunRepository;
import com.supply.bpm.service.IProcessRunService;
import com.supply.bpm.util.ActivityUtil;
import com.supply.common.util.SystemUserUtil;
import com.supply.common.web.model.BpmRequestEntity;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wjd
 * @description .
 * @date 2023-06-20
 */
@Service
public class ProcessRunServiceImpl implements IProcessRunService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessRunServiceImpl.class);

    private final IProcessRunRepository processRunRepository;

    private final IBusinessVariableRepository businessVariableRepository;

    private final RepositoryService repositoryService;

    private final SystemUserUtil userUtil;


    public ProcessRunServiceImpl(IProcessRunRepository processRunRepository, IBusinessVariableRepository businessVariableRepository,
                                 RepositoryService repositoryService, SystemUserUtil userUtil) {
        this.processRunRepository = processRunRepository;
        this.businessVariableRepository = businessVariableRepository;
        this.repositoryService = repositoryService;
        this.userUtil = userUtil;
    }


    public void startProcess(BpmRequestEntity request) {
        logger.info("[流程发起]---待发起的实体信息为{}", JSON.toJSONString(request));
        final String definitionId = request.getBpmDefinitionId();

        // 组装流程所需业务参数
        final List<BusinessVariablePo> businessVariables = businessVariableRepository.getListByDefinitionId(definitionId);
        Map<String, Object> businessVariableMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(businessVariables)) {
            final Map<String, Object> bpmBusinessVariableMap = request.getBpmBusinessVariableMap();
            for (BusinessVariablePo businessVariable : businessVariables) {
                if (bpmBusinessVariableMap.containsKey(businessVariable.getVariableKey())) {
                    final Object variableValue = bpmBusinessVariableMap.get(businessVariable.getVariableKey());
                    businessVariableMap.put(businessVariable.getVariableKey(), variableValue);
                }
            }
        }
        // 获取下一步审批人信息
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        Process process = bpmnModel.getProcesses().get(bpmnModel.getProcesses().size() - 1);
        Collection<FlowElement> flowElements = process.getFlowElements();
        final FlowElement startFlowElement = ActivityUtil.getStartFlowElement(flowElements);
        final StartEvent startEvent = (StartEvent) startFlowElement;
    }
}
