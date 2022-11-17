package com.supply.message.constant;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 消息通知方式枚举.
 * @date 2022-10-29
 */
@Getter
@AllArgsConstructor
public enum NotifyTypeEnum {


    SYSTEM(1, "系统"),

    EMAIL(2, "邮件"),

    SMS(3, "短信");

    @ApiModelProperty(value = "类型")
    private int type;

    @ApiModelProperty(value = "名称")
    private String name;
}
