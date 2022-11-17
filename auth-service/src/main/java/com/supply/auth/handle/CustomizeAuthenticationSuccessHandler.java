package com.supply.auth.handle;

import com.alibaba.fastjson.JSON;
import com.supply.auth.model.LoginUser;
import com.supply.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wjd
 * @description 登录成功.
 * @date 2022-07-20
 */
@Component
public class CustomizeAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomizeAuthenticationSuccessHandler.class);


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        logger.info("用户{}登录成功", loginUser.getUsername());

        //此处还可以进行一些处理,比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限
        //进而前台动态的控制菜单的显示等,具体根据自己的业务需求进行扩展
        //返回json数据
        final Result<Object> ok = Result.ok();

        //处理编码方式，防止中文乱码的情况
        httpServletResponse.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        httpServletResponse.getWriter().write(JSON.toJSONString(ok));
    }

}
