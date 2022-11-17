package com.supply.auth.controller;

import com.supply.auth.model.LoginUser;
import com.supply.auth.model.request.ClientDetailRequest;
import com.supply.auth.service.OauthService;
import com.supply.auth.service.UserService;
import com.supply.common.annotation.Note;
import com.supply.common.model.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author wjd
 * @description 外部服务调用.
 * @date 2022-07-27
 */
@RestController
@RequestMapping("/external")
public class ExternalController {

    private final UserService userService;

    private final OauthService oauthService;

    public ExternalController(UserService userService, OauthService oauthService) {
        this.userService = userService;
        this.oauthService = oauthService;
    }

    @Note(description = "根据token获取登录用户信息")
    @RequestMapping("/getLoginInfoByToken")
    public Result<LoginUser> getLoginInfoByToken(@RequestParam String token) {
        final LoginUser loginUser = userService.getLoginInfoByToken(token);
        return Result.ok(loginUser);
    }

    @Note(description = "保存客户端详细信息")
    @PostMapping("/saveClientDetails")
    public Result<Object> saveClientDetails(@RequestBody ClientDetailRequest request) {
        oauthService.saveClientDetails(request);
        return Result.ok();
    }
}
