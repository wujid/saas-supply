package com.supply.common.web.config;

import cn.hutool.core.util.StrUtil;
import com.supply.common.constant.Constant;
import com.supply.common.web.util.ContextUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author wjd
 * @description feign拦截.
 * @date 2023-06-21
 */
@Configuration
public class FeignClientInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 请求添加当前租户ID和用户ID
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
        final String tenantId = request.getHeader(Constant.TENANT_ID_KEY);
        if (StrUtil.isNotBlank(tenantId)) {
            requestTemplate.header(Constant.TENANT_ID_KEY, tenantId);
        }
        final String currentUserId = ContextUtil.getCurrentUserId();
        if (StrUtil.isNotBlank(currentUserId)) {
            requestTemplate.header(Constant.USER_ID_KEY, currentUserId);
        }
    }
}
