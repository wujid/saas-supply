package com.supply.common.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * @author wjd
 * @description 通用bean注入.
 * @date 2022-07-31
 */
@Configuration
public class CommonConfig {

    @Value("${snowflake.workId:1}")
    private Long workId;

    @Value("${snowflake.datacenterId:1}")
    private Long datacenterId;



    /**
      * @description 统一密码加密.
      * @author wjd
      * @date 2022/8/31
      */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
      * @description 雪花算法唯一ID值.
      * @author wjd
      * @date 2023/6/20
      */
    @Bean
    public Snowflake snowflake() {
        return IdUtil.createSnowflake(workId, datacenterId);
    }
}
