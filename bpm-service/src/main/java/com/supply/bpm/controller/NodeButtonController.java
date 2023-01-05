package com.supply.bpm.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.supply.bpm.model.po.NodeButtonPo;
import com.supply.bpm.model.request.NodeButtonRequest;
import com.supply.bpm.model.response.NodeButtonResponse;
import com.supply.bpm.service.INodeButtonService;
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
@Api(tags="流程节点按钮信息权限控制层")
@RestController
@RequestMapping("/nodeButton")
public class NodeButtonController {

    private final INodeButtonService nodeButtonService;

    public NodeButtonController(INodeButtonService nodeButtonService) {
        this.nodeButtonService = nodeButtonService;
    }

    @ApiOperation(value = "新增流程节点按钮信息")
    @PostMapping("/addNodeButton")
    public Result<?> addNodeButton(@RequestBody List<NodeButtonRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return Result.error("不能为空");
        }
        final Long tenantId = ContextUtil.getCurrentTenantId();
        requests.forEach(request -> request.setTenantId(tenantId));
        nodeButtonService.addNodeButton(requests);
        return Result.ok();
    }

    @ApiOperation(value = "修改流程节点按钮信息")
    @PostMapping("/updateNodeButton")
    public Result<?> updateNodeButton(@RequestBody List<NodeButtonRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return Result.error("不能为空");
        }
        final Long tenantId = ContextUtil.getCurrentTenantId();
        requests.forEach(request -> request.setTenantId(tenantId));
        nodeButtonService.updateNodeButton(requests);
        return Result.ok();
    }

    @ApiOperation(value = "获取流程节点按钮信息集")
    @GetMapping("/getNodeButtonListByParams")
    public Result<List<NodeButtonResponse>> getNodeUserListByParams(NodeButtonRequest request) {
        request.setOrderColumn(NodeButtonPo::getSort);
        request.setIsAsc(true);
        final List<NodeButtonResponse> data = nodeButtonService.getNodeButtonListByParams(request);
        return Result.ok(data);
    }

}
