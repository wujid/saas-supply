package com.supply.bpm.controller;

import com.supply.bpm.model.po.TaskOpinionPo;
import com.supply.bpm.model.request.TaskOpinionRequest;
import com.supply.bpm.model.response.TaskOpinionResponse;
import com.supply.bpm.service.ITaskOpinionService;
import com.supply.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wjd
 * @description 流程审批意见.
 * @date 2023-07-05
 */
@Api(tags = "流程审批意见")
@RestController
@RequestMapping("/taskOpinion")
public class TaskOpinionController {

    private final ITaskOpinionService taskOpinionService;

    public TaskOpinionController(ITaskOpinionService taskOpinionService) {
        this.taskOpinionService = taskOpinionService;
    }

    @ApiOperation(value = "根据运行实例ID查询审批意见信息集")
    @GetMapping("/getTaskOpinionsByInstanceId")
    public Result<?> getTaskOpinionsByInstanceId(@RequestParam String instanceId) {
        TaskOpinionRequest request = new TaskOpinionRequest();
        request.setInstanceId(instanceId);
        request.setOrderColumn(TaskOpinionPo::getId);
        request.setIsAsc(true);
        final List<TaskOpinionResponse> data = taskOpinionService.getByParams(request);
        return Result.ok(data);
    }
}
