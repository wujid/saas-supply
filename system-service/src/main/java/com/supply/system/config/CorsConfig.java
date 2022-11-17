package com.supply.system.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wjd
 * @description 跨域配置.
 * @date 2022-07-29
 */
@Component
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加映射路径，“/**”表示对所有的路径实行全局跨域访问权限的设置
        registry.addMapping("/**")
                //开放哪些ip、端口、域名的访问权限
                .allowedOriginPatterns("*")
                //是否允许发送Cookie信息
                .allowCredentials(true)
                //开放哪些Http方法，允许跨域访问
                .allowedMethods("GET","POST", "PUT", "DELETE")
                //允许HTTP请求中的携带哪些Header信息
                .allowedHeaders("*")
                //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
                .exposedHeaders("*");

    }
}
