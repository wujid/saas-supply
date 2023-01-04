package com.supply.bpm.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 流程节点按钮类型枚举值.
 * @date 2022-07-28
 */
@Getter
@AllArgsConstructor
public enum ButtonTypeEnum {

    AGREE(1, "同意"),

    REJECT(2, "反对"),

    OPPOSE_TO_START_USER(3, "驳回到发起人");


    @Note(description = "按钮类型")
    private final int type;

    @Note(description = "按钮名称")
    private final String name;

}
