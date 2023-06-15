package com.supply.bpm.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.supply.bpm.model.po.UserNodeButtonPo;
import com.supply.bpm.model.request.UserNodeButtonRequest;
import com.supply.bpm.model.response.UserNodeButtonResponse;
import com.supply.bpm.service.IUserNodeButtonService;
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
 * @description 用户节点操作按钮信息控制层.
 * @date 2023-01-05
 */
@Api(tags="用户节点操作按钮信息控制层")
@RestController
@RequestMapping("/nodeButton")
public class UserNodeButtonController {

    private final IUserNodeButtonService userNodeButtonService;

    public UserNodeButtonController(IUserNodeButtonService userNodeButtonService) {
        this.userNodeButtonService = userNodeButtonService;
    }


    @ApiOperation(value = "新增用户节点按钮信息")
    @PostMapping("/addUserNodeButton")
    public Result<?> addUserNodeButton(@RequestBody List<UserNodeButtonRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return Result.error("不能为空");
        }
        final Long tenantId = ContextUtil.getCurrentTenantId();
        requests.forEach(request -> request.setTenantId(tenantId));
        userNodeButtonService.addUserNodeButton(requests);
        return Result.ok();
    }

    @ApiOperation(value = "修改用户节点按钮信息")
    @PostMapping("/updateUserNodeButton")
    public Result<?> updateUserNodeButton(@RequestBody List<UserNodeButtonRequest> requests) {
        if (CollectionUtil.isEmpty(requests)) {
            return Result.error("不能为空");
        }
        final Long tenantId = ContextUtil.getCurrentTenantId();
        requests.forEach(request -> request.setTenantId(tenantId));
        userNodeButtonService.updateUserNodeButton(requests);
        return Result.ok();
    }

    @ApiOperation(value = "获取用户节点按钮信息集")
    @GetMapping("/getNodeButtonListByParams")
    public Result<List<UserNodeButtonResponse>> getNodeUsersByUserNodeId(@RequestParam Long userNodeId) {
        UserNodeButtonRequest request = new UserNodeButtonRequest();
        request.setUserNodeId(userNodeId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(UserNodeButtonPo::getSort);
        request.setIsAsc(true);
        final List<UserNodeButtonResponse> data = userNodeButtonService.getUserNodeButtonListByParams(request);
        return Result.ok(data);
    }

}
