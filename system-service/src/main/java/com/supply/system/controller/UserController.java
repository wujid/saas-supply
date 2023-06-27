package com.supply.system.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import com.supply.common.web.util.DataScopeUtil;
import com.supply.common.web.validate.AddGroup;
import com.supply.common.web.validate.UpdateGroup;
import com.supply.system.model.po.UserPo;
import com.supply.system.model.request.UserRequest;
import com.supply.system.model.response.UserResponse;
import com.supply.system.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author wjd
 * @description 用户信息控制层.
 * @date 2022-07-08
 */
@Api(tags="用户信息控制层")
@RestController
@RequestMapping("/sysUser")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/addUser")
    public Result<Object> addUser(@RequestBody @Validated(value = AddGroup.class) UserRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        userService.addUser(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改用户")
    @PostMapping("/updateUser")
    public Result<Object> updateUser(@RequestBody @Validated(value = UpdateGroup.class) UserRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        userService.updateUser(request);
        return Result.ok();
    }

    @ApiOperation(value = "重置密码")
    @GetMapping("/resetPassword")
    public Result<Object> resetPassword(@RequestParam Long userId, @RequestParam String password) {
        userService.resetPassword(userId, password);
        return Result.ok();
    }

    @ApiOperation(value = "根据用户ID删除用户")
    @GetMapping("/delUser")
    public Result<Object> delUser(@RequestParam Long userId) {
        userService.delUser(userId);
        return Result.ok();
    }

    @ApiOperation(value = "根据用户ID冻结用户")
    @GetMapping("/freezeUser")
    public Result<Object> freezeUser(@RequestParam Long userId) {
        userService.freezeUser(userId);
        return Result.ok();
    }

    @ApiOperation(value = "根据用户ID启用用户")
    @GetMapping("/activeUser")
    public Result<Object> activeUser(@RequestParam Long userId) {
        userService.activeUser(userId);
        return Result.ok();
    }

    @ApiOperation(value = "用户分页")
    @GetMapping("/getUserPage")
    public Result<Object> getUserPage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                      @RequestParam(required = false) String account, @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String telephone,  @RequestParam(required = false) String email,
                                      @RequestParam(required = false) Integer businessStatus,  @RequestParam(required = false) Long orgId,
                                      @RequestParam(required = false) Long deptId, @RequestParam(required = false) String deptIds,
                                      @RequestParam(required = false) Long roleId, @RequestParam(defaultValue = "false") Boolean isUseDataScope) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        UserRequest request = new UserRequest();
        request.setTenantId(tenantId);
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setLikeAccount(account);
        request.setLikeName(name);
        request.setLikeTelephone(telephone);
        request.setLikeEmail(email);
        request.setBusinessStatus(businessStatus);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrgId(orgId);
        request.setDepartId(deptId);
        if (StrUtil.isNotBlank(deptIds)) {
            final Set<Long> set = Convert.toSet(Long.class, deptIds);
            request.setDeptIds(set);
        }
        request.setRoleId(roleId);
        request.setOrderColumn(UserPo::getCreateTime);
        if (isUseDataScope) {
            final String dataScopeSql = DataScopeUtil.getDataScopeSql("ACCOUNT_MANAGE", "org_id", "depart_id", "id");
            request.setAuthSql(dataScopeSql);
        }
        final IPage<UserResponse> userPage = userService.getUserPage(request);
        return Result.ok(userPage);
    }

    @ApiOperation(value = "根据自定义条件查询用户信息集")
    @GetMapping("/getUserListByParams")
    public Result<List<UserResponse>> getUserListByParams(@RequestParam(required = false) String likeName) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        UserRequest request = new UserRequest();
        request.setTenantId(tenantId);
        request.setLikeName(likeName);
        final List<UserResponse> list = userService.getUserListByParams(request);
        return Result.ok(list);
    }

}
