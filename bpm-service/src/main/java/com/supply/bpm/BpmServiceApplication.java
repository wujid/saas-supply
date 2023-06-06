package com.supply.bpm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.supply.**",
        exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@MapperScan(value = {"com.supply.bpm.mapper"})
@EnableFeignClients("com.supply.**.api")
public class BpmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpmServiceApplication.class, args);
    }

}
