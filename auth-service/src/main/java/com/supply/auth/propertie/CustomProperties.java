package com.supply.auth.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wjd
 * @description
 * @date 2022-07-21
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth.jwt")
public class CustomProperties {

    private long expire;

    private String secret;

    private String header;

    private String privateKey;
}
