package com.supply.message.service;

import com.supply.message.model.request.SendMessageRequest;

/**
 * @author wjd
 * @description
 * @date 2022-10-13
 */
public interface ISendMessageService {

    /**
      * @description 发送消息.
      * @author wjd
      * @date 2022/11/1
      * @param request 消息请求内容
      */
    void sendMessage(SendMessageRequest request);
}
