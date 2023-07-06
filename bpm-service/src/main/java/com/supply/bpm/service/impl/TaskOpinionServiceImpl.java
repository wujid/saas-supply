package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.supply.bpm.constant.CheckStatusEnum;
import com.supply.bpm.cvt.TaskOpinionCvt;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public TaskOpinionServiceImpl(ITaskOpinionRepository taskOpinionRepository, IProcessRunRepository processRunRepository,
                                  SystemUserUtil userUtil) {
        this.taskOpinionRepository = taskOpinionRepository;
        this.processRunRepository = processRunRepository;
        this.userUtil = userUtil;
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
                    final String name = StrUtil.format("{}-" + CheckStatusEnum.STATUS_ASSIGN.getName(), taskOpinion.getOwnerUserName());
                    taskOpinion.setCheckStatusName(name);
                }
            }
        }

    }
}
