//package com.supply.bpm.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Arrays;
//
///**
// * @author wjd
// * @description 权限设置虚拟用户权限.
// * @date 2023-06-28
// */
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final PasswordEncoder passwordEncoder;
//
//    public SecurityConfig(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                // 关闭csrf防护
//                .csrf().disable()
//                .headers().frameOptions().disable()
//                .and()
//                //定制url访问权限
//                .authorizeRequests()
//                //无需登录即可访问
//                .antMatchers("/**").permitAll()
//                .and();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return s -> new User("admin", passwordEncoder.encode("123456"),
//                Arrays.asList(new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"), new SimpleGrantedAuthority("GROUP_activitiTeam")));
//    }
//
//}
