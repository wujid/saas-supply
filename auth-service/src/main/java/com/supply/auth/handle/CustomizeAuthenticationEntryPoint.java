package com.supply.auth.handle;

import com.alibaba.fastjson.JSON;
import com.supply.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wjd
 * @description 认证用户但无相应资源访问权限.
 * @date 2022-07-21
 */
@Component
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CustomizeAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        final String message = "请求访问:" + request.getRequestURI() + "接口,jwt认证失败";
        logger.error(message);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.error(HttpStatus.UNAUTHORIZED.value(), message)));
    }
}
