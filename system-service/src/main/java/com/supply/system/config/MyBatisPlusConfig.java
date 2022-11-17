//package com.supply.system.config;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
//import org.springframework.context.annotation.Configuration;
//
//
///**
// * @author wjd
// * @description
// * @date 2022-07-08
// */
//@Configuration
//public class MyBatisPlusConfig {
//
//    private final SysTenantHandler sysTenantHandler;
//
//    public MyBatisPlusConfig(SysTenantHandler sysTenantHandler) {
//        this.sysTenantHandler = sysTenantHandler;
//    }
//
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        // 多租户插件
//        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(sysTenantHandler));
//        // 分页插件（ps：如果项目中有用到分页插件可以添加如下这行代码，但是必须要写在多租户插件后面）
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        return interceptor;
//    }
//
//}
