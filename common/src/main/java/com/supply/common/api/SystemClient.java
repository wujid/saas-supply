package com.supply.common.api;

import com.supply.common.annotation.Note;
import com.supply.common.model.Result;
import com.supply.common.model.request.sys.SysResourceRequest;
import com.supply.common.model.request.sys.SysTenantRequest;
import com.supply.common.model.request.sys.SysUserRequest;
import com.supply.common.model.response.sys.SysDataScopeTypeResponse;
import com.supply.common.model.response.sys.SysResourceResponse;
import com.supply.common.model.response.sys.SysRoleResponse;
import com.supply.common.model.response.sys.SysTenantResponse;
import com.supply.common.model.response.sys.SysUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
@FeignClient("system-service")
public interface SystemClient {

    @Note(description = "根据自定义条件查询用户信息")
    @PostMapping("/external/getUseByParams")
    Result<SysUserResponse> getUseByParams(@RequestBody SysUserRequest sysUserRequest);

    @Note(description = "根据自定义条件查询用户信息集")
    @PostMapping("/external/getUsesByParams")
    Result<List<SysUserResponse>> getUsesByParams(@RequestBody SysUserRequest userRequest);

    @Note(description = "根据客户端ID获取管理员用户信息")
    @GetMapping("/external/getManageUserByClientId")
    Result<SysUserResponse> getManageUserByClientId(@RequestParam String clientId);

    @Note(description = "根据用户ID获取用户信息")
    @GetMapping("/external/getUserById")
    Result<SysUserResponse> getUserById(@RequestParam Long userId);

    @Note(description = "根据自定义条件查询资源信息")
    @PostMapping("/external/getResourceByParams")
    Result<SysResourceResponse> getResourceByParams(@RequestBody SysResourceRequest request);

    @Note(description = "根据条件查询用户资源权限信息")
    @GetMapping("/external/getDataScopeByParams")
    Result<SysDataScopeTypeResponse> getDataScopeByParams(@RequestParam(required = false) Long userId,
                                                          @RequestParam(required = false) Long resourceId);

    @Note(description = "根据角色ID集查询对应的用户ID集信息")
    @PostMapping("/external/getUserIdsByRoleIds")
    Result<Set<Long>> getUserIdsByRoleIds(@RequestBody Set<Long> roleIds);

    @Note(description = "根据自定义条件查询租户信息")
    @PostMapping("/external/getTenantByParams")
    Result<SysTenantResponse> getTenantByParams(@RequestBody SysTenantRequest request);

    @Note(description = "根据角色ID集查询角色信息集")
    @PostMapping("/external/getRolesByIds")
    Result<List<SysRoleResponse>> getRolesByIds(@RequestBody Set<Long> roleIds);
}
