package com.supply.bpm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.model.response.ProcessDefinitionResponse;
import com.supply.bpm.service.IProcessDefinitionService;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description 流程定义信息权限控制层.
 * @date 2022-12-20
 */
@Api(tags="流程定义信息权限控制层")
@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {

    private final IProcessDefinitionService processDefinitionService;

    public ProcessDefinitionController(IProcessDefinitionService processDefinitionService) {
        this.processDefinitionService = processDefinitionService;
    }

    @ApiOperation(value = "新增流程部署")
    @PostMapping("/addDeployment")
    public Result<?> addDeployment(ProcessDefinitionRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        processDefinitionService.addDeployment(request);
        return Result.ok();
    }

    @ApiOperation(value = "流程定义分页信息")
    @GetMapping("/getProcessDefinitionPage")
    public Result<IPage<ProcessDefinitionResponse>> getProcessDefinitionPage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                                             @RequestParam(required = false) Long categoryId, @RequestParam(required = false) Integer businessStatus,
                                                                             @RequestParam(required = false) String processName) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        ProcessDefinitionRequest request = new ProcessDefinitionRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setCategoryId(categoryId);
        request.setBusinessStatus(businessStatus);
        request.setProcessName(processName);
        request.setTenantId(tenantId);
        request.setIsDefault(true);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final IPage<ProcessDefinitionResponse> data = processDefinitionService.getProcessDefinitionPage(request);
        return Result.ok(data);
    }


}
