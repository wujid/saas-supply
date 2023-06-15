package com.supply.bpm.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 流程节点任务类型.
 * @date 2023-06-07
 */
@Getter
@AllArgsConstructor
public enum NodeTypeEnum {

    START(1, "开始节点"),

    USER_OWNER(2, "用户任务节点--个人任务"),

    USER_CANDIDATE_USERS(3, "用户任务节点--候选人任务"),

    USER_GROUP(4, "用户任务节点--组任务"),

    END(5, "结束节点");


    @Note(description = "节点类型")
    private int type;

    @Note(description = "节点名称")
    private String name;
}
