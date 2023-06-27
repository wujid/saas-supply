package com.supply.bpm.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 流程节点操作按钮类型枚举值.
 * @date 2022-07-28
 */
@Getter
@AllArgsConstructor
public enum NodeButtonTypeEnum {

    AGREE(1, "同意"),

    AGAINST(2, "反对"),

    REJECT_TO_START_USER(3, "驳回到发起人");


    @Note(description = "节点操作类型")
    private final int type;

    @Note(description = "节点操作类型名称")
    private final String name;

}
