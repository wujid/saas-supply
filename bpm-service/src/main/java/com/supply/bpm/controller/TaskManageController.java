package com.supply.bpm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.TaskRequest;
import com.supply.bpm.model.response.TaskResponse;
import com.supply.bpm.service.ITaskManageService;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description 流程任务管理.
 * @date 2023-06-25
 */
@Api(tags = "流程任务管理")
@RestController
@RequestMapping("/task")
public class TaskManageController {

    private final ITaskManageService taskManageService;

    public TaskManageController(ITaskManageService taskManageService) {
        this.taskManageService = taskManageService;
    }

    @ApiOperation(value = "我的待办")
    @GetMapping("/getMyTask")
    public Result<?> getMyTask(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                               @RequestParam(required = false) String processName, @RequestParam(required = false) String businessTitle,
                               @RequestParam(required = false) Long startUserId, @RequestParam(required = false) Long categoryId) {
        TaskRequest request = new TaskRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setProcessName(processName);
        request.setBusinessTitle(businessTitle);
        request.setStartUserId(startUserId);
        request.setCategoryId(categoryId);
        final String userId = ContextUtil.getCurrentUserIdStr();
        request.setAssigneeUserId(userId);
        final IPage<TaskResponse> data = taskManageService.getMyTask(request);
        return Result.ok(data);
    }

    @ApiOperation(value = "我发起的流程")
    @GetMapping("/getMyStart")
    public Result<?> getMyStart(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                @RequestParam(required = false) String processName, @RequestParam(required = false) String businessTitle,
                                @RequestParam(required = false) Long categoryId) {
        TaskRequest request = new TaskRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setProcessName(processName);
        request.setBusinessTitle(businessTitle);
        request.setCategoryId(categoryId);
        final Long userId = ContextUtil.getCurrentUserId();
        request.setStartUserId(userId);
        final IPage<TaskResponse> data = taskManageService.getMyStart(request);
        return Result.ok(data);
    }

    @ApiOperation(value = "我参与的流程")
    @GetMapping("/getMyAttend")
    public Result<?> getMyAttend(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                @RequestParam(required = false) String processName, @RequestParam(required = false) String businessTitle,
                                @RequestParam(required = false) Long categoryId) {
        TaskRequest request = new TaskRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setProcessName(processName);
        request.setBusinessTitle(businessTitle);
        request.setCategoryId(categoryId);
        final String userId = ContextUtil.getCurrentUserIdStr();
        request.setAssigneeUserId(userId);
        final IPage<TaskResponse> data = taskManageService.getMyAttend(request);
        return Result.ok(data);
    }
}
