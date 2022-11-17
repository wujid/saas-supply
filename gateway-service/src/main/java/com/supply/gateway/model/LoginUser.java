package com.supply.gateway.model;

import com.supply.common.annotation.Note;
import lombok.Data;

/**
 * @author wjd
 * @description 登录用户基本信息.
 * @date 2022-07-11
 */
@Data
public class LoginUser {

    @Note(description = "用户ID")
    private Long userId;

    @Note(description = "租户ID")
    private Long tenantId;

    @Note(description = "账号")
    private String username;

    @Note(description = "密码")
    private String password;

}
