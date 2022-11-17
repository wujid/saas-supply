package com.supply.system.service;

import com.supply.common.model.response.auth.AuthTokenResponse;
import com.supply.system.model.request.LoginRequest;
import com.supply.system.model.response.UserInfoResponse;

/**
 * @author wjd
 * @description
 * @date 2022-08-11
 */
public interface ILoginService {

    /**
     * @description 客户端密码模式登录系统.
     * @author wjd
     * @date 2022/7/31
     * @param request 登录请求信息
     * @return token信息
     */
    AuthTokenResponse login(LoginRequest request);

    /**
     * @description 退出
     * @author wjd
     * @date 2022/8/3
     * @param userId 待退出的用户ID
     */
    void logout(Long userId);

    /**
      * @description 生成随机验证码.
      * @author wjd
      * @date 2022/8/11
      * @param key 唯一标识
      */
    void generateCaptcha(String key);

    /**
     * @description 根据用户ID获取用户详细信息.
     * @author wjd
     * @date 2022/7/28
     * @param userId 用户ID
     * @return 用户详细信息
     */
    UserInfoResponse getUserInfo(Long userId);
}
