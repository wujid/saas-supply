package com.supply.system.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description
 * @date 2022-07-29
 */
@ApiModel(value="用户登录请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 1518352290594194744L;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "账号")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "登录成功跳转地址")
    private String redirect;

    @ApiModelProperty(value = "唯一值")
    private String key;

    @ApiModelProperty(value = "验证码")
    private String captcha;
}
