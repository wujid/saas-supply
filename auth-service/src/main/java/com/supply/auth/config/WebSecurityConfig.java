package com.supply.auth.config;

import com.supply.auth.handle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        http.cors().and().csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/login/**", "/oauth/**", "/external/**", "/doc.html").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated() //   有请求都需要验证

                //登入
                .and().formLogin().
                permitAll()//允许所有用户
                .successHandler(loginSuccessHandler).//登录成功处理逻辑
                failureHandler(authenticationFailureHandler).//登录失败处理逻辑

                //登出
                and().logout().
                permitAll()//允许所有用户
                .logoutSuccessHandler(logoutSuccessHandler)//登出成功处理逻辑

                //异常处理(权限拒绝、登录失效等)
                .and()
                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)//匿名用户访问无权限资源时的异常处理--oauth下不能自定义,将导致授权码模式被拦截异常处理
                .accessDeniedHandler(customizeAccessDeniedHandler)
//
//                 //无状态session，不进行存储 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);
        //http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
