package com.supply.message.constant;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 公共消息通知类型枚举值(当消息类型为公共消息时,该消息为全系统通知还是指定某些租户通知)
 * @date 2022-10-31
 */
@Getter
@AllArgsConstructor
public enum PublishMsgNotifyTypeEnum {

    NOTIFY_SYSTEM(1, "系统所有成员"),

    NOTIFY_TENANT(2, "指定租户");

    @ApiModelProperty(value = "类型")
    private int type;

    @ApiModelProperty(value = "名称")
    private String name;
}
