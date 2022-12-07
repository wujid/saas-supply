package com.supply.gateway.config;

import com.supply.gateway.properties.TaskThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-07-14
 */
@Configuration
public class GatewayConfig {
    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    @Bean("customerExecutor")
    public Executor customerExecutor(TaskThreadPoolProperties config) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数量
        executor.setCorePoolSize(config.getCorePoolSize());
        // 配置最大线程数
        executor.setMaxPoolSize(config.getMaxPoolSize());
        // 配置队列容量
        executor.setQueueCapacity(config.getQueueCapacity());
        // 配置空闲线程存活时间
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        // 线程池前缀
        executor.setThreadNamePrefix(config.getThreadNamePrefix());
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 执行初始化
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

//    @Bean
//    public WebFilter corsFilter() {
//        return (ServerWebExchange ctx, WebFilterChain chain) -> {
//            ServerHttpRequest request = ctx.getRequest();
//            final boolean isTrue = CorsUtils.isCorsRequest(request);
//            logger.info("跨域过滤-------------是否是个跨域请求{}", isTrue);
//            if (isTrue) {
//                ServerHttpResponse response = ctx.getResponse();
//                HttpHeaders headers = response.getHeaders();
//                headers.add("Access-Control-Allow-Origin", "*");
//                headers.add("Access-Control-Allow-Methods", "*");
//                headers.add("Access-Control-Max-Age", "18000L");
//                headers.add("Access-Control-Allow-Headers", "*");
//                headers.add("Access-Control-Expose-Headers", "*");
//                headers.add("Access-Control-Allow-Credentials", "true");
//                if (request.getMethod() == HttpMethod.OPTIONS) {
//                    response.setStatusCode(HttpStatus.OK);
//                    return Mono.empty();
//                }
//            }
//            return chain.filter(ctx);
//        };
//    }

}
