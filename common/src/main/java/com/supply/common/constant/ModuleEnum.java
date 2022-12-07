package com.supply.common.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 功能模块枚举类.
 * @date 2022-07-28
 */
@Getter
@AllArgsConstructor
public enum ModuleEnum {

    COMMON("COMMON", "公共模块"),

    SYSTEM("SYSTEM", "系统模块"),

    BPM("BPM", "流程模块"),

    FILE("FILE", "文件模块"),

    MESSAGE("MESSAGE", "消息模块");

    @Note(description = "模块")
    private final String module;

    @Note(description = "模块名称")
    private final String name;
}
