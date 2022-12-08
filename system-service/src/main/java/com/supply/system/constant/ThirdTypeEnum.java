package com.supply.system.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description .
 * @date 2022-12-08
 */
@Getter
@AllArgsConstructor
public enum ThirdTypeEnum {

    WE_CAT(1, "微信"),

    QQ(2, "QQ");


    @Note(description = "第三方平台类型")
    private int type;

    @Note(description = "第三方平台名称")
    private String name;
}
