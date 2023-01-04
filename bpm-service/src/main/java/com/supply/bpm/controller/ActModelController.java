package com.supply.bpm.controller;

import com.supply.bpm.model.request.ActModelRequest;
import com.supply.bpm.model.response.ActModelResponse;
import com.supply.bpm.service.IActModelService;
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
 * @date 2022-12-19
 */
@Api(tags="流程模型信息权限控制层")
@RestController
@RequestMapping("/actModel")
public class ActModelController {

    private final IActModelService actModelService;

    public ActModelController(IActModelService actModelService) {
        this.actModelService = actModelService;
    }

    @ApiOperation(value = "新增流程模型")
    @PostMapping("/addModel")
    public Result<Object> addModel(@RequestBody ActModelRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        final ActModelResponse data = actModelService.addModel(request);
        return Result.ok(data);
    }
}
