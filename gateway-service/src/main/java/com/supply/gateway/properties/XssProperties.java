package com.supply.gateway.properties;

import com.supply.common.annotation.Note;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wjd
 * @description XSS跨站脚本配置.
 * @date 2022-07-14
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "gateway.xss")
public class XssProperties {

    @Note(description = "是否开启")
    private Boolean enabled;

    @Note(description = "排除路径")
    private List<String> excludeUrls = new ArrayList<>();

}
