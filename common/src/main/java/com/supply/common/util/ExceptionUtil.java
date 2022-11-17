package com.supply.common.util;

import cn.hutool.core.util.StrUtil;
import com.supply.common.exception.ApiException;
import com.supply.common.exception.ICustomerException;
import com.supply.common.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @description: 异常处理工具类.
 * @Author wjd
 * @date 2022/3/28
 */
public class ExceptionUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);


    /**
     * @author wjd
     * @description 当为真时进行异常处理.
     * @date 2022/3/28
     * @return 异常信息
     **/
    public static ICustomerException error(boolean isTrue) {
        return (t, format, arguments) -> {
            if (isTrue) {
                final String message = StrUtil.format(format, arguments);
                logger.error(message);
                throw new ApiException(message, t);
            }
        };
    }

    /**
      * @description 将异常以字符串的方式进行输出.
      * @author wjd
      * @date 2022/6/2
      * @param t 异常
      * @return 格式化后的异常
      */
    public static String printStackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }

    /**
     * @description 将异常格式化输出.
     * @author wjd
     * @date 2022/6/2
     * @param t 异常
     * @param httpStatus 异常类型
     * @return 格式化后的异常
     */
    public static Result errorStack(Throwable t, HttpStatus httpStatus) {
        final String errorMessage = printStackTraceToString(t);
        return Result.error("未知异常", errorMessage);
    }
}
