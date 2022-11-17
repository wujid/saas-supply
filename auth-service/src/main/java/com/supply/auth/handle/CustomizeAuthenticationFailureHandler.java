package com.supply.auth.handle;

import com.alibaba.fastjson.JSON;
import com.supply.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wjd
 * @description 登录失败处理.
 * @date 2022-07-21
 */
@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomizeAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        final String message = "请求访问:" + request.getRequestURI() + "接口, 账号认证失败";
        logger.error(message);
        // 返回json数据
        Result result;
        if (exception instanceof AccountExpiredException) {
            // 账号过期
            result = Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "账号过期");
        } else if (exception instanceof BadCredentialsException) {
            // 密码错误
            result = Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "账号密码错误");
        } else{
            //其他错误
            result = Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常");
        }
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(result));
    }
}
