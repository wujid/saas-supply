package com.supply.bpm.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 用户节点任务类型.
 * @date 2023-06-07
 */
@Getter
@AllArgsConstructor
public enum UserNodeTypeEnum {

    OWNER(1, "个人任务"),

    CANDIDATE_USERS(2, "候选人任务"),

    GROUP(3, "组任务");


    @Note(description = "机构类型")
    private int type;

    @Note(description = "机构名称")
    private String name;
}
