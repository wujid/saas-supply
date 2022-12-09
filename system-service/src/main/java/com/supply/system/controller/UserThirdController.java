package com.supply.system.controller;

import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.web.util.ContextUtil;
import com.supply.system.model.request.UserThirdRequest;
import com.supply.system.model.response.UserThirdResponse;
import com.supply.system.service.IUserThirdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wjd
 * @description 用户和第三方平台关联关系信息控制层.
 * @date 2022-12-08
 */
@Api(tags="用户和第三方平台关联关系信息控制层")
@RestController
@RequestMapping("/sysUser")
public class UserThirdController {

    private final IUserThirdService userThirdService;

    public UserThirdController(IUserThirdService userThirdService) {
        this.userThirdService = userThirdService;
    }

    @ApiOperation(value = "授权码回调")
    @GetMapping("/codeCallBack")
    public Result<?> codeCallBack(@RequestParam String code, @RequestParam int thirdType) {
        final SysUserResponse currentUser = ContextUtil.getCurrentUser();
        if (null == currentUser) {
            return Result.error("未获取到当前登录人信息");
        }
        UserThirdRequest request = new UserThirdRequest();
        request.setCode(code);
        request.setThirdType(thirdType);
        request.setUserId(currentUser.getId());
        request.setTenantId(currentUser.getTenantId());
        userThirdService.addThird(request);
        return Result.ok();
    }

    @ApiOperation(value = "解除关联关系")
    @GetMapping("/delThirdBind")
    public Result<?> delThirdBind(@RequestParam Long thirdId) {
        userThirdService.delThird(thirdId);
        return Result.ok();
    }

    @ApiOperation(value = "获取关联关系")
    @GetMapping("/getThirdList")
    public Result<List<UserThirdResponse>> getThirdList() {
        final SysUserResponse currentUser = ContextUtil.getCurrentUser();
        if (null == currentUser) {
            return Result.error("未获取到当前登录人信息");
        }
        UserThirdRequest request = new UserThirdRequest();
        request.setUserId(currentUser.getId());
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<UserThirdResponse> data = userThirdService.getThirdListByParams(request);
        return Result.ok(data);
    }

}
