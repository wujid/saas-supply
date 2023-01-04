package com.supply.bpm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.model.response.ProcessDefinitionResponse;
import com.supply.bpm.service.IProcessDefinitionService;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    private final RepositoryService repositoryService;

    public ProcessDefinitionController(IProcessDefinitionService processDefinitionService, RepositoryService repositoryService) {
        this.processDefinitionService = processDefinitionService;
        this.repositoryService = repositoryService;
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
        request.setIsMain(true);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final IPage<ProcessDefinitionResponse> data = processDefinitionService.getProcessDefinitionPage(request);
        return Result.ok(data);
    }

    @ApiOperation(value = "导出流程定义信息")
    @GetMapping("/exportProcessDefinition")
    public void exportProcessDefinition(@RequestParam String definitionId, @RequestParam Integer type, HttpServletResponse response) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(definitionId).singleResult();
        String resourceName;
        if (type == 1) {
            resourceName = processDefinition.getResourceName();
        } else {
            resourceName = processDefinition.getDiagramResourceName();
        }
        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        try {
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(resourceName, StandardCharsets.UTF_8));
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            response.flushBuffer();
        } catch (Exception e) {
            throw new ApiException("导出流程定义信息失败", e);
        }
    }

}
