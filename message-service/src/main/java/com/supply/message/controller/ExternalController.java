package com.supply.message.controller;

import com.supply.common.model.Result;
import com.supply.message.model.request.SendMessageRequest;
import com.supply.message.service.ISendMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description 外部服务调用.
 * @date 2022-11-01
 */
@Api(tags = "外部服务调用")
@RestController
@RequestMapping("/external")
public class ExternalController {

    private final ISendMessageService sendMessageService;

    public ExternalController(ISendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }


    @ApiOperation(value = "发送消息")
    @PostMapping("/sendMessage")
    public Result<Object> sendMessage(@RequestBody SendMessageRequest request) {
        sendMessageService.sendMessage(request);
        return Result.ok();
    }
}
