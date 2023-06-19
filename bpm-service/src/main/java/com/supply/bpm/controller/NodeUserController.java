package com.supply.bpm.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.po.NodeUserPo;
import com.supply.bpm.model.request.NodeUserRequest;
import com.supply.bpm.model.response.NodeUserResponse;
import com.supply.bpm.service.INodeUserService;
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
@Api(tags="流程节点审批人信息权限控制层")
@RestController
@RequestMapping("/nodeUser")
public class NodeUserController {

    private final INodeUserService nodeUserService;

    public NodeUserController(INodeUserService nodeUserService) {
        this.nodeUserService = nodeUserService;
    }

    @ApiOperation(value = "新增流程节点审批人信息")
    @PostMapping("/addNodeUsers")
    public Result<?> addNodeUsers(@RequestBody List<NodeUserRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return Result.error("不能为空");
        }
        final Long tenantId = ContextUtil.getCurrentTenantId();
        requests.forEach(request -> request.setTenantId(tenantId));
        nodeUserService.addNodeUsers(requests);
        return Result.ok();
    }

    @ApiOperation(value = "删除流程节点审批人信息")
    @GetMapping("/delNodeUser")
    public Result<?> delNodeUser(@RequestParam Long id) {
        nodeUserService.delNodeUser(id);
        return Result.ok();
    }

    @ApiOperation(value = "获取流程审批人分页信息")
    @GetMapping("/getNodeUserPage")
    public Result<?> getNodeUserPage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                          @RequestParam(required = false) Long nodeSetId, @RequestParam(required = false) Integer nodeUserType) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        NodeUserRequest request = new NodeUserRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setTenantId(tenantId);
        request.setNodeSetId(nodeSetId);
        request.setNodeUserType(nodeUserType);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(NodeUserPo::getCreateTime);
        final IPage<NodeUserResponse> data = nodeUserService.getNodeUserPage(request);
        return Result.ok(data);
    }


}