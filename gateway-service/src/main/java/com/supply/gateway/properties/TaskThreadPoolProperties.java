package com.supply.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author wjd
 * @description
 * @date 2022-07-14
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "gateway.thread.pool")
public class TaskThreadPoolProperties {

    private int corePoolSize;

    private int maxPoolSize;

    private int keepAliveSeconds;

    private int queueCapacity;

    private String threadNamePrefix;
}
