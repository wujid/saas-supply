package com.supply.auth.config;

import com.supply.auth.constant.AuthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * @author wjd
 * @description
 * @date 2022-07-21
 */
@Configuration
public class TokenConfig {

    @Autowired
    private DataSource dataSource;

    /**
      * @description 使用JWT存储令牌进行对称加密.
      * @author wjd
      * @date 2022/7/21
      * @return
      */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        // JWT存储令牌加密方式
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 对称秘钥,资源服务器使用该秘钥进行验证
        jwtAccessTokenConverter.setSigningKey(AuthConstant.SIGNING_KEY);
        return jwtAccessTokenConverter;
    }

    /**
      * @description 令牌存储方式:使用JWT格式
      * @author wjd
      * @date 2022/7/21
      * @return 令牌存储方式
      */
    @Bean
    public TokenStore tokenStore() {
        //使用JWT存储令牌
//        return new JwtTokenStore(accessTokenConverter());
        return new JdbcTokenStore(dataSource);
    }


}
