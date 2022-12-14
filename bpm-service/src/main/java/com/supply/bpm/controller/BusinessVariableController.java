package com.supply.bpm.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.BusinessVariableRequest;
import com.supply.bpm.model.response.BusinessVariableResponse;
import com.supply.bpm.service.IBusinessVariableService;
import com.supply.common.constant.Constant;
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

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
@Api(tags="流程业务参数信息权限控制层")
@RestController
@RequestMapping("/businessVariable")
public class BusinessVariableController {

    private final IBusinessVariableService businessVariableService;

    public BusinessVariableController(IBusinessVariableService businessVariableService) {
        this.businessVariableService = businessVariableService;
    }

    @ApiOperation(value = "新增流程业务参数")
    @PostMapping("/addBusinessVariable")
    public Result<?> addNodeUser(@RequestBody List<BusinessVariableRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return Result.error("不能为空");
        }
        final Long tenantId = ContextUtil.getCurrentTenantId();
        requests.forEach(request -> request.setTenantId(tenantId));
        businessVariableService.addBusinessVariable(requests);
        return Result.ok();
    }

    @ApiOperation(value = "修改流程业务参数")
    @PostMapping("/updateBusinessVariable")
    public Result<?> updateNodeUser(@RequestBody List<BusinessVariableRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return Result.error("不能为空");
        }
        final Long tenantId = ContextUtil.getCurrentTenantId();
        requests.forEach(request -> request.setTenantId(tenantId));
        businessVariableService.updateBusinessVariable(requests);
        return Result.ok();
    }

    @ApiOperation(value = "流程业务参数分页信息")
    @GetMapping("/getProcessDefinitionPage")
    public Result<IPage<BusinessVariableResponse>> getProcessDefinitionPage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                                             @RequestParam(required = false) String definitionId, @RequestParam(required = false) String variableKey,
                                                                             @RequestParam(required = false) String variableName) {
        BusinessVariableRequest request = new BusinessVariableRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setDefinitionId(definitionId);
        request.setVariableKey(variableKey);
        request.setVariableName(variableName);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final IPage<BusinessVariableResponse> data = businessVariableService.getBusinessVariablePage(request);
        return Result.ok(data);
    }

}
