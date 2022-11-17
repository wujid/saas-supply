package com.supply.auth.config;

import com.supply.auth.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author wjd
 * @description 授权服务器配置
 * @date 2022-07-21
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Value("${auth.accessTokenValiditySeconds:7200}")
    private Integer accessTokenValiditySeconds;

    @Value("${auth.refreshTokenValiditySeconds:259200}")
    private Integer refreshTokenValiditySeconds;

    /**密码加密方式**/
    private final BCryptPasswordEncoder passwordEncoder;

    /**令牌存储方式**/
    private final TokenStore tokenStore;

    /**JWT令牌加密方式**/
    final
    JwtAccessTokenConverter accessTokenConverter;

    /**认证管理器*/
    private final AuthenticationManager authorizationManager;

    /**数据源**/
    private final DataSource dataSource;

    private final UserDetailsServiceImpl userDetailsService;

    public AuthorizationServer(BCryptPasswordEncoder passwordEncoder, TokenStore tokenStore,
                               JwtAccessTokenConverter accessTokenConverter, AuthenticationManager authorizationManager,
                               DataSource dataSource, UserDetailsServiceImpl userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
        this.authorizationManager = authorizationManager;
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
    }

    /**
      * @description 令牌端点的安全约束.
      * @author wjd
      * @date 2022/7/21
      * @param security 约束
      */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                // 将/oauth/token_key访问公开
                .tokenKeyAccess("permitAll()")
                // 将/oauth/check_token访问公开
                .checkTokenAccess("permitAll()")
                // 允许客户端的表单身份验证(申请令牌)
                .allowFormAuthenticationForClients();
    }

    /**
      * @description 配置客户端详细信息
      * clients的pathMapping()方法用来配置端点的URL链接
      * /oauth/authorize 授权端点
      * /oauth/token     令牌端点
      * /oauth/confirm_access  用户确认授权提交端点
      * /oauth/error     授权服务错误信息端点
      * /oauth/check_token 用于资源服务访问的令牌解析端点
      * /oauth/token_key  提供公有秘钥的端点,比如使用JWT令牌 采用RSA非对称加密方式
      * @author wjd
      * @date 2022/7/21
      * @param clients 客户端配置.
      */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 将客户端信息存储到数据库
        clients.withClientDetails(clientDetailsService(dataSource));
    }
    
    /**
      * @description 配置令牌的服务端点
      * @author wjd
      * @date 2022/7/21
      * @param endpoints 端点
      */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                //认证管理器
                .authenticationManager(authorizationManager)
                //授权码服务
                .authorizationCodeServices(authorizationCodeServices(dataSource))
//                .userDetailsService(userDetailsService)
                //令牌管理服务
                .tokenServices(tokenServices())
                //允许的令牌服务端点请求方法
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    /**
      * @description 配置令牌管理服务.
      * @author wjd
      * @date 2022/7/21
      */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // setClientDetailsService       客户端详情服务
        defaultTokenServices.setClientDetailsService(clientDetailsService(dataSource));
        // setSupportRefreshToken        支持刷新令牌
        defaultTokenServices.setSupportRefreshToken(true);
        // setTokenStore                 令牌存储策略
        defaultTokenServices.setTokenStore(tokenStore);

        // 设置令牌增强 采用JWT令牌的方式
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(List.of(accessTokenConverter));
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        // setAccessTokenValiditySeconds 令牌默认有效期 2小时 60 * 60 * 2
        defaultTokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        // setRefreshTokenValiditySeconds 刷新令牌默认有效期3天
        defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);

        return defaultTokenServices;
    }

    /**
      * @description 授权码基于数据库方式存储(对应表oauth_code)
      * @author wjd
      * @date 2022/7/21
      * @param dataSource 数据源
      * @return 授权码存储方式
      */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource){
        //采用JDBC数据库存储授权码(如果基于内存存储则使用new InMemoryAuthorizationCodeServices())
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
      * @description 客户端详情服务(对应表oauth_client_details)
      * @author wjd
      * @date 2022/7/21
      * @param dataSource 数据源
      * @return 客户端详情服务
      */
    @Bean
    public JdbcClientDetailsService clientDetailsService(DataSource dataSource){
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        // 使用jdbc情况下客户端密钥加密方式
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }



    /**
      * @description 基于内存方式配置客户端详细信息
      * @author wjd
      * @date 2022/7/21
      * @param clients 客户端配置.
      */
    private void saveInMemory(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //用来标识客户端的ID
                .withClient("c1")
                //客户端秘钥
                .secret(new BCryptPasswordEncoder().encode("order"))
                //资源列表
                .resourceIds("order")
                //该Client允许的授权类型
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")
                //允许的授权范围
                .scopes("all","order")
                // 自动批准 false 跳转到授权页面
                .autoApprove(false)
                //加上验证回调地址
                .redirectUris("http://www.baidu.com")
                //使用and()可以配置多个客户端
                .and()
                .withClient("c2")
                .secret(new BCryptPasswordEncoder().encode("account"))
                .resourceIds("account")
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")
                .scopes("all","account")
                .autoApprove(false)
                .redirectUris("http://www.baidu.com");
    }

}
