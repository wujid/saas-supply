package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.model.request.TaskRequest;
import com.supply.bpm.model.response.TaskResponse;
import com.supply.bpm.repository.IProcessRunRepository;
import com.supply.bpm.service.ITaskManageService;
import com.supply.common.util.SystemUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    private final SystemUserUtil systemUserUtil;

    public TaskManageServiceImpl(IProcessRunRepository processRunRepository, SystemUserUtil systemUserUtil) {
        this.processRunRepository = processRunRepository;
        this.systemUserUtil = systemUserUtil;
    }

    @Override
    public IPage<TaskResponse> getMyTask(TaskRequest request) {
        // 如果审批人不为空,查询当前审批人所属角色
        final String assigneeId = request.getAssigneeId();
        if (StrUtil.isNotBlank(assigneeId)) {
            final Set<Long> roleIds = systemUserUtil.getRoleIdsByUserId(Long.valueOf(assigneeId));
            if (CollectionUtil.isNotEmpty(roleIds)) {
                final Set<String> assigneeGroups = roleIds.stream()
                        .map(roleId -> StrUtil.concat(true, "role:", roleId.toString()))
                        .collect(Collectors.toSet());
                request.setAssigneeGroups(assigneeGroups);
            }
        }
        final Page<TaskResponse> taskPage = processRunRepository.getMyTask(request);
        if (taskPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        return taskPage;
    }
}
