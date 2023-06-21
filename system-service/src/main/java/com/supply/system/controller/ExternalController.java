package com.supply.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.system.model.request.DataScopeTypeRequest;
import com.supply.system.model.request.ResourceRequest;
import com.supply.system.model.request.RoleRequest;
import com.supply.system.model.request.TenantRequest;
import com.supply.system.model.request.UserRequest;
import com.supply.system.model.request.UserRoleRequest;
import com.supply.system.model.response.DataScopeTypeResponse;
import com.supply.system.model.response.ResourceResponse;
import com.supply.system.model.response.RoleResponse;
import com.supply.system.model.response.TenantResponse;
import com.supply.system.model.response.UserResponse;
import com.supply.system.model.response.UserRoleResponse;
import com.supply.system.service.IDataScopeService;
import com.supply.system.service.IResourceService;
import com.supply.system.service.IRoleService;
import com.supply.system.service.ITenantService;
import com.supply.system.service.IUserRoleService;
import com.supply.system.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description 外部服务调用.
 * @date 2022-07-08
 */
@Api(tags = "外部服务调用")
@RestController
@RequestMapping("/external")
public class ExternalController {

    private final IUserService userService;

    private final ITenantService tenantService;

    private final IResourceService resourceService;

    private final IDataScopeService dataScopeService;

    private final IUserRoleService userRoleService;

    private final IRoleService roleService;

    public ExternalController(IUserService userService, ITenantService tenantService,
                              IResourceService resourceService, IDataScopeService dataScopeService,
                              IUserRoleService userRoleService, IRoleService roleService) {
        this.userService = userService;
        this.tenantService = tenantService;
        this.resourceService = resourceService;
        this.dataScopeService = dataScopeService;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }


    @ApiOperation(value = "根据自定义条件查询用户信息")
    @PostMapping("/getUseByParams")
    public Result<UserResponse> getUseByParams(@RequestBody UserRequest userRequest) {
        final UserResponse userInfo = userService.getUseByParams(userRequest);
        return Result.ok(userInfo);
    }

    @ApiOperation(value = "根据自定义条件查询用户信息集")
    @PostMapping("/getUsesByParams")
    public Result<List<UserResponse>> getUsesByParams(@RequestBody UserRequest userRequest) {
        final List<UserResponse> list = userService.getUserListByParams(userRequest);
        return Result.ok(list);
    }


    @ApiOperation(value = "根据客户端ID获取管理员用户信息")
    @GetMapping("/getManageUserByClientId")
    public Result<UserResponse> getManageUserByClientId(@RequestParam String clientId) {
        final UserResponse userInfo = userService.getManageUserByClientId(clientId);
        return Result.ok(userInfo);
    }

    @ApiOperation(value = "根据用户ID获取用户信息")
    @GetMapping("/getUserById")
    public Result<UserResponse> getUserById(@RequestParam Long userId) {
        UserRequest userRequest = new UserRequest();
        userRequest.setId(userId);
        final UserResponse userInfo = userService.getUseByParams(userRequest);
        return Result.ok(userInfo);
    }

    @ApiOperation(value = "租户过期")
    @GetMapping("/expireTenant")
    public Result<Object> expireTenant() {
        tenantService.expireTenant();
        return Result.ok();
    }

    @ApiOperation(value = "根据自定义条件查询租户信息")
    @PostMapping("/getTenantByParams")
    public Result<TenantResponse> getTenantByParams(@RequestBody TenantRequest request) {
        final TenantResponse tenant = tenantService.getTenantByParams(request);
        return Result.ok(tenant);
    }

    @ApiOperation(value = "根据自定义条件查询资源信息")
    @PostMapping("/getResourceByParams")
    public Result<ResourceResponse> getResourceByParams(@RequestBody ResourceRequest request) {
        final ResourceResponse resource = resourceService.getResourceByParams(request);
        return Result.ok(resource);
    }

    @ApiOperation(value = "根据条件查询用户资源权限信息")
    @GetMapping("/getDataScopeByParams")
    public Result<DataScopeTypeResponse> getDataScopeByParams(@RequestParam(required = false) Long userId,
                                                              @RequestParam(required = false) Long resourceId) {
        DataScopeTypeRequest request = new DataScopeTypeRequest();
        request.setUserId(userId);
        request.setResourceId(resourceId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final DataScopeTypeResponse dataScope = dataScopeService.getDataScopeByParams(request);
        return Result.ok(dataScope);
    }

    @ApiOperation(value = "根据角色ID集查询对应的用户ID集信息")
    @PostMapping("/getUserIdsByRoleIds")
    public Result<Set<Long>> getUserIdsByRoleIds(@RequestBody Set<Long> roleIds) {
        Set<Long> userIds = new HashSet<>();
        UserRoleRequest request = new UserRoleRequest();
        request.setRoleIds(roleIds);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<UserRoleResponse> list = userRoleService.getUserRoleListByParams(request);
        if (CollectionUtil.isNotEmpty(list)) {
            userIds = list.stream().map(UserRoleResponse::getUserId).collect(Collectors.toSet());
        }
        return Result.ok(userIds);
    }

    @ApiOperation(value = "根据角色ID集查询角色信息集")
    @PostMapping("/getRolesByIds")
    public Result<List<RoleResponse>> getRolesByIds(@RequestBody Set<Long> roleIds) {
        RoleRequest request = new RoleRequest();
        request.setRoleIds(roleIds);
        final List<RoleResponse> data = roleService.getListByParams(request);
        return Result.ok(data);
    }

}
