package com.supply.bpm.controller;

import cn.hutool.core.util.StrUtil;
import com.supply.bpm.model.request.TaskHandleRequest;
import com.supply.bpm.service.IProcessRunService;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        final String userId = ContextUtil.getCurrentUserId();
        if (StrUtil.isBlank(userId)) {
            return Result.error("未获取到当前人信息");
        }
        request.setAssigneeId(Long.valueOf(userId));
        processRunService.completeTask(request);
        return Result.ok();
    }
}
