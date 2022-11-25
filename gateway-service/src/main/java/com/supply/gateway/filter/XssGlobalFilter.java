package com.supply.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.supply.common.exception.ApiException;
import com.supply.gateway.properties.XssProperties;
import com.supply.gateway.util.GatewayUtil;
import com.supply.gateway.util.ServletUtil;
import com.supply.gateway.util.XssCleanRuleUtil;
import io.netty.buffer.ByteBufAllocator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author wjd
 * @description
 * @date 2022-11-25
 */
@Component
public class XssGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(XssGlobalFilter.class);


    private final XssProperties xssProperties;

    public XssGlobalFilter(XssProperties xssProperties) {
        this.xssProperties = xssProperties;
    }


    // 自定义过滤器执行的顺序,数值越大越靠后执行,越小就越先执行
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.debug("----自定义防XSS攻击网关全局过滤器生效----");
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        HttpMethod method = serverHttpRequest.getMethod();
        String contentType = serverHttpRequest.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        URI uri = exchange.getRequest().getURI();
        String url = uri.getPath();
        // 如果未开启或者处于不需要验证的url则直接跳过
        if (!xssProperties.getEnabled() || GatewayUtil.matches(url, xssProperties.getExcludeUrls())) {
            return chain.filter(exchange);
        }
        //过滤get请求
        if (method == HttpMethod.GET) {
            String rawQuery = uri.getRawQuery();
            if (StrUtil.isBlank(rawQuery)) {
                return chain.filter(exchange);
            }
            logger.info("原请求参数为：{}", rawQuery);
            // 执行XSS清理
            rawQuery = XssCleanRuleUtil.xssGetClean(rawQuery);
            logger.info("修改后参数为：{}", rawQuery);
            // 如果存在sql注入,直接拦截请求
            if (StrUtil.equals(rawQuery, "forbid")) {
                final String message = StrUtil.format("请求{}{}参数中包含不允许sql的关键词, 请求拒绝", uri.getRawPath(), uri.getRawQuery());
                logger.error(message);
                return this.forbidResponse(exchange, message);
            }
            try {
                //重新构造get request
                URI newUri = UriComponentsBuilder.fromUri(uri)
                        .replaceQuery(rawQuery)
                        .build(true)
                        .toUri();

                ServerHttpRequest request = exchange.getRequest().mutate()
                        .uri(newUri).build();
                return chain.filter(exchange.mutate().request(request).build());
            } catch (Exception e) {
                final String message = StrUtil.format("get请求:{}清理xss攻击异常", rawQuery);
                logger.error(message, e);
                throw new ApiException(message, e);
            }
        }
        //post请求时,如果是文件上传之类的请求,不修改请求消息体
        boolean isPost = (method == HttpMethod.POST || method == HttpMethod.PUT) &&
                (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(contentType) || MediaType.APPLICATION_JSON_VALUE.equals(contentType));
        if (!isPost) {
            return chain.filter(exchange);
        }
        return DataBufferUtils.join(serverHttpRequest.getBody()).flatMap(d -> Mono.just(Optional.of(d))).defaultIfEmpty(
                        Optional.empty())
                .flatMap(optional -> {
                    // 取出body中的参数
                    String bodyString = "";
                    if (optional.isPresent()) {
                        byte[] oldBytes = new byte[optional.get().readableByteCount()];
                        optional.get().read(oldBytes);
                        bodyString = new String(oldBytes, StandardCharsets.UTF_8);
                    }
                    HttpHeaders httpHeaders = serverHttpRequest.getHeaders();
                    // 执行XSS清理
                    logger.info("{} - {} XSS处理前参数：{}", method, uri.getPath(), bodyString);
                    bodyString = XssCleanRuleUtil.xssPostClean(bodyString);
                    logger.info("{} - {} XSS处理后参数：{}", method, uri.getPath(), bodyString);
                    //  如果存在sql注入,直接拦截请求
                    if (bodyString.contains("forbid")) {
                        final String message = StrUtil.format("{} - {} 参数：{}, 包含不允许sql的关键词，请求拒绝", method, uri.getPath(), bodyString);
                        logger.error(message);
                        return this.forbidResponse(exchange, message);
                    }
                    ServerHttpRequest newRequest = serverHttpRequest.mutate().uri(uri).build();
                    // 重新构造body
                    byte[] newBytes = bodyString.getBytes(StandardCharsets.UTF_8);
                    DataBuffer bodyDataBuffer = this.toDataBuffer(newBytes);
                    final Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);

                    // 重新构造header
                    HttpHeaders headers = new HttpHeaders();
                    headers.putAll(httpHeaders);
                    // 由于修改了传递参数,需要重新设置CONTENT_LENGTH,长度是字节长度,不是字符串长度
                    int length = newBytes.length;
                    headers.remove(HttpHeaders.CONTENT_LENGTH);
                    headers.setContentLength(length);
                    headers.set(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf8");
                    // 重写ServerHttpRequestDecorator,修改了body和header,重写getBody和getHeaders方法
                    newRequest = new ServerHttpRequestDecorator(newRequest) {
                        @NotNull
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return bodyFlux;
                        }

                        @NotNull
                        @Override
                        public HttpHeaders getHeaders() {
                            return headers;
                        }
                    };
                    return chain.filter(exchange.mutate().request(newRequest).build());
                });
    }


    private Mono<Void> forbidResponse(ServerWebExchange exchange, String msg) {
        logger.error("[网关防XSS攻击]---请求路径:{}", exchange.getRequest().getPath());
        return ServletUtil.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.FORBIDDEN);
    }

    /**
     * 字节数组转DataBuffer
     * @param bytes 字节数组
     * @return DataBuffer
     */
    private DataBuffer toDataBuffer(byte[] bytes) {
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

}
