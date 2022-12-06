package com.supply.common.web.annotation;

import com.supply.common.web.validate.LongValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wjd
 * @description 验证Long型是否为空
 * @date 2022-12-06
 */
@Constraint(validatedBy = {LongValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LongNotEmpty {

    String message() default "long型值不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
