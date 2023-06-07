package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.constant.BpmConstant;
import com.supply.bpm.constant.UserNodeTypeEnum;
import com.supply.bpm.cvt.ProcessDefinitionCvt;
import com.supply.bpm.model.po.UserNodePo;
import com.supply.bpm.model.po.ProcessDefinitionPo;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.model.response.ProcessDefinitionResponse;
import com.supply.bpm.repository.IProcessDefinitionRepository;
import com.supply.bpm.repository.IUserNodeRepository;
import com.supply.bpm.service.IProcessDefinitionService;
import com.supply.bpm.util.ActivityUtil;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.util.CommonUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private final RuntimeService runtimeService;

    private final HistoryService historyService;

    private final IProcessDefinitionRepository processDefinitionRepository;

    private final IUserNodeRepository userNodeRepository;

    public ProcessDefinitionServiceImpl(RepositoryService repositoryService, RuntimeService runtimeService,
                                        HistoryService historyService, IProcessDefinitionRepository processDefinitionRepository,
                                        IUserNodeRepository userNodeRepository) {
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
        this.processDefinitionRepository = processDefinitionRepository;
        this.userNodeRepository = userNodeRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDeployment(ProcessDefinitionRequest request) {
        logger.info("[新增流程部署xml]---待新增的实体信息为{}", JSON.toJSONString(request));

        final String bpmnName = ActivityUtil.getBpmnNameByXml(request.getBpmnXml());
        // 保存流程部署xml信息
        final String resourceName = StrUtil.concat(true, bpmnName, ".bpmn");
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
        processDefinitionPo.setDefinitionId(processDefinition.getId());
        processDefinitionPo.setProcessKey(processDefinition.getKey());
        processDefinitionPo.setProcessName(processDefinition.getName());
        processDefinitionPo.setIsDefault(true);
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        long groupId = snowflake.nextId();
        processDefinitionPo.setGroupId(groupId);
        processDefinitionPo.setVersion(BpmConstant.DEFAULT_VERSION);
        // 判断是否存在默认流程,不存在则设置为默认流程
        processDefinitionPo.setIsGroupUse(this.isInDefaultProcess(request.getCategoryId()));
        processDefinitionPo.setBusinessStatus(BusinessStatusEnum.PROCESS_STATUS_ACTIVE.getStatus());
        processDefinitionPo.setTenantId(request.getTenantId());
        processDefinitionRepository.save(processDefinitionPo);

        // 获取用户任务节点信息并保存
        this.userNodeInfo(processDefinition.getId(), request.getTenantId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void suspendProcess(Long defId) {
        logger.info("[流程挂起]---待挂起的流程ID为{}", defId);
        // 当流程定义被挂起时,已经发起的该流程定义的流程实例不受影响
        // 当流程定义被挂起时,无法发起新的该流程定义的流程实例
        final ProcessDefinitionPo processDefinitionPo = processDefinitionRepository.getById(defId);
        if (null == processDefinitionPo) {
            logger.error("流程定义ID{}不存在", defId);
            throw new ApiException("操作失败");
        }
        if (processDefinitionPo.getStatus() == BusinessStatusEnum.PROCESS_STATUS_SUSPEND.getStatus()) {
            throw new ApiException("该流程已被挂起,请勿重复操作!");
        }
        // 修改状态为挂起状态
        ProcessDefinitionPo processDefinition = new ProcessDefinitionPo();
        processDefinition.setId(defId);
        processDefinition.setBusinessStatus(BusinessStatusEnum.PROCESS_STATUS_SUSPEND.getStatus());
        processDefinitionRepository.updateById(processDefinition);

        // 将流程表act_re_procdef的SUSPENSION_STATE_值改为2
        repositoryService.suspendProcessDefinitionById(processDefinitionPo.getDefinitionId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeProcess(Long defId) {
        logger.info("[流程激活]---待激活的流程ID为{}", defId);
        final ProcessDefinitionPo processDefinitionPo = processDefinitionRepository.getById(defId);
        if (null == processDefinitionPo) {
            logger.error("流程定义ID{}不存在", defId);
            throw new ApiException("操作失败");
        }
        if (processDefinitionPo.getStatus() == BusinessStatusEnum.PROCESS_STATUS_ACTIVE.getStatus()) {
            throw new ApiException("该流程已被激活,请勿重复操作!");
        }
        // 修改状态为激活状态
        ProcessDefinitionPo processDefinition = new ProcessDefinitionPo();
        processDefinition.setId(defId);
        processDefinition.setBusinessStatus(BusinessStatusEnum.PROCESS_STATUS_ACTIVE.getStatus());
        processDefinitionRepository.updateById(processDefinition);

        // 修改流程定义表状态
        repositoryService.activateProcessDefinitionById(processDefinitionPo.getDefinitionId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delProcess(Long defId) {
        logger.info("[流程删除]---待删除的流程ID为{}", defId);
        // 删除条件: 当前流程有且仅有一个版本且该流程未被使用
        // 查询当前流程是否存在多版本
        final ProcessDefinitionPo processDefinitionPo = processDefinitionRepository.getById(defId);
        ProcessDefinitionRequest request = new ProcessDefinitionRequest();
        request.setDefinitionId(processDefinitionPo.getDefinitionId());
        request.setGroupId(processDefinitionPo.getGroupId());
        request.setStatus(Constant.STATUS_NOT_DEL);
        final Long count = processDefinitionRepository.getCountByParams(request);
        if (count > 1) {
            throw new ApiException("该流程存在多版本,不允许删除!");
        }
        // 查询该流程是否存在
        final long processInstanceCount = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionPo.getDefinitionId()).count();
        if (processInstanceCount > 0) {
            throw new ApiException("该流程已存在流程实例,不允许删除!");
        }

        // 删除
        ProcessDefinitionPo processDefinition = new ProcessDefinitionPo();
        processDefinition.setId(defId);
        processDefinition.setStatus(Constant.STATUS_DEL);
        processDefinitionRepository.updateById(processDefinition);
    }

    /**
      * @description 根据分类ID查询是否存在默认流程.
      * @author wjd
      * @date 2023/6/7
      * @param categoryId 流程分类ID
      * @return 是否存在默认流程
      */
    private boolean isInDefaultProcess(Long categoryId) {
        ProcessDefinitionRequest request = new ProcessDefinitionRequest();
        request.setCategoryId(categoryId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setIsDefault(true);
        final Long count = processDefinitionRepository.getCountByParams(request);
        return count == 0;
    }

    /**
      * @description 根据流程定义ID获取用户节点信息并保存.
      * @author wjd
      * @date 2023/6/7
      * @param definitionId 流程定义ID
      * @param tenantId 租户ID
      */
    private void userNodeInfo(String definitionId, Long tenantId) {
        // 获取流程节点上的所有用户任务节点
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        Process process = bpmnModel.getProcesses().get(bpmnModel.getProcesses().size() - 1);
        Collection<FlowElement> flowElements = process.getFlowElements();
        final List<UserTask> userTaskList = ActivityUtil.getUserTaskList(flowElements);
        if (CollectionUtil.isEmpty(userTaskList)) {
            return;
        }
        final List<UserNodePo> userNodes = new ArrayList<>();
        int sort = 0;
        for (UserTask userTask : userTaskList) {
            sort++;
            UserNodePo userNode = new UserNodePo();
            userNode.setDefinitionId(definitionId);
            userNode.setNodeId(userTask.getId());
            userNode.setNodeName(userTask.getName());
            if (StrUtil.isNotBlank(userTask.getAssignee())) {
                userNode.setNodeType(UserNodeTypeEnum.OWNER.getType());
            }
            if (CollectionUtil.isNotEmpty(userTask.getCandidateUsers())) {
                userNode.setNodeType(UserNodeTypeEnum.CANDIDATE_USERS.getType());
            }
            if (CollectionUtil.isNotEmpty(userTask.getCandidateGroups())) {
                userNode.setNodeType(UserNodeTypeEnum.GROUP.getType());
            }
            userNode.setSort(sort);
            userNode.setTenantId(tenantId);
            userNodes.add(userNode);
        }
        userNodeRepository.saveBatch(userNodes);
    }


    @Override
    public IPage<ProcessDefinitionResponse> getProcessDefinitionPage(ProcessDefinitionRequest request) {
        Page<ProcessDefinitionPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<ProcessDefinitionPo> poPage = processDefinitionRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<ProcessDefinitionPo> poList = poPage.getRecords();
        final List<ProcessDefinitionResponse> responseList = ProcessDefinitionCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }
}
