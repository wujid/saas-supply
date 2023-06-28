package com.supply.business.controller;

import com.supply.business.service.IWorkLeaveService;
import com.supply.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description .
 * @date 2023-06-28
 */
@Api(tags = "外部服务调用")
@RestController
@RequestMapping("/external")
public class ExternalController {

    private final IWorkLeaveService workLeaveService;

    public ExternalController(IWorkLeaveService workLeaveService) {
        this.workLeaveService = workLeaveService;
    }

    @ApiOperation(value = "流程结束回调修改请假业务状态")
    @GetMapping("/updateBusinessStatus")
    public Result<?> updateBusinessStatus(@RequestParam String businessId, @RequestParam Integer approvalType) {
        workLeaveService.updateBusinessStatus(businessId, approvalType);
        return Result.ok();
    }
}
