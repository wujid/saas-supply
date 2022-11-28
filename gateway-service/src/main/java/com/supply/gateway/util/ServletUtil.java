package com.supply.gateway.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.supply.common.model.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author wjd
 * @description 客户端工具类.
 * @date 2022-07-14
 */
public class ServletUtil {

    /**
      * @description 设置webflux模型响应.
      * @author wjd
      * @date 2022/7/14
      * @param response 响应体
      * @param value 响应内容
      * @param httpStatus 响应状态码
      * @return Mono<Void>
      */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value, HttpStatus httpStatus) {
        return webFluxResponse(response, value, httpStatus);
    }

    /**
      * @description 设置webflux模型响应.
      * @author wjd
      * @date 2022/7/14
      * @param response ServerHttpResponse
      * @param value 响应内容
      * @param httpStatus http状态码
      * @return Mono<Void>
      */
    public static Mono<Void> webFluxResponse(ServerHttpResponse response, Object value, HttpStatus httpStatus) {
        return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, value, httpStatus);
    }

    /**
      * @description 设置webflux模型响应.
      * @author wjd
      * @date 2022/7/14
      * @param response ServerHttpResponse
      * @param contentType content-type
      * @param value 响应内容
      * @param httpStatus http状态码
      * @return Mono<Void>
      */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, Object value, HttpStatus httpStatus) {
        response.setStatusCode(httpStatus);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        Result<?> result = Result.error(httpStatus.value(), value.toString());
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return StrUtil.EMPTY;
        }
    }
}
