package com.supply.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description 通用bean注入.
 * @date 2022-07-31
 */
@Configuration
public class CommonConfig {

    /**
      * @description 统一密码加密.
      * @author wjd
      * @date 2022/8/31
      */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
