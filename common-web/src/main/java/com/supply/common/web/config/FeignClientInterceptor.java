package com.supply.common.web.config;

import cn.hutool.core.util.StrUtil;
import com.supply.common.constant.Constant;
import com.supply.common.web.util.ContextUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

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
        final Long tenantId = ContextUtil.getCurrentTenantId();
        if (null != tenantId) {
            requestTemplate.header(Constant.TENANT_ID_KEY, tenantId.toString());
        }
        final String currentUserId = ContextUtil.getCurrentUserId();
        if (StrUtil.isNotBlank(currentUserId)) {
            requestTemplate.header(Constant.USER_ID_KEY, currentUserId);
        }
    }
}
