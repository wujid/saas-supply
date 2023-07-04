package com.supply.bpm.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 流程节点任务审批状态枚举值.
 * @date 2023-06-07
 */
@Getter
@AllArgsConstructor
public enum CheckStatusEnum {

    STATUS_CHECKING(1, "审批中"),

    STATUS_ASSIGN(2, "交办"),

    STATUS_DELEGATE(3, "委派"),

    STATUS_AGREE(4, "同意"),

    STATUS_REFUSE(5, "反对"),

    STATUS_REJECT(6, "驳回"),

    STATUS_RECOVER(7, "撤回");


    @Note(description = "审批状态")
    private int status;

    @Note(description = "审批状态名称")
    private String name;
}
