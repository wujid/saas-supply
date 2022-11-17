package com.supply.message.constant;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 消息类型枚举.
 * @date 2022-10-29
 */
@Getter
@AllArgsConstructor
public enum MsgTypeEnum {


    OWNER(1, "个人"),

    PUBLISH(2, "公告");

    @ApiModelProperty(value = "类型")
    private int type;

    @ApiModelProperty(value = "名称")
    private String name;
}
