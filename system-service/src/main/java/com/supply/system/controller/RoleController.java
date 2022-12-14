package com.supply.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import com.supply.system.model.po.RolePo;
import com.supply.system.model.request.RoleRequest;
import com.supply.system.model.response.RoleResponse;
import com.supply.system.service.IRoleService;
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
 * @description 系统角色信息控制层.
 * @date 2022-08-03
 */
@Api(tags="系统角色信息控制层")
@RestController
@RequestMapping("/sysRole")
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/addRole")
    public Result<Object> addRole(@RequestBody RoleRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        roleService.addRole(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改角色")
    @PostMapping("/updateRole")
    public Result<Object> updateRole(@RequestBody RoleRequest request) {
        roleService.updateRole(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除角色")
    @GetMapping("/delRole")
    public Result<Object> delRole(@RequestParam Long roleId) {
        roleService.delRole(roleId);
        return Result.ok();
    }

    @ApiOperation(value = "冻结角色")
    @GetMapping("/freezeRole")
    public Result<Object> freezeRole(@RequestParam Long roleId) {
        roleService.freezeRole(roleId);
        return Result.ok();
    }

    @ApiOperation(value = "解冻角色")
    @GetMapping("/activeRole")
    public Result<Object> activeRole(@RequestParam Long roleId) {
        roleService.activeUser(roleId);
        return Result.ok();
    }

    @ApiOperation(value = "设置角色资源权限")
    @PostMapping("/setResourceAuth")
    public Result<Object> setResourceAuth(@RequestBody RoleRequest request) {
        roleService.setResourceAuth(request);
        return Result.ok();
    }

    @ApiOperation(value = "查询角色信息集")
    @GetMapping("/getRoleListByParams")
    public Result<List<RoleResponse>> getRoleListByParams(@RequestParam(required = false) String name) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        RoleRequest request = new RoleRequest();
        request.setName(name);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setTenantId(tenantId);
        final List<RoleResponse> list = roleService.getListByParams(request);
        return Result.ok(list);
    }

    @ApiOperation(value = "获取角色的分页信息")
    @GetMapping("/getRolePageByParams")
    public Result<IPage<RoleResponse>> getRolePageByParams(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                           @RequestParam(required = false) String code, @RequestParam(required = false) String name,
                                                           @RequestParam(required = false) Integer businessStatus) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        RoleRequest request = new RoleRequest();
        request.setPageSize(pageSize);
        request.setPageIndex(pageIndex);
        request.setCode(code);
        request.setName(name);
        request.setBusinessStatus(businessStatus);
        request.setTenantId(tenantId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(RolePo::getCreateTime);
        final IPage<RoleResponse> page = roleService.getPageByParams(request);
        return Result.ok(page);
    }

    @ApiOperation(value = "通过角色ID获取对应的权限")
    @GetMapping("/getAuthByRoleId")
    public Result<RoleResponse> getAuthByRoleId(@RequestParam Long roleId) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        final RoleResponse response = roleService.getAuthByRoleId(roleId, tenantId);
        return Result.ok(response);
    }

}
