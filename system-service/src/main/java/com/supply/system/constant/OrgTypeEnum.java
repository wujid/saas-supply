package com.supply.system.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 组织机构类型枚举值.
 * @date 2022-07-28
 */
@Getter
@AllArgsConstructor
public enum OrgTypeEnum {

    COMPANY(1, "公司"),

    DEPT(2, "部门");


    @Note(description = "机构类型")
    private int type;

    @Note(description = "机构名称")
    private String name;

}
