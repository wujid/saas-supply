package com.supply.common.exception;

import com.supply.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author wjd
 * @description 全局异常处理.
 * @date 2022-07-07
 */
@RestControllerAdvice
public class SupplyExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SupplyExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public Result<?> handleApiException(ApiException e){
        logger.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }

}
