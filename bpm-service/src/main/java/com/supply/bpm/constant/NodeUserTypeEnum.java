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

    ROLE(1, "角色"),

    DEPT(2, "部门"),

    USER(3, "用户");


    @Note(description = "节点类型")
    private final int type;

    @Note(description = "节点名称")
    private final String name;

}
