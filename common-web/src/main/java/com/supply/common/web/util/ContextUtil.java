package com.supply.common.web.util;

import cn.hutool.core.util.StrUtil;
import com.supply.common.api.SystemClient;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.model.Result;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.util.ExceptionUtil;
import com.supply.common.util.RedisUtil;
import com.supply.common.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author wjd
 * @description 获取当前登录信息.
 * @date 2022-07-28
 */
public class ContextUtil {
    private static final Logger logger = LoggerFactory.getLogger(ContextUtil.class);

    // 当前登录用户缓存过期时间六小时
    private static final long CURRENT_USER_EXPIRE = 60 * 60 * 6L;

    /**
     * @description 获取登录用户信息.
     * @author wjd
     * @date 2022/7/28
     * @return 用户信息
     */
    public static SysUserResponse getCurrentUser() {
        // 从request中获取到当前登陆用户ID,未获取到则直接返回空
        final String currentUserId = getCurrentUserId();
        if (StrUtil.isBlank(currentUserId)) {
            logger.error("未从request中获取到用户ID信息!");
            return null;
        }
        // 从redis中获取数据
        final String redisKey = String.format(Constant.CURRENT_USER, currentUserId);
        final RedisUtil redisUtil = SpringContextUtil.getBean(RedisUtil.class);
        SysUserResponse userResponse = redisUtil.getCacheObject(redisKey);
        if (null == userResponse) {
            final SystemClient systemClient = SpringContextUtil.getFeignBean("user-service", SystemClient.class);
            final Result<SysUserResponse> result = systemClient.getUserById(Long.valueOf(currentUserId));
            ExceptionUtil.error(result.getCode() != HttpStatus.OK.value())
                    .errorMessage(null, result.getMessage());
            userResponse = result.getData();
            redisUtil.setCacheObject(redisKey, userResponse, ContextUtil.CURRENT_USER_EXPIRE, TimeUnit.SECONDS);
        }
        return userResponse;
    }

    public static Long getCurrentTenantId() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
        final String tenantId = request.getHeader(Constant.TENANT_ID_KEY);
        if (StrUtil.isNotBlank(tenantId)) {
            return Long.valueOf(tenantId);
        }
        throw new ApiException("通过上下文未获取到当前租户ID");
    }


    /**
     * @description 从request中获取到当前登陆系统用户ID
     * @author wjd
     * @date 2022/7/28
     * @return 当前登陆用户ID
     */
    public static String getCurrentUserId() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
        final String userId = request.getHeader(Constant.USER_ID_KEY);
        if (StrUtil.isNotBlank(userId)) {
            return userId;
        }
        return null;
    }
}
