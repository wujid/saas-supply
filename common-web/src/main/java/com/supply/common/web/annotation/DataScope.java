package com.supply.common.web.annotation;

import com.supply.common.annotation.Note;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
  * @description 用户数据权限过滤.
  * @author wjd
  * @date 2022/9/5
  */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    @Note(description = "组织机构列别名")
    String orgAlias() default "org_id";

    @Note(description = "部门列别名")
    String deptAlias() default "depart_id";

    @Note(description = "用户表别名")
    String userAlias() default "create_user_id";

    @Note(description = "资源编码")
    String resourceCode();
}
