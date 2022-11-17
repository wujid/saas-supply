package com.supply.common.api;

import com.supply.common.annotation.Note;
import com.supply.common.model.Result;
import com.supply.common.model.request.msg.MsgSendMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wjd
 * @description
 * @date 2022-11-01
 */
@FeignClient("message-service")
public interface MessageClient {

    @Note(description = "发送消息:推荐异步调用")
    @PostMapping("external/sendMessage")
    Result<Object> sendMessage(@RequestBody MsgSendMessageRequest request);
}
