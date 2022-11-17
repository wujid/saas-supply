package com.supply.system.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 租户类型枚举值.
 * @date 2022-07-28
 */
@Getter
@AllArgsConstructor
public enum TenantTypeEnum {

    PERSONAL(1, "个人"),

    COMPANY(2, "组织机构");


    @Note(description = "租户类型")
    private int type;

    @Note(description = "租户名称")
    private String name;

}
