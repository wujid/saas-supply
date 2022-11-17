package com.supply.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.gateway.model.LoginUser;
import com.supply.gateway.properties.IgnoreWhiteProperties;
import com.supply.gateway.task.LoginUserTask;
import com.supply.gateway.util.GatewayUtil;
import com.supply.gateway.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/**
 * @author wjd
 * @description 网关鉴权.
 * @date 2022-07-14
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);


    private final IgnoreWhiteProperties ignoreWhiteProperties;


    private final Executor customerThreadPool;

    public AuthFilter(IgnoreWhiteProperties ignoreWhiteProperties, Executor customerExecutor) {
        this.ignoreWhiteProperties = ignoreWhiteProperties;
        this.customerThreadPool = customerExecutor;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        // 当前请求路径
        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (GatewayUtil.matches(url, ignoreWhiteProperties.getWhites())) {
            return chain.filter(exchange);
        }
        // 获取token
        final HttpHeaders headers = request.getHeaders();
        final String token = headers.getFirst("token");
        if (StrUtil.isBlank(token)) {
            final String message = "token不能为空";
            logger.error(message);
            return unauthorizedResponse(exchange, message);
        }

        // 根据token获取登录用户信息
        Result<LoginUser> loginUserResult = null;
        try {
            LoginUserTask task = new LoginUserTask(token);
            FutureTask<Result<LoginUser>> futureTask = new FutureTask<>(task);
            customerThreadPool.execute(futureTask);
            loginUserResult = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            final String message = "token已过期请重新登录";
            logger.error(message, e);
            return unauthorizedResponse(exchange, message);
        }
        if (loginUserResult.getCode() != HttpStatus.OK.value() || null == loginUserResult || null == loginUserResult.getData()) {
            logger.info("根据token{}鉴权失败,鉴权结果为{}", token, JSON.toJSONString(loginUserResult));
            return unauthorizedResponse(exchange, "token已过期或验证不正确!");
        }
        // 设置用户信息到请求体
        final LoginUser loginUser = loginUserResult.getData();
        addHeader(mutate, Constant.USER_ID_KEY, loginUser.getUserId());
        addHeader(mutate, Constant.TENANT_ID_KEY, loginUser.getTenantId());
        addHeader(mutate, Constant.ACCOUNT_ID_KEY, loginUser.getUsername());

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return -200;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        logger.error("[网关鉴权]---请求路径:{}", exchange.getRequest().getPath());
        return ServletUtil.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtil.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }
}
