package com.supply.bpm.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.supply.bpm.model.request.NodeUserRequest;
import com.supply.bpm.model.response.NodeUserResponse;
import com.supply.bpm.service.INodeUserService;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
@Api(tags="流程节点审批人信息权限控制层")
@RestController
@RequestMapping("/nodeUser")
public class NodeUserController {

    private final INodeUserService nodeUserService;

    public NodeUserController(INodeUserService nodeUserService) {
        this.nodeUserService = nodeUserService;
    }

    @ApiOperation(value = "新增流程节点审批人信息")
    @PostMapping("/addNodeUser")
    public Result<?> addNodeUser(@RequestBody List<NodeUserRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return Result.error("不能为空");
        }
        final Long tenantId = ContextUtil.getCurrentTenantId();
        requests.forEach(request -> request.setTenantId(tenantId));
        nodeUserService.addNodeUser(requests);
        return Result.ok();
    }

    @ApiOperation(value = "修改流程节点审批人信息")
    @PostMapping("/updateNodeUser")
    public Result<?> updateNodeUser(@RequestBody List<NodeUserRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return Result.error("不能为空");
        }
        final Long tenantId = ContextUtil.getCurrentTenantId();
        requests.forEach(request -> request.setTenantId(tenantId));
        nodeUserService.updateNodeUser(requests);
        return Result.ok();
    }

    @ApiOperation(value = "获取流程节点审批人信息集")
    @GetMapping("/getNodeUserListByParams")
    public Result<List<NodeUserResponse>> getNodeUserListByParams(NodeUserRequest request) {
        final List<NodeUserResponse> data = nodeUserService.getNodeUserListByParams(request);
        return Result.ok(data);
    }


}
