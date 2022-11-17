package com.supply.common.web.annotation;

import com.supply.common.constant.OperatorTypeEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 填补基础数据.
 * @author wjd
 * @date 2021/12/25
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseData {

    /**
     * @author wjd
     * @description 字段自动填充策略.
     * @date 2022/4/27
     * @param
     * @return 操作类型
     **/
    OperatorTypeEnum fill() default OperatorTypeEnum.DEFAULT;
}
