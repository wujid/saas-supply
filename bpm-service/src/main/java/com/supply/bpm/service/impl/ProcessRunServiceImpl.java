package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.supply.bpm.constant.NodeButtonTypeEnum;
import com.supply.bpm.constant.NodeTypeEnum;
import com.supply.bpm.constant.NodeUserTypeEnum;
import com.supply.bpm.model.po.BusinessVariablePo;
import com.supply.bpm.model.po.NodeButtonPo;
import com.supply.bpm.model.po.NodeSetPo;
import com.supply.bpm.model.po.NodeUserPo;
import com.supply.bpm.model.po.ProcessDefinitionPo;
import com.supply.bpm.model.po.ProcessRunPo;
import com.supply.bpm.model.request.NodeButtonRequest;
import com.supply.bpm.model.request.NodeSetRequest;
import com.supply.bpm.model.request.ProcessRunRequest;
import com.supply.bpm.model.request.TaskHandleRequest;
import com.supply.bpm.repository.IBusinessVariableRepository;
import com.supply.bpm.repository.INodeButtonRepository;
import com.supply.bpm.repository.INodeSetRepository;
import com.supply.bpm.repository.INodeUserRepository;
import com.supply.bpm.repository.IProcessDefinitionRepository;
import com.supply.bpm.repository.IProcessRunRepository;
import com.supply.bpm.service.IProcessRunService;
import com.supply.bpm.util.ActivityUtil;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.model.Result;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.util.CommonUtil;
import com.supply.common.util.SystemUserUtil;
import com.supply.common.web.model.BpmRequestEntity;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description .
 * @date 2023-06-20
 */
@Service
public class ProcessRunServiceImpl implements IProcessRunService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessRunServiceImpl.class);

    private final IProcessRunRepository processRunRepository;

    private final IProcessDefinitionRepository processDefinitionRepository;

    private final IBusinessVariableRepository businessVariableRepository;

    private final INodeSetRepository nodeSetRepository;

    private final INodeUserRepository nodeUserRepository;

    private final INodeButtonRepository nodeButtonRepository;

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final RestTemplate restTemplate;

    private final SystemUserUtil userUtil;

    public ProcessRunServiceImpl(IProcessRunRepository processRunRepository, IProcessDefinitionRepository processDefinitionRepository,
                                 IBusinessVariableRepository businessVariableRepository, INodeSetRepository nodeSetRepository,
                                 INodeUserRepository nodeUserRepository, INodeButtonRepository nodeButtonRepository,
                                 RepositoryService repositoryService, RuntimeService runtimeService,
                                 TaskService taskService, RestTemplate restTemplate, SystemUserUtil userUtil) {
        this.processRunRepository = processRunRepository;
        this.processDefinitionRepository = processDefinitionRepository;
        this.businessVariableRepository = businessVariableRepository;
        this.nodeSetRepository = nodeSetRepository;
        this.nodeUserRepository = nodeUserRepository;
        this.nodeButtonRepository = nodeButtonRepository;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.restTemplate = restTemplate;
        this.userUtil = userUtil;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProcess(BpmRequestEntity request) {
        logger.info("[流程发起]---待发起的实体信息为{}", JSON.toJSONString(request));
        final String definitionId = request.getBpmDefinitionId();

        // 过滤流程所需业务参数
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

        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        Process process = bpmnModel.getProcesses().get(bpmnModel.getProcesses().size() - 1);
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 为发起人赋值
        final Long startUserId = request.getBpmStartUserId();
        final FlowElement startFlowElement = ActivityUtil.getStartFlowElement(flowElements);
        final StartEvent startEvent = (StartEvent) startFlowElement;
        final String startElName = StrUtil.subBetween(startEvent.getInitiator(), "{", "}");
        businessVariableMap.put(startElName, startUserId);

        // 查询下一个审批节点信息并赋值
        this.setNextNodeVariables(definitionId, flowElements, startFlowElement, businessVariableMap, request.getBpmNodeUserMap());

        // 启动流程
        final String businessId = request.getBpmBusinessId();
        final ProcessInstance processInstance = runtimeService.startProcessInstanceById(definitionId, businessId, businessVariableMap);
        final String instanceId = processInstance.getProcessInstanceId();

        // 保存流程运行信息
        ProcessRunPo processRun = new ProcessRunPo();
        processRun.setDefinitionId(definitionId);
        processRun.setInstanceId(instanceId);
        processRun.setBusinessId(businessId);
        processRun.setStartUserId(startUserId);
        processRun.setBusinessStatus(BusinessStatusEnum.PROCESS_STATUS_ACTIVE.getStatus());
        final String businessTitle = this.getBusinessTitle(definitionId, startUserId, businessVariableMap);
        processRun.setBusinessTitle(businessTitle);
        processRunRepository.save(processRun);
    }

    @Override
    public void completeTask(TaskHandleRequest request) {
        final Task task = taskService.createTaskQuery().taskId(request.getTaskId()).singleResult();
        final Integer buttonType = request.getButtonType();
        // 同意
        if (buttonType == NodeButtonTypeEnum.AGREE.getType()) {
            this.agreeTask(request, task);
            return;
        }
        // 反对
        if (buttonType == NodeButtonTypeEnum.AGAINST.getType()) {
            runtimeService.deleteProcessInstance(task.getProcessInstanceId(), null);
            return;
        }
        // 驳回到发起人
        if (buttonType == NodeButtonTypeEnum.REJECT_TO_START_USER.getType()) {
            return;
        }
    }

    private void agreeTask(TaskHandleRequest request, Task task) {
        final String taskId = request.getTaskId();
        //获取BpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        //获取Process对象
        Process process = bpmnModel.getProcesses().get(bpmnModel.getProcesses().size()-1);
        //获取所有的FlowElement信息
        Collection<FlowElement> flowElements = process.getFlowElements();
        //获取当前节点信息
        FlowElement flowElement = ActivityUtil.getFlowElementById(task.getTaskDefinitionKey(), flowElements);
        // 流程参数信息
        final Map<String, Object> variablesMap = runtimeService.getVariables(task.getProcessInstanceId());
        // 下一步审批人赋值
        this.setNextNodeVariables(task.getProcessDefinitionId(), flowElements, flowElement, variablesMap, request.getNodeUserMap());
        // 完成当前任务
        taskService.claim(taskId, request.getAssigneeId().toString());
        taskService.complete(taskId);

        // 执行当前审批节点对应的脚本任务
        this.executeScript(task.getProcessInstanceId(), request.getNodeSetId(), request.getButtonType());

        // 判断当前流程实例是否完成,如果完成则执行对应的脚本任务
        // 查询运行中的流程实例信息
        final long count = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).count();
        if (count == 0L) {

        }
    }

    private void executeScript(String instanceId, Long nodeSetId, Integer buttonType) {
        // 根据流程运行实例ID获取关联的业务ID
        ProcessRunRequest request = new ProcessRunRequest();
        request.setInstanceId(instanceId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final ProcessRunPo processRun = processRunRepository.getByParams(request);
        final String businessId = processRun.getBusinessId();

        // 获取流程在该审批节点下的脚本
        NodeButtonRequest nodeButtonRequest = new NodeButtonRequest();
        nodeButtonRequest.setNodeSetId(nodeSetId);
        nodeButtonRequest.setButtonType(buttonType);
        nodeButtonRequest.setStatus(Constant.STATUS_NOT_DEL);
        final NodeButtonPo nodeButton = nodeButtonRepository.getByParams(nodeButtonRequest);
        final String script = nodeButton.getScript();
        // 执行脚本
        if (StrUtil.isNotBlank(script)) {
            final Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("businessId", businessId);
            paramsMap.put("operateType", buttonType);
            final String finalUrl = CommonUtil.getContentByRule(script, paramsMap);
            restTemplate.getForEntity(finalUrl, Result.class);
        }
    }

    /**
      * @description 为流程下一个节点审批人赋值.
      * @author wjd
      * @date 2023/6/21
      */
    private void setNextNodeVariables(String definitionId, Collection<FlowElement> flowElements, FlowElement flowElement, Map<String, Object> businessVariableMap, Map<Long, Long> nodeUserMap) {
        final Map<String, UserTask> userTaskMap = ActivityUtil.getNextNodeMap(flowElements, flowElement, businessVariableMap);
        if (CollectionUtil.isEmpty(userTaskMap)) {
            return;
        }
        final Set<String> nodeIds = userTaskMap.keySet();
        NodeSetRequest nodeSetRequest = new NodeSetRequest();
        nodeSetRequest.setDefinitionId(definitionId);
        nodeSetRequest.setStatus(Constant.STATUS_NOT_DEL);
        nodeSetRequest.setNodeIds(nodeIds);
        final List<NodeSetPo> nodeSets = nodeSetRepository.getListByParams(nodeSetRequest);
        for (NodeSetPo nodeSet : nodeSets) {
            final Long nodeSetId = nodeSet.getId();
            final Integer nodeType = nodeSet.getNodeType();
            // 个人任务赋值发起人选择的人员
            if (nodeType == NodeTypeEnum.USER_OWNER.getType()) {
                if (!nodeUserMap.containsKey(nodeSetId)) {
                    logger.error("节点ID{}未选择审批人", nodeSetId);
                    final String errorMessage = StrUtil.format("{}未选择审批人", nodeSet.getNodeName());
                    throw new ApiException(errorMessage);
                }
                final Long userId = nodeUserMap.get(nodeSetId);
                businessVariableMap.put(nodeSet.getNodeElName(), userId);
            }
            // 候选人任务赋值所有可选人员
            if (nodeType == NodeTypeEnum.USER_CANDIDATE_USERS.getType()) {
                final String userIds = this.getCandidateUserIdsByNodeSetId(nodeSetId, nodeSet.getNodeName());
                businessVariableMap.put(nodeSet.getNodeElName(), userIds);
            }
            // 候选组任务赋值
            if (nodeType == NodeTypeEnum.USER_GROUP.getType()) {
                final String groupIds = this.getUserGroupIdsByNodeSetId(nodeSetId, nodeSet.getNodeName());
                businessVariableMap.put(nodeSet.getNodeElName(), groupIds);
            }
        }
    }

    /**
      * @description 获取候选人节点下的所有人员ID.
      * @author wjd
      * @date 2023/6/21
      * @param nodeSetId 节点ID
      * @param nodeName 节点名称
      * @return 人员ID
      */
    private String getCandidateUserIdsByNodeSetId(Long nodeSetId, String nodeName) {
        final List<NodeUserPo> nodeUsers = nodeUserRepository.getListByNodeSetId(nodeSetId);
        if (CollectionUtil.isEmpty(nodeUsers)) {
            logger.error("节点ID{}未设置候选人", nodeSetId);
            final String errorMessage = StrUtil.format("{}未设置候选人,请先设置候选人", nodeName);
            throw new ApiException(errorMessage);
        }
        final Set<Long> userIds = nodeUsers.stream().
                filter(nodeUser -> nodeUser.getNodeUserType() == NodeUserTypeEnum.USER.getType())
                .map(NodeUserPo::getRelationId).collect(Collectors.toSet());
        return StrUtil.join(",", userIds);
    }

    /**
     * @description 获取用户组节点下的所有人员ID.
     * @author wjd
     * @date 2023/6/21
     * @param nodeSetId 节点ID
     * @param nodeName 节点名称
     * @return 人员ID
     */
    private String getUserGroupIdsByNodeSetId(Long nodeSetId, String nodeName) {
        final List<NodeUserPo> nodeUsers = nodeUserRepository.getListByNodeSetId(nodeSetId);
        if (CollectionUtil.isEmpty(nodeUsers)) {
            logger.error("节点ID{}未设置候选组", nodeSetId);
            final String errorMessage = StrUtil.format("{}未设置候选组,请先设置候选组", nodeName);
            throw new ApiException(errorMessage);
        }
        final Set<String> groupIds = new HashSet<>();
        for (NodeUserPo nodeUser : nodeUsers) {
            if (nodeUser.getNodeUserType() == NodeUserTypeEnum.ROLE.getType()) {
                final String groupId = StrUtil.concat(true, "role:", nodeUser.getRelationId().toString());
                groupIds.add(groupId);
            }
            if (nodeUser.getNodeUserType() == NodeUserTypeEnum.DEPT.getType()) {
                final String groupId = StrUtil.concat(true, "dept:", nodeUser.getRelationId().toString());
                groupIds.add(groupId);
            }
        }
        return StrUtil.join(",", groupIds);
    }

    /**
     * @description 获取流程标题.
     * @author wjd
     * @date 2023/6/21
     * @param definitionId 流程定义ID
     * @param startUserId 发起人ID
     * @param businessVariableMap 流程所需业务参数
     * @return 流程标题
     */
    private String getBusinessTitle(String definitionId, Long startUserId,  Map<String, Object> businessVariableMap) {
        // 组装标题 + 发起人 + 发起时间
        final ProcessDefinitionPo definition = processDefinitionRepository.getByDefinitionId(definitionId);
        businessVariableMap.put("title", definition.getProcessName());

        final SysUserResponse user = userUtil.getUserById(startUserId);
        if (null != user) {
            businessVariableMap.put("startUserName", user.getName());
        }
        businessVariableMap.put("startTime", DateUtil.now());
        return CommonUtil.getContentByRule(definition.getTitle(), businessVariableMap);
    }
}
