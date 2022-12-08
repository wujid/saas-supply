package com.supply.system.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "租户编码不能为空")
    private String tenantCode;

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String userName;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "登录成功跳转地址")
    private String redirect;

    @ApiModelProperty(value = "唯一值")
    private String key;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String captcha;
}
