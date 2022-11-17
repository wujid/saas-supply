package com.supply.common.exception;

/**
 * @description: 异常处理接口
 * @Author wjd
 * @date 2022/3/26
 */
@FunctionalInterface
public interface ICustomerException {

    void errorMessage(Throwable t, String format, Object... arguments);
}
