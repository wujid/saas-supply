package com.supply.bpm.config;

import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wjd
 * @description .
 * @date 2023-01-04
 */
@Configuration
public class BpmConfig {

    @Bean
    public ProcessEngineConfigurationConfigurer processEngineConfigurationConfigurer(){
        return processEngineConfiguration -> {
            processEngineConfiguration.setActivityFontName("Microsoft YaHei");
            processEngineConfiguration.setAnnotationFontName("Microsoft YaHei");
            processEngineConfiguration.setLabelFontName("Microsoft YaHei");
        };
    }
}
