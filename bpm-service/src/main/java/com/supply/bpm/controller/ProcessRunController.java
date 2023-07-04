package com.supply.bpm.controller;

import com.supply.bpm.model.request.TaskHandleRequest;
import com.supply.bpm.service.IProcessRunService;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wjd
 * @description .
 * @date 2023-06-28
 */
@Api(tags = "流程运行任务")
@RestController
@RequestMapping("/run")
public class ProcessRunController {

    private final IProcessRunService processRunService;

    public ProcessRunController(IProcessRunService processRunService) {
        this.processRunService = processRunService;
    }

    @ApiOperation(value = "完成任务")
    @PostMapping("/completeTask")
    public Result<?> completeTask(@RequestBody TaskHandleRequest request) {
        final Long userId = ContextUtil.getCurrentUserId();
        request.setAssigneeId(Long.valueOf(userId));
        processRunService.completeTask(request);
        return Result.ok();
    }

    @ApiOperation(value = "流程运行图")
    @GetMapping("/getActImage")
    public void getActImage(@RequestParam String instanceId, HttpServletResponse response) {
        processRunService.getActImage(instanceId, response);
    }
}
