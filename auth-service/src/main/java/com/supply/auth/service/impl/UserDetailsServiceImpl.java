package com.supply.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.supply.auth.model.LoginUser;
import com.supply.auth.service.UserService;
import com.supply.common.api.SystemClient;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.constant.GrantTypeEnum;
import com.supply.common.exception.ApiException;
import com.supply.common.model.Result;
import com.supply.common.model.request.sys.SysTenantRequest;
import com.supply.common.model.request.sys.SysUserRequest;
import com.supply.common.model.response.sys.SysTenantResponse;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.util.SystemUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author wjd
 * @description 登录用户信息验证.
 * @date 2022-07-19
 */
@Service
public class UserDetailsServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    private final SystemClient systemClient;

    private final TokenStore tokenStore;

    private final SystemUserUtil systemUserUtil;

    private final BCryptPasswordEncoder passwordEncoder;

    private final HttpServletRequest request;

    public UserDetailsServiceImpl(SystemClient systemClient, TokenStore tokenStore,
                                  SystemUserUtil systemUserUtil, BCryptPasswordEncoder passwordEncoder,
                                  HttpServletRequest request) {
        this.systemClient = systemClient;
        this.tokenStore = tokenStore;
        this.systemUserUtil = systemUserUtil;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("[用户登录]---当前用户为{}", username);
        // 设置默认语言为中文
        LocaleContextHolder.resetLocaleContext();
        LocaleContextHolder.setDefaultLocale(Locale.CHINA);
        // 验证
        final SysUserResponse userResponse = this.getUser(username);
        return new LoginUser(userResponse);
    }

    @Override
    public LoginUser getLoginInfoByToken(String token) {
        final OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
        if (null == oAuth2Authentication) {
            final String message = StrUtil.format("令牌{}不存在或已过期", token);
            logger.error(message);
            throw new ApiException(message);
        }
        LoginUser loginUser = null;

        // 客户端模式:通过客户端密钥查询出该租户的管理员(客户端模式登录默认为该租户对应的管理员)
        if (StrUtil.equals(oAuth2Authentication.getOAuth2Request().getGrantType(), GrantTypeEnum.CLIENT_CREDENTIALS.getCode())) {
            final String clientId = oAuth2Authentication.getOAuth2Request().getClientId();
            final Result<SysUserResponse> result = systemClient.getManageUserByClientId(clientId);
            if (result.getCode() != HttpStatus.OK.value() || result.getData() == null) {
               throw new ApiException("认证失败!");
            }
            final SysUserResponse userResponse = result.getData();
            loginUser = new LoginUser(userResponse);
        }

        // 密码模式:直接获取缓存中的用户
        if (StrUtil.equals(oAuth2Authentication.getOAuth2Request().getGrantType(), GrantTypeEnum.PASSWORD.getCode())) {
            loginUser = (LoginUser) oAuth2Authentication.getUserAuthentication().getPrincipal();
        }
        return loginUser;
    }

    /**
     * @description 根据当前登录用户验证用户信息.
     * @author wjd
     * @date 2022/7/22
     * @param account 账号
     * @return 当前登录用户信息
     */
    private SysUserResponse getUser(String account) {
        // 根据&进行拆分出账号和租户ID

        final String loginType = request.getHeader("loginType");
        final String tenantId = request.getHeader("tenantId");
        final SysTenantResponse tenant = this.validateTenant(Long.valueOf(tenantId));

        // 根据账号和租户ID进行查询
        SysUserRequest userRequest = new SysUserRequest();
        userRequest.setAccount(account);
        userRequest.setTenantId(Long.valueOf(tenantId));
        userRequest.setStatus(Constant.STATUS_NOT_DEL);
        final SysUserResponse userResponse = systemUserUtil.getUserByParams(userRequest);
        // 用户不存在或者已经删除
        if (userResponse == null) {
            final String message = StrUtil.format("[用户登录]---该用户{}不存在", account);
            logger.error(message);
            throw new ApiException(message);
        }
        if (userResponse.getBusinessStatus() == BusinessStatusEnum.IN_FREEZE.getStatus()) {
            final String message = StrUtil.format("[用户登录]---用户{}已被冻结", account);
            logger.error(message);
            throw new ApiException(message);
        }
        // 租户编码
        userResponse.setTenantCode(tenant.getCode());
        // 如果登录方式为第三方登录将密码改为默认密码的加密方式
        if (null != loginType && Integer.parseInt(loginType) == Constant.LOGIN_TYPE_THIRD) {
            final String encodePassword = passwordEncoder.encode(Constant.DEFAULT_PASSWORD);
            userResponse.setPassword(encodePassword);
        }
        return userResponse;
    }

    /**
     * @description 租户验证.
     * @author wjd
     * @date 2022/11/4
     * @param tenantId 租户ID
     */
    private SysTenantResponse validateTenant(Long tenantId) {
        SysTenantRequest request = new SysTenantRequest();
        request.setId(tenantId);
        final SysTenantResponse tenant = systemUserUtil.getTenantByParams(request);
        if (null == tenant) {
            final String message = "该编码不存在!";
            throw new ApiException(message);
        }
        if (tenant.getBusinessStatus() == BusinessStatusEnum.IN_EXPIRE.getStatus()) {
            final String message = "该编码已过期!";
            throw new ApiException(message);
        }
        if (tenant.getBusinessStatus() == BusinessStatusEnum.IN_FREEZE.getStatus()) {
            final String message = "该编码已冻结!";
            throw new ApiException(message);
        }
        return tenant;
    }
}
