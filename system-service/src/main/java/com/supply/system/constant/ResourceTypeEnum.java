package com.supply.system.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {

    MENU(1, "菜单"),

    BUTTON(2, "按钮");


    @Note(description = "资源类型")
    private int type;

    @Note(description = "资源名称")
    private String name;

    }
