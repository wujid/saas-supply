package com.supply.common.web.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 数据权限范围枚举.
 * @date 2022-09-02
 */
@Getter
@AllArgsConstructor
public enum DataScopeRangeEnum {

    DATA_SCOPE_RANGE_ROLE(1, "角色"),

    DATA_SCOPE_RANGE_ORG(2, "公司"),

    DATA_SCOPE_RANGE_DEPT(3, "部门"),

    DATA_SCOPE_RANGE_USER(4, "用户");


    @Note(description = "数据权限范围")
    private int type;

    @Note(description = "数据权限范围说明")
    private String description;
}
