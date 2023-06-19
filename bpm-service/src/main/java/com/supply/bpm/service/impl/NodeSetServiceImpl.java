package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.constant.NodeUserTypeEnum;
import com.supply.bpm.cvt.NodeSetCvt;
import com.supply.bpm.model.po.NodeSetPo;
import com.supply.bpm.model.po.NodeUserPo;
import com.supply.bpm.model.request.NodeSetRequest;
import com.supply.bpm.model.request.NodeUserRequest;
import com.supply.bpm.model.response.NodeSetResponse;
import com.supply.bpm.repository.INodeSetRepository;
import com.supply.bpm.repository.INodeUserRepository;
import com.supply.bpm.service.INodeSetService;
import com.supply.bpm.util.ActivityUtil;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.util.CommonUtil;
import com.supply.common.util.SystemUserUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description .
 * @date 2023-06-15
 */
@Service
public class NodeSetServiceImpl implements INodeSetService {
    private static final Logger logger = LoggerFactory.getLogger(NodeSetServiceImpl.class);

    private final INodeSetRepository nodeSetRepository;

    private final RepositoryService repositoryService;

    private final INodeUserRepository nodeUserRepository;

    private final SystemUserUtil userUtil;

    public NodeSetServiceImpl(INodeSetRepository nodeSetRepository, RepositoryService repositoryService,
                              INodeUserRepository nodeUserRepository, SystemUserUtil userUtil) {
        this.nodeSetRepository = nodeSetRepository;
        this.repositoryService = repositoryService;
        this.nodeUserRepository = nodeUserRepository;
        this.userUtil = userUtil;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNodeSetFormUrl(Long id, String formUrl) {
        logger.info("[根据用户节点ID修改详情表单url]---待修改的用户节点id{}对应的表单url为{}", id, formUrl);
        NodeSetPo nodeSetPo = new NodeSetPo();
        nodeSetPo.setId(id);
        nodeSetPo.setFormUrl(formUrl);
        nodeSetRepository.updateById(nodeSetPo);
    }

    @Override
    public IPage<NodeSetResponse> getNodeSetPage(NodeSetRequest request) {
        Page<NodeSetPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<NodeSetPo> poPage = nodeSetRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<NodeSetPo> poList = poPage.getRecords();
        final List<NodeSetResponse> responseList = NodeSetCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    @Override
    public List<NodeSetResponse> startBpmNextNode(NodeSetRequest request) {
        final String definitionId = request.getDefinitionId();
        final Map<String, Object> variablesMap = request.getVariablesMap();
        // 获取BpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        // 获取Process对象
        Process process = bpmnModel.getProcesses().get(bpmnModel.getProcesses().size() - 1);
        //获取所有的FlowElement信息
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 获取起始节点信息
        final FlowElement startFlowElement = ActivityUtil.getStartFlowElement(flowElements);
        if (null == startFlowElement) {
            throw new ApiException("流程起始节点未定义,请检查流程图!");
        }
        List<UserTask> userTasks = new ArrayList<>();
        ActivityUtil.getNextNode(flowElements, startFlowElement, variablesMap, userTasks);
        if (CollectionUtil.isEmpty(userTasks)) {
            throw new ApiException("未能获取到下一节点信息");
        }
        final Map<String, UserTask> userTaskMap = userTasks.stream().collect(Collectors.toMap(UserTask::getId, e -> e, (k1, k2) -> k1));

        // 根据流程定义获取已保存的流程节点信息
        NodeSetRequest nodeSetRequest = new NodeSetRequest();
        nodeSetRequest.setDefinitionId(definitionId);
        nodeSetRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<NodeSetPo> nodeSets = nodeSetRepository.getListByParams(nodeSetRequest);
        if (CollectionUtil.isEmpty(nodeSets)) {
            return null;
        }

        // 获取下一个节点审批人信息
        final List<NodeSetPo> nexNodes = nodeSets.stream().filter(nodeSet -> userTaskMap.containsKey(nodeSet.getNodeId())).collect(Collectors.toList());
        for (NodeSetPo nodeSet : nexNodes) {
            final List<SysUserResponse> users = this.getNodeUsers(nodeSet);
            nodeSet.setNodeUsers(users);
        }
        return NodeSetCvt.INSTANCE.poToResponseBatch(nexNodes);
    }

    /**
      * @description 获取节点下的所有审批人信息.
      * @author wjd
      * @date 2023/6/19
      * @param nodeSet 节点信息
      * @return 节点下的所有审批人信息
      */
    private List<SysUserResponse> getNodeUsers(NodeSetPo nodeSet) {
        // 根据节点ID查询关联人员信息
        final Long nodeSetId = nodeSet.getId();
        NodeUserRequest request = new NodeUserRequest();
        request.setNodeSetId(nodeSetId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<NodeUserPo> nodeUsers = nodeUserRepository.getListByParams(request);
        if (CollectionUtil.isEmpty(nodeUsers)) {
            return null;
        }

        List<SysUserResponse> nodeUserList = new ArrayList<>();
        // 获取不同类型下的人员信息
        final Map<Integer, Set<Long>> nodeUserTypeGroup = nodeUsers.stream().collect(Collectors.groupingBy(NodeUserPo::getNodeUserType,
                Collectors.mapping(NodeUserPo::getRelationId, Collectors.toSet())));
        for (Integer nodeUserType : nodeUserTypeGroup.keySet()) {
            // 个人类型
            if (nodeUserType == NodeUserTypeEnum.USER.getType()) {
                final Set<Long> userIds = nodeUserTypeGroup.get(nodeUserType);
                List<SysUserResponse> users = userUtil.getUsersByIds(userIds);
                if (CollectionUtil.isEmpty(users)) {
                    continue;
                }
                users = users.stream().filter(user -> user.getBusinessStatus() == BusinessStatusEnum.IN_ACTIVE.getStatus()).collect(Collectors.toList());
                nodeUserList.addAll(users);
            }
        }

        // 人员去重
        nodeUserList = nodeUserList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                -> new TreeSet<>(Comparator.comparing(SysUserResponse::getId))), ArrayList::new));
        return nodeUserList;
    }
}
