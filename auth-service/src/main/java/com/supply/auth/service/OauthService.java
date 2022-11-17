package com.supply.auth.service;

import com.supply.auth.model.request.ClientDetailRequest;

/**
 * @author wjd
 * @description
 * @date 2022-07-27
 */
public interface OauthService {

    /**
      * @description 保存客户端详细信息.
      * @author wjd
      * @date 2022/7/27
      * @param request 待保存的消息体
      */
    void saveClientDetails(ClientDetailRequest request);

    void updateClientDetails(ClientDetailRequest request);

    void removeClientDetails(String clientId);
}
