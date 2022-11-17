package com.supply.message.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-06-08
 */
@ApiModel(value="发送消息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString
public class SendMessageRequest implements Serializable {
    private static final long serialVersionUID = 7340243764170177211L;

    @ApiModelProperty(value = "消息模板编码")
    private String templateCode;

    @ApiModelProperty(value = "参数值")
    // key值为用户ID,用于不同用户不同参数,如果每个人消息参数一致则key值传入0
    private Map<Long, Map<String, Object>> paramsMap = new HashMap<>();

    @ApiModelProperty(value = "消息接收人")
    private Set<Long> receiverUserIds;
}
