package com.supply.bpm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.po.ProcessDefinitionPo;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

    @ApiOperation(value = "流程挂起")
    @GetMapping("/suspendProcess")
    public Result<?> suspendProcess(Long defId) {
        processDefinitionService.suspendProcess(defId);
        return Result.ok();
    }

    @ApiOperation(value = "流程激活")
    @GetMapping("/activeProcess")
    public Result<?> activeProcess(Long defId) {
        processDefinitionService.activeProcess(defId);
        return Result.ok();
    }

    @ApiOperation(value = "流程删除")
    @GetMapping("/delProcess")
    public Result<?> delProcess(Long defId) {
        processDefinitionService.delProcess(defId);
        return Result.ok();
    }

    @ApiOperation(value = "修改流程标题")
    @PostMapping("/updateProcessTitle")
    public Result<?> updateProcessTitle(@RequestBody ProcessDefinitionRequest request) {
        ProcessDefinitionRequest processDefinitionRequest = new ProcessDefinitionRequest();
        processDefinitionRequest.setId(request.getId());
        processDefinitionRequest.setTitle(request.getTitle());
        processDefinitionService.updateProcessById(processDefinitionRequest);
        return Result.ok();
    }

    @ApiOperation(value = "修改表单URL")
    @PostMapping("/updateProcessFormUrl")
    public Result<?> updateProcessFormUrl(@RequestBody ProcessDefinitionRequest request) {
        ProcessDefinitionRequest processDefinitionRequest = new ProcessDefinitionRequest();
        processDefinitionRequest.setId(request.getId());
        processDefinitionRequest.setFormUrl(request.getFormUrl());
        processDefinitionService.updateProcessById(processDefinitionRequest);
        return Result.ok();
    }

    @ApiOperation(value = "修改流程结束脚本")
    @PostMapping("/updateProcessEndScript")
    public Result<?> updateProcessEndScript(@RequestBody ProcessDefinitionRequest request) {
        ProcessDefinitionRequest processDefinitionRequest = new ProcessDefinitionRequest();
        processDefinitionRequest.setId(request.getId());
        processDefinitionRequest.setEndScript(request.getEndScript());
        processDefinitionService.updateProcessById(processDefinitionRequest);
        return Result.ok();
    }

    @ApiOperation(value = "修改流程为当前流程版本中使用状态")
    @GetMapping("/updateProcessInUse")
    public Result<?> updateProcessInUse(Long defId) {
        processDefinitionService.updateProcessInUse(defId);
        return Result.ok();
    }

    @ApiOperation(value = "修改流程为默认流程")
    @GetMapping("/updateDefaultProcess")
    public Result<?> updateDefaultProcess(Long defId) {
        processDefinitionService.updateDefaultProcess(defId);
        return Result.ok();
    }

    @ApiOperation(value = "流程定义分页信息")
    @GetMapping("/getProcessDefinitionPage")
    public Result<IPage<ProcessDefinitionResponse>> getProcessDefinitionPage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                                             @RequestParam(required = false) Long categoryId, @RequestParam(required = false) Integer businessStatus,
                                                                             @RequestParam(required = false) String processName, @RequestParam(required = false) Long groupId,
                                                                             @RequestParam(required = false) Integer version,  @RequestParam(required = false) Boolean isDefault,
                                                                             @RequestParam(required = false) Boolean isGroupUse) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        ProcessDefinitionRequest request = new ProcessDefinitionRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setCategoryId(categoryId);
        request.setBusinessStatus(businessStatus);
        request.setProcessName(processName);
        request.setTenantId(tenantId);
        request.setIsDefault(isDefault);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setGroupId(groupId);
        request.setIsGroupUse(isGroupUse);
        request.setVersion(version);
        request.setOrderColumn(ProcessDefinitionPo::getCreateTime);
        final IPage<ProcessDefinitionResponse> data = processDefinitionService.getProcessDefinitionPage(request);
        return Result.ok(data);
    }

    @ApiOperation(value = "获取流程定义xml")
    @GetMapping("/getProcessDefinitionXml")
    public void getProcessDefinitionXml(@RequestParam String deploymentId, @RequestParam String processName, HttpServletResponse response) throws IOException {
        processDefinitionService.getProcessDefinitionXml(deploymentId, processName, response);
    }

    @ApiOperation(value = "根据流程定义ID获取流程信息")
    @GetMapping("/getByDefinitionId")
    public Result<?> getByDefinitionId(@RequestParam String definitionId) {
        ProcessDefinitionRequest request = new ProcessDefinitionRequest();
        request.setDefinitionId(definitionId);
        final ProcessDefinitionResponse data = processDefinitionService.getByParams(request);
        return Result.ok(data);
    }

    @ApiOperation(value = "根据流程分类编码查询流程定义信息集")
    @GetMapping("/getDefinitionsByCategoryCode")
    public Result<?> getDefinitionsByCategoryCode(@RequestParam String categoryCode) {
        final List<ProcessDefinitionResponse> data = processDefinitionService.getListByCategoryCode(categoryCode);
        return Result.ok(data);
    }
}
