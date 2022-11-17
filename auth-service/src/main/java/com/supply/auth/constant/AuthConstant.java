package com.supply.auth.constant;

import cn.hutool.core.convert.Convert;

import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-07-20
 */
public class AuthConstant {

    /**客户端ID**/
    public static final String CLIENT_ID = "client_id";

    /**客户端密钥**/
    public static final String CLIENT_SECRET = "client_secret";

    /**授权模式**/
    public static final String GRANT_TYPE = "grant_type";

    /**登录用户**/
    public static final String USER_NAME = "username";

    /**登录密码**/
    public static final String PASSWORD = "password";

    /**jwt密钥**/
    public static final String SIGNING_KEY = "jwt123";

    /**默认客户端资源**/
    public static final Set<String> DEFAULT_RESOURCE_IDS = Convert.toSet(String.class, "all");

    /**默认客户端权限**/
    public static final Set<String> DEFAULT_SCOPE = Convert.toSet(String.class, "all");

    /**默认客户端重定向地址**/
    public static final String DEFAULT_WEB_SERVER_REDIRECT_URI = "http://localhost:8090/login.html";
}
