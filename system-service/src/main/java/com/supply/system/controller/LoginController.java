package com.supply.system.controller;

import com.supply.common.model.Result;
import com.supply.common.model.response.auth.AuthTokenResponse;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.web.util.ContextUtil;
import com.supply.system.model.request.LoginRequest;
import com.supply.system.model.response.UserInfoResponse;
import com.supply.system.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description 用户登录控制层.
 * @date 2022-08-11
 */
@Api(tags="用户登录控制层")
@RestController
@RequestMapping("/login")
public class LoginController {

    private final ILoginService loginService;

    public LoginController(ILoginService loginService) {
        this.loginService = loginService;
    }


    @ApiOperation(value = "密码模式登录")
    @PostMapping("/loginForPwd")
    public Result<AuthTokenResponse> login(@RequestBody @Validated LoginRequest request) {
        final AuthTokenResponse tokenResponse = loginService.login(request);
        return Result.ok(tokenResponse);
    }

    @ApiOperation(value = "微信登录")
    @GetMapping("/loginByWeCat")
    public Result<AuthTokenResponse> loginByWeCat(@RequestParam String code) {
        final AuthTokenResponse tokenResponse = loginService.loginByWeCat(code);
        return Result.ok(tokenResponse);
    }

    @ApiOperation(value = "退出")
    @PostMapping("/logout")
    public Result<Object> logout() {
        final SysUserResponse currentUser = ContextUtil.getCurrentUser();
        if (null == currentUser) {
            return Result.error("退出失败!");
        }
        loginService.logout(currentUser.getId());
        return Result.ok();
    }

    @ApiOperation(value = "生成随机验证码")
    @GetMapping("/generateCaptcha")
    public void generateCaptcha(@RequestParam String key) {
        loginService.generateCaptcha(key);
    }

    @ApiOperation(value = "获取当前登录人用户信息")
    @GetMapping("/getUserInfo")
    public Result<UserInfoResponse> getUserInfo() {
        final SysUserResponse currentUser = ContextUtil.getCurrentUser();
        if (null == currentUser) {
            return Result.error("未获取到当前登录人信息");
        }
        final UserInfoResponse userInfo = loginService.getUserInfo(currentUser.getId());
        return Result.ok(userInfo);
    }
}
