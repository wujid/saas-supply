package com.supply.common.annotation;

import java.lang.annotation.*;

/**
 * @author wjd
 * @description 功能属性描述.
 * @date 2022-07-22
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Note {
    String description() default "";
}
