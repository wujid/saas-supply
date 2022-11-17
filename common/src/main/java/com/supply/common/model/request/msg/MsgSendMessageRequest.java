package com.supply.common.model.request.msg;

import com.supply.common.annotation.Note;
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
@Note(description="发送消息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString
public class MsgSendMessageRequest implements Serializable {
    private static final long serialVersionUID = 7014209289487809623L;


    @Note(description = "消息模板编码")
    private String templateCode;

    @Note(description = "参数值")
    private Map<String, Object> paramsMap = new HashMap<>();

    @Note(description = "消息接收人")
    private Set<Long> receiverUserIds;
}
