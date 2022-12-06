package com.supply.common.web.validate;

import com.supply.common.web.annotation.LongNotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author wjd
 * @description long型验证.
 * @date 2022-12-06
 */
public class LongValidator implements ConstraintValidator<LongNotEmpty, Long> {

    @Override
    public void initialize(LongNotEmpty constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        // 空或者0表示验证未通过
        if (aLong == null || aLong == 0L) {
            return false;
        }
        return true;
    }


}
