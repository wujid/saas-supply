package com.supply.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wjd
 * @description 登录方式.
 * @date 2022-07-22
 */
@Getter
@AllArgsConstructor
public enum GrantTypeEnum {

    AUTHORIZATION_CODE("authorization_code", "验证码"),

    PASSWORD("password", "密码"),

    CLIENT_CREDENTIALS("client_credentials", "客户端"),

    IMPLICIT("implicit", ""),

    REFRESH_TOKEN("refresh_token", "刷新token");

    private final String code;

    private final String description;

    /**
      * @description 获取枚举值映射关系.
      * @author wjd
      * @date 2022/7/27
      * @return 映射结果
      */
    public static Map<String, String> getEnumMap() {
        Map<String, String> map = new HashMap<>();
        for (GrantTypeEnum typeEnum : GrantTypeEnum.values()) {
            map.put(typeEnum.getCode(), typeEnum.getDescription());
        }
        return map;
    }
}
