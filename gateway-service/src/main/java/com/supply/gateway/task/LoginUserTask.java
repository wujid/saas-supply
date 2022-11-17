package com.supply.gateway.task;

import com.supply.common.model.Result;
import com.supply.common.util.SpringContextUtil;
import com.supply.gateway.api.AuthClient;
import com.supply.gateway.model.LoginUser;

import java.util.concurrent.Callable;

/**
 * @author wjd
 * @description
 * @date 2022-07-14
 */
public class LoginUserTask implements Callable<Result<LoginUser>> {

    public String token;

    public LoginUserTask(String token) {
        this.token = token;
    }

    @Override
    public Result<LoginUser> call() {
        final AuthClient securityClient = SpringContextUtil.getFeignBean("auth-service", AuthClient.class);
        return securityClient.getLoginUserByToken(token);
    }

}
