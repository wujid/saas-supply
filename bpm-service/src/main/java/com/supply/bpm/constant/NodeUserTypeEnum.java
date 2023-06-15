package com.supply.bpm.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 流程节点审批人类型枚举值.
 * @date 2022-07-28
 */
@Getter
@AllArgsConstructor
public enum NodeUserTypeEnum {

    USER(1, "用户"),

    ROLE(2, "角色"),

    DEPT(3, "部门");


    @Note(description = "节点用户类型")
    private final int type;

    @Note(description = "节点用户名称")
    private final String name;

}
