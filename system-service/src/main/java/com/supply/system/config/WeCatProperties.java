package com.supply.system.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author wjd
 * @description 第三方平台-微信相关属性配置.
 * @date 2022-07-14
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "system.third.weCat")
public class WeCatProperties {

    @ApiModelProperty(value = "应用唯一标识")
    private String appId;

    @ApiModelProperty(value = "应用密钥")
    private String appSecret;

    @ApiModelProperty(value = "应用跳转路径")
    private String redirectUrl;
}
