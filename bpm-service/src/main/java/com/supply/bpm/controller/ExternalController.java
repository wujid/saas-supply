package com.supply.bpm.controller;

import com.supply.bpm.service.IProcessRunService;
import com.supply.common.model.Result;
import com.supply.common.web.model.BpmRequestEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description 外部服务调用.
 * @date 2023-06-21
 */
@Api(tags = "外部服务调用")
@RestController
@RequestMapping("/external")
public class ExternalController {

    private final IProcessRunService processRunService;

    public ExternalController(IProcessRunService processRunService) {
        this.processRunService = processRunService;
    }

    @ApiOperation(value = "流程发起")
    @PostMapping("/startProcess")
    public Result<?> startProcess(@RequestBody BpmRequestEntity request) {
        processRunService.startProcess(request);
        return Result.ok();
    }
}
