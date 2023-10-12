package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.model.po.NodeSetPo;
import com.supply.bpm.model.request.NodeSetRequest;
import com.supply.bpm.model.request.TaskRequest;
import com.supply.bpm.model.response.TaskResponse;
import com.supply.bpm.repository.INodeSetRepository;
import com.supply.bpm.repository.IProcessRunRepository;
import com.supply.bpm.service.ITaskManageService;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.util.SystemUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description 流程任务管理.
 * @date 2023-06-19
 */
@Service
public class TaskManageServiceImpl implements ITaskManageService {
    private static final Logger logger = LoggerFactory.getLogger(TaskManageServiceImpl.class);

    private final IProcessRunRepository processRunRepository;

    private final INodeSetRepository nodeSetRepository;

    private final SystemUserUtil systemUserUtil;

    public TaskManageServiceImpl(IProcessRunRepository processRunRepository, INodeSetRepository nodeSetRepository,
                                 SystemUserUtil systemUserUtil) {
        this.processRunRepository = processRunRepository;
        this.nodeSetRepository = nodeSetRepository;
        this.systemUserUtil = systemUserUtil;
    }


    @Override
    public IPage<TaskResponse> getMyTask(TaskRequest request) {
        // 如果审批人不为空,查询当前审批人所属角色
        final String assigneeUserId = request.getAssigneeUserId();
        if (StrUtil.isNotBlank(assigneeUserId)) {
            final Set<Long> roleIds = systemUserUtil.getRoleIdsByUserId(Long.valueOf(assigneeUserId));
            if (CollectionUtil.isNotEmpty(roleIds)) {
                final Set<String> assigneeGroups = roleIds.stream()
                        .map(roleId -> StrUtil.concat(true, "role:", roleId.toString()))
                        .collect(Collectors.toSet());
                request.setAssigneeGroups(assigneeGroups);
            }
        }
        final Page<TaskResponse> page = processRunRepository.getMyTask(request);
        if (page.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<TaskResponse> list = page.getRecords();
        this.getExtData(list);
        return page;
    }

    @Override
    public IPage<TaskResponse> getMyStart(TaskRequest request) {
        final Page<TaskResponse> page = processRunRepository.getMyStart(request);
        if (page.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<TaskResponse> list = page.getRecords();
        this.getExtData(list);
        return page;
    }

    @Override
    public IPage<TaskResponse> getMyAttend(TaskRequest request) {
        final Page<TaskResponse> page = processRunRepository.getMyAttend(request);
        if (page.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<TaskResponse> list = page.getRecords();
        this.getExtData(list);
        return page;
    }

    @Override
    public IPage<TaskResponse> getBpmHistory(TaskRequest request) {
        final Page<TaskResponse> page = processRunRepository.getBpmHistory(request);
        if (page.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<TaskResponse> list = page.getRecords();
        this.getExtData(list);
        return page;
    }

    /**
     * @description 任务管理拓展信息.
     * @author wjd
     * @date 2023/6/18
     */
    private void getExtData(List<TaskResponse> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        Map<Long, String> startUserMap = new HashMap<>();
        final Set<Long> startUserIds = list.stream().map(TaskResponse::getStartUserId).collect(Collectors.toSet());
        final List<SysUserResponse> startUsers = systemUserUtil.getUsersByIds(startUserIds);
        if (CollectionUtil.isNotEmpty(startUsers)) {
            startUserMap = startUsers.stream().collect(Collectors.toMap(SysUserResponse::getId, SysUserResponse::getName, (k1, k2) -> k1));
        }
        for(TaskResponse task : list) {
            final Long startUserId = task.getStartUserId();
            if (startUserMap.containsKey(startUserId)) {
                final String startUserName = startUserMap.get(startUserId);
                task.setStartUserName(startUserName);
            }
            if (StrUtil.isNotBlank(task.getNodeId())) {
                NodeSetRequest nodeSetRequest = new NodeSetRequest();
                nodeSetRequest.setDefinitionId(task.getDefinitionId());
                nodeSetRequest.setNodeId(task.getNodeId());
                nodeSetRequest.setStatus(Constant.STATUS_NOT_DEL);
                final NodeSetPo nodeSetPo = nodeSetRepository.getByParams(nodeSetRequest);
                if (null == nodeSetPo) {
                    logger.error("根据流程定义ID{}和节点ID{}未查询到流程节点设置信息", task.getDefinitionId(), task.getNodeId());
                    throw new ApiException();
                }
                task.setNodeSetId(nodeSetPo.getId());
            }
        }
    }
}
