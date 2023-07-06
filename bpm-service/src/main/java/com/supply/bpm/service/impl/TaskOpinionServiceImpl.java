package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.supply.bpm.constant.CheckStatusEnum;
import com.supply.bpm.cvt.TaskOpinionCvt;
import com.supply.bpm.image.CustomProcessDiagramGenerator;
import com.supply.bpm.model.po.ProcessRunPo;
import com.supply.bpm.model.po.TaskOpinionPo;
import com.supply.bpm.model.request.TaskOpinionRequest;
import com.supply.bpm.model.response.TaskOpinionResponse;
import com.supply.bpm.repository.IProcessRunRepository;
import com.supply.bpm.repository.ITaskOpinionRepository;
import com.supply.bpm.service.ITaskOpinionService;
import com.supply.common.exception.ApiException;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.util.SystemUserUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: .
 * @author: wjd
 * @date 2023-07-04
 */
@Service
public class TaskOpinionServiceImpl implements ITaskOpinionService {
    private static final Logger logger = LoggerFactory.getLogger(TaskOpinionServiceImpl.class);

    private final ITaskOpinionRepository taskOpinionRepository;

    private final IProcessRunRepository processRunRepository;

    private final SystemUserUtil userUtil;

    private final RuntimeService runtimeService;

    private final HistoryService historyService;

    private final RepositoryService repositoryService;

    public TaskOpinionServiceImpl(ITaskOpinionRepository taskOpinionRepository, IProcessRunRepository processRunRepository,
                                  SystemUserUtil userUtil, RuntimeService runtimeService, HistoryService historyService,
                                  RepositoryService repositoryService) {
        this.taskOpinionRepository = taskOpinionRepository;
        this.processRunRepository = processRunRepository;
        this.userUtil = userUtil;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
        this.repositoryService = repositoryService;
    }

    @Override
    public List<TaskOpinionResponse> getTaskOpinions(String instanceId, String businessId) {
        if (StrUtil.isBlank(instanceId) && StrUtil.isBlank(businessId)) {
            logger.error("根据流程运行实例ID或业务ID查询审批意见未传入任何参数");
            throw new ApiException();
        }
        // 如果未传入运行实例ID则根据业务ID查询出对应的运行实例ID
        if (StrUtil.isBlank(instanceId)) {
            final ProcessRunPo processRun = processRunRepository.getByBusinessId(businessId);
            if (null == processRun) {
                logger.error("根据业务ID{}未查询到流程运行信息", businessId);
                throw new ApiException();
            }
            instanceId = processRun.getInstanceId();
        }
        TaskOpinionRequest request = new TaskOpinionRequest();
        request.setInstanceId(instanceId);
        request.setOrderColumn(TaskOpinionPo::getId);
        request.setIsAsc(true);
        final List<TaskOpinionPo> list = taskOpinionRepository.getListByParams(request);
        final List<TaskOpinionResponse> responses = TaskOpinionCvt.INSTANCE.poToResponseBatch(list);
        this.extData(responses);
        return responses;
    }

    @Override
    public void getActImage(String instanceId, String businessId, HttpServletResponse response) {
        if (StrUtil.isBlank(instanceId) && StrUtil.isBlank(businessId)) {
            logger.error("根据流程运行实例ID或业务ID获取审批图未传入任何参数");
            throw new ApiException();
        }
        // 如果未传入运行实例ID则根据业务ID查询出对应的运行实例ID
        if (StrUtil.isBlank(instanceId)) {
            final ProcessRunPo processRun = processRunRepository.getByBusinessId(businessId);
            if (null == processRun) {
                logger.error("根据业务ID{}未查询到流程运行信息", businessId);
                throw new ApiException();
            }
            instanceId = processRun.getInstanceId();
        }
        // 获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
        // 获取流程中已经执行的节点，按照执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstanceList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .orderByHistoricActivityInstanceId()
                .asc().list();
        // 将已经执行的节点放入高亮显示节点集合
        List<String> highLightedActivityIdList = historicActivityInstanceList.stream()
                .map(HistoricActivityInstance::getActivityId).collect(Collectors.toList());

        // 通过流程实例ID获取流程中正在执行的节点
        List<Execution> runningActivityInstanceList = runtimeService.createExecutionQuery()
                .processInstanceId(instanceId).list();
        List<String> runningActivityIdList = runningActivityInstanceList.stream().map(Execution::getActivityId)
                .filter(StrUtil::isNotBlank).collect(Collectors.toList());


        // 定义流程画布生成器
        CustomProcessDiagramGenerator processDiagramGenerator = new CustomProcessDiagramGenerator();

        // 获取流程定义Model对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

        // 获取已经流经的流程线，需要高亮显示流程已经发生流转的线id集合
        List<String> highLightedFlowsIds = this.getHighLightedFlowsByIncomingFlows(bpmnModel, historicActivityInstanceList);

        // 根据runningActivityIdList获取runningActivityFlowsIds
        List<String> runningActivityFlowsIds = this.getRunningActivityFlowsIds(bpmnModel, runningActivityIdList, historicActivityInstanceList);
        try (InputStream inputStream = processDiagramGenerator.generateDiagramCustom(bpmnModel, highLightedActivityIdList, runningActivityIdList,
                highLightedFlowsIds, runningActivityFlowsIds, "原版宋体", "原版宋体", "原版宋体");
             ServletOutputStream outputStream = response.getOutputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            response.setContentType("image/svg+xml");
            response.setStatus(203);
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void extData(List<TaskOpinionResponse> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        // 获取人员对应的名称信息
        final Set<Long> userIds = new HashSet<>();
        list.forEach(opinion -> {
            if (null != opinion.getAssigneeUserId()) {
                userIds.add(opinion.getAssigneeUserId());
            }
            if (null != opinion.getOwnerUserId()) {
                userIds.add(opinion.getOwnerUserId());
            }
        });
        Map<Long, String> userMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(userIds)) {
            final List<SysUserResponse> users = userUtil.getUsersByIds(userIds);
            userMap = users.stream().collect(Collectors.toMap(SysUserResponse::getId, SysUserResponse::getName, (k1, k2) -> k1));
        }

        final Map<Integer, String> checkStatusMap = CheckStatusEnum.getEnumMap();

        for(TaskOpinionResponse taskOpinion : list) {
            // 审批人
            final Long assigneeUserId = taskOpinion.getAssigneeUserId();
            if (null != assigneeUserId && userMap.containsKey(assigneeUserId)) {
                final String userName = userMap.get(assigneeUserId);
                taskOpinion.setAssigneeUserName(userName);
            }
            // 转交人
            final Long ownerUserId = taskOpinion.getOwnerUserId();
            if (null != ownerUserId && userMap.containsKey(ownerUserId)) {
                final String userName = userMap.get(ownerUserId);
                taskOpinion.setOwnerUserName(userName);
            }
            // 审批状态
            final Integer checkStatus = taskOpinion.getCheckStatus();
            if (null != checkStatus && checkStatusMap.containsKey(checkStatus)) {
                final String checkStatusName = checkStatusMap.get(checkStatus);
                taskOpinion.setCheckStatusName(checkStatusName);
                // 交办
                if (checkStatus == CheckStatusEnum.STATUS_ASSIGN.getStatus() && StrUtil.isNotBlank(taskOpinion.getOwnerUserName())) {
                    final String name = StrUtil.format(CheckStatusEnum.STATUS_ASSIGN.getName() + "-{}", taskOpinion.getOwnerUserName());
                    taskOpinion.setCheckStatusName(name);
                }
            }
        }
    }

    private List<String> getHighLightedFlowsByIncomingFlows(BpmnModel bpmnModel,
                                                            List<HistoricActivityInstance> historicActivityInstanceList) {
        //historicActivityInstanceList 是 流程中已经执行的历史活动实例
        // 已经流经的顺序流，需要高亮显示
        List<String> highFlows = new ArrayList<>();

        // 全部活动节点(包括正在执行的和未执行的)
        List<FlowNode> allHistoricActivityNodeList = new ArrayList<>();

        /*
         * 循环的目的：
         *           获取所有的历史节点FlowNode并放入allHistoricActivityNodeList
         *           获取所有确定结束了的历史节点finishedActivityInstancesList
         */
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            // 获取流程节点
            // bpmnModel.getMainProcess()获取一个Process对象
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            allHistoricActivityNodeList.add(flowNode);
        }
        // 循环活动节点
        for (FlowNode flowNode : allHistoricActivityNodeList) {
            // 获取每个活动节点的输入线
            List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();

            // 循环输入线，如果输入线的源头处于全部活动节点中，则将其包含在内
            for (SequenceFlow sequenceFlow : incomingFlows) {
                if (allHistoricActivityNodeList.stream().map(BaseElement::getId).collect(Collectors.toList()).contains(sequenceFlow.getSourceFlowElement().getId())) {
                    highFlows.add(sequenceFlow.getId());
                }
            }
        }
        return highFlows;
    }


    private List<String> getRunningActivityFlowsIds(BpmnModel bpmnModel, List<String> runningActivityIdList, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> runningActivityFlowsIds = new ArrayList<>();
        List<String> runningActivityIds = new ArrayList<>(runningActivityIdList);
        // 逆序寻找，因为historicActivityInstanceList有序
        if (CollectionUtils.isEmpty(runningActivityIds)) {
            return runningActivityFlowsIds;
        }
        for (int i = historicActivityInstanceList.size() - 1; i >= 0; i--) {
            HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            // 如果当前节点是未完成的节点
            if (runningActivityIds.contains(flowNode.getId())) {
                continue;
            }
            // 当前节点的所有流出线
            List<SequenceFlow> outgoingFlowList = flowNode.getOutgoingFlows();
            // 遍历所有的流出线
            for (SequenceFlow outgoingFlow : outgoingFlowList) {
                // 获取当前节点流程线对应的下一级节点
                FlowNode targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef(), true);
                // 如果找到流出线的目标是runningActivityIdList中的，那么添加后将其移除，避免找到重复的都指向runningActivityIdList的流出线
                if (runningActivityIds.contains(targetFlowNode.getId())) {
                    runningActivityFlowsIds.add(outgoingFlow.getId());
                    runningActivityIds.remove(targetFlowNode.getId());
                }
            }

        }
        return runningActivityFlowsIds;
    }
}
