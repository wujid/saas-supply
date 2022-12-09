package com.supply.common.constant;

/**
 * @author wjd
 * @description
 * @date 2022-07-06
 */
public class Constant {
    // 状态-未删除
    public static final int STATUS_NOT_DEL = 0;
    // 状态-已删除
    public static final int STATUS_DEL = 1;

    // 登录方式-账户密码登录
    public static final int LOGIN_TYPE_PWD = 1;

    // 登录方式-第三方登录
    public static final int LOGIN_TYPE_THIRD = 2;

    // 用户ID标识
    public static final String USER_ID_KEY = "userId";

    // 租户ID标识
    public static final String TENANT_ID_KEY = "tenantId";

    // 账号标识
    public static final String ACCOUNT_ID_KEY = "accountId";

    // 当前登录用户key值格式
    public static final String CURRENT_USER = "currentUser_%s";

    // 默认密码
    public static final String DEFAULT_PASSWORD = "123456";
}
