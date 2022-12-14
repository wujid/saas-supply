package com.supply.system.controller;

import com.supply.common.model.Result;
import com.supply.system.model.request.UserRoleRequest;
import com.supply.system.service.IUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description
 * @date 2022-09-30
 */
@Api(tags="用户角色关联关系信息控制层")
@RestController
@RequestMapping("/userRole")
public class UserRoleController {

    private final IUserRoleService userRoleService;

    public UserRoleController(IUserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @ApiOperation(value = "新增用户角色关联关系")
    @PostMapping("/addUserRole")
    public Result<Object> addUserRole(@RequestBody UserRoleRequest request) {
        userRoleService.addUserRole(request);
        return Result.ok();
    }

    @ApiOperation(value = "取消用户授权")
    @GetMapping("/delUserAuth")
    public Result<Object> delUserAuth(@RequestParam Long userId, @RequestParam Long roleId) {
        UserRoleRequest request = new UserRoleRequest();
        request.setUserId(userId);
        request.setRoleId(roleId);
        userRoleService.delUserRoleByParams(request);
        return Result.ok();
    }
}
