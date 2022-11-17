package com.supply.common.web.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 数据权限类型枚举.
 * @date 2022-09-02
 */
@Getter
@AllArgsConstructor
public enum DataScopeTypeEnum {

    DATA_SCOPE_ALL(1, "所有数据权限"),

    DATA_SCOPE_ROLE(2, "角色数据权限"),

    DATA_SCOPE_ORG(3, "组织机构数据权限"),

    DATA_SCOPE_USER(4, "用户数据权限"),

    DATA_SCOPE_SELF(5, "个人数据权限"),

    DATA_SCOPE_CUSTOM(6, "自定义数据权限");


    @Note(description = "数据权限类型")
    private int type;

    @Note(description = "数据权限类型说明")
    private String description;
}
