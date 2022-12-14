package com.supply.auth.config;

import com.supply.auth.handle.CustomizeAccessDeniedHandler;
import com.supply.auth.handle.CustomizeAuthenticationEntryPoint;
import com.supply.auth.handle.CustomizeAuthenticationFailureHandler;
import com.supply.auth.handle.CustomizeAuthenticationSuccessHandler;
import com.supply.auth.handle.CustomizeLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author wjd
 * @description
 * @date 2022-07-19
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 匿名用户访问无权限资源时的异常
     */
    @Autowired
    private CustomizeAuthenticationEntryPoint authenticationEntryPoint;
    /**
     * 登陆失败执行方法
     */
    @Autowired
    private CustomizeAuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 没有权限设置
     */
    @Autowired
    private CustomizeAccessDeniedHandler customizeAccessDeniedHandler;
    /**
     * 登出成功执行方法
     */
    @Autowired
    private CustomizeLogoutSuccessHandler logoutSuccessHandler;

    /**
     * 登录成功执行方法
     */
    @Autowired
    private CustomizeAuthenticationSuccessHandler loginSuccessHandler;


    /**
     * AuthenticationManager
     * <p>
     * 如果不声明，会导致授权服务器无AuthenticationManager，
     * 密码模式：而password方式无法获取token
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();
        http.authorizeRequests().antMatchers("/login/**", "/oauth/**", "/external/**", "/doc.html").access("permitAll");
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }

}
