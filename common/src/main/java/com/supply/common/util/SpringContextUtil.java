package com.supply.common.util;

import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author wjd
 * @description spring相关工具类.
 * @date 2022-05-13
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
      * @description 通过名称获取bean.
      * @author wjd
      * @date 2022/5/13
      * @param name bean名称
      * @return 获取到的bean
      */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
      * @description 通过类获取到对应的bean.
      * @author wjd
      * @date 2022/5/13
      * @param clazz 待获取的bean类
      * @return 当前bean
      */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
      * @description 通过名称 + 类获取到bean
      * @author wjd
      * @date 2022/5/13
      * @param name bean名称
      * @param clazz bean类
      * @return 获取到的bean
      */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
      * @description 通过名称判断是否存在该bean.
      * @author wjd
      * @date 2022/5/13
      * @param name bean名称
      * @return boolean 是否存在
      */
    public static boolean isContainsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
      * @description 通过名称 + 类获取到feign上的bean.
      * @author wjd
      * @date 2022/5/24
      * @param beanName bean名称
      * @param clazz bean类
      * @return 获取到的bean
      */
    public static <T> T getFeignBean(String beanName, Class<T> clazz) {
        FeignContext feignContext = applicationContext.getBean("feignContext", FeignContext.class);
        return feignContext.getInstance(beanName, clazz);
    }
}
