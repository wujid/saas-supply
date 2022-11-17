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
 * @description 白名单配置.
 * @date 2022-07-14
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "gateway.ignore")
public class IgnoreWhiteProperties {

    @Note(description = "白名单")
    private List<String> whites = new ArrayList<>();
}
