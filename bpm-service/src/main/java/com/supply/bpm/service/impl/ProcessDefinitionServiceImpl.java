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
import com.supply.common.util.CommonUtil;
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

    private final IProcessDefinitionRepository processDefinitionRepository;

    private final IUserNodeRepository userNodeRepository;

    public ProcessDefinitionServiceImpl(RepositoryService repositoryService, IProcessDefinitionRepository processDefinitionRepository,
                                        IUserNodeRepository userNodeRepository) {
        this.repositoryService = repositoryService;
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
        processDefinitionPo.setIsGroupUse(true);
        processDefinitionPo.setBusinessStatus(BusinessStatusEnum.PROCESS_STATUS_ACTIVE.getStatus());
        processDefinitionPo.setTenantId(request.getTenantId());
        processDefinitionRepository.save(processDefinitionPo);

        // 获取用户任务节点信息并保存
        this.userNodeInfo(processDefinition.getId(), request.getTenantId());
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
