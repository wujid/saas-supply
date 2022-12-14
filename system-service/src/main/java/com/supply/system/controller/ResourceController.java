package com.supply.system.controller;

import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.request.ResourceRequest;
import com.supply.system.model.response.ResourceResponse;
import com.supply.system.service.IResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wjd
 * @description 系统资源信息控制层.
 * @date 2022-08-05
 */
@Api(tags="系统资源信息控制层")
@RestController
@RequestMapping("/sysResource")
public class ResourceController {

    private final IResourceService resourceService;

    public ResourceController(IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @ApiOperation(value = "新增资源")
    @PostMapping("/addResource")
    public Result<Object> addResource(@RequestBody ResourceRequest request) {
        resourceService.addResource(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改资源")
    @PostMapping("/updateResource")
    public Result<Object> updateResource(@RequestBody ResourceRequest request) {
        resourceService.updateResource(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除资源")
    @GetMapping("/delResource")
    public Result<Object> delResource(@RequestParam Long resourceId) {
        resourceService.delResource(resourceId);
        return Result.ok();
    }

    @ApiOperation(value = "资源冻结")
    @GetMapping("/freezeResource")
    public Result<Object> freezeResource(@RequestParam Long resourceId) {
        resourceService.freezeResource(resourceId);
        return Result.ok();
    }

    @ApiOperation(value = "资源解冻")
    @GetMapping("/activeResource")
    public Result<Object> activeResource(@RequestParam Long resourceId) {
        resourceService.activeResource(resourceId);
        return Result.ok();
    }

    @ApiOperation(value = "资源树结构")
    @GetMapping("/getResourceTree")
    public Result<List<ResourceResponse>> getResourceTree(@RequestParam(required = false) Integer type) {
        ResourceRequest request = new ResourceRequest();
        request.setType(type);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(ResourcePo::getSort);
        request.setIsAsc(true);
        final List<ResourceResponse> list = resourceService.getResourceTree(request);
        return Result.ok(list);
    }
}
