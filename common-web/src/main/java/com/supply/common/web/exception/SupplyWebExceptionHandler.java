package com.supply.common.web.exception;

import com.supply.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wjd
 * @description 全局异常处理.
 * @date 2022-07-07
 */
@RestControllerAdvice
public class SupplyWebExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SupplyWebExceptionHandler.class);

    @ExceptionHandler(BindException.class)
    public Result<?> handleApiException(BindException  e){
        logger.error(e.getMessage(), e);
        BindingResult result = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        //1.获取校验的错误结果
        result.getFieldErrors().forEach(item -> {
            // FieldError获取到错误提示
            String message = item.getDefaultMessage();
            // 获取错误的属性的名字
            String field = item.getField();
            map.put(field,message);
        });
        return Result.error("参数验证不通过", map);
    }

}
