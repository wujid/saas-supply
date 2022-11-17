package com.supply.common.model.response.auth;

import com.supply.common.annotation.Note;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @author wjd
 * @description Token信息响应实体.
 * @date 2022-07-22
 */
@Data
@Builder
public class AuthTokenResponse {

    @Note(description = "访问令牌")
    private String token;

    @Note(description = "刷新令牌")
    private String refreshToken;

    @Note(description = "令牌类型")
    private String tokenType;

    @Note(description = "有效时间(秒)")
    private int expiresIn;

    @Note(description = "权限作用域")
    private Set<String> scope;

    @Note(description = "租户编码")
    private String tenantCode;
}
