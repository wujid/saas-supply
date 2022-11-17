package com.supply.auth.handle;

import com.alibaba.fastjson.JSON;
import com.supply.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wjd
 * @description 匿名用户访问无权限资源异常.
 * @date 2022-07-21
 */
@Component
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomizeAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        final String message = "请求访问:" + request.getRequestURI() + "接口,没有访问权限";
        logger.error(message);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.error(HttpStatus.UNAUTHORIZED.value(), message)));
    }
}
