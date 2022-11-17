package com.supply.auth.handle;

import com.supply.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wjd
 * @description 全局处理Oauth2异常.
 * @date 2022-07-22
 */

@ControllerAdvice
public class Oauth2ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(Oauth2ExceptionHandler.class);


    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public Result<String> handleOauth2(OAuth2Exception e) {
        logger.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }
}
