package com.supply.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.supply.auth.constant.AuthConstant;
import com.supply.auth.cvt.ClientDetailCvt;
import com.supply.auth.model.request.ClientDetailRequest;
import com.supply.auth.service.OauthService;
import com.supply.common.constant.GrantTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author wjd
 * @description
 * @date 2022-07-27
 */
@Service
public class OauthServiceImpl implements OauthService {
    private static final Logger logger = LoggerFactory.getLogger(OauthServiceImpl.class);

    private final JdbcClientDetailsService jdbcClientDetailsService;

    public OauthServiceImpl(JdbcClientDetailsService jdbcClientDetailsService) {
        this.jdbcClientDetailsService = jdbcClientDetailsService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveClientDetails(ClientDetailRequest request) {
        logger.info("[客户端信息保存]---待保存的客户端实体{}", JSON.toJSONString(request));
        // 如果资源未传入则使用默认
        if (CollUtil.isEmpty(request.getResourceIds())) {
            request.setResourceIds(AuthConstant.DEFAULT_RESOURCE_IDS);
        }
        // 如果权限范围未传入则使用默认
        if (CollUtil.isEmpty(request.getScope())) {
            request.setScope(AuthConstant.DEFAULT_SCOPE);
        }
        // 如果未传入验证方式则使用所有验证方式
        if (CollUtil.isEmpty(request.getAuthorizedGrantTypes())) {
            final Map<String, String> enumMap = GrantTypeEnum.getEnumMap();
            request.setAuthorizedGrantTypes(enumMap.keySet());
        }
        // 如果重定向地址未传入则使用默认
        if (StrUtil.isBlank(request.getRedirectUri())) {
            request.setRedirectUri(AuthConstant.DEFAULT_WEB_SERVER_REDIRECT_URI);
        }
        BaseClientDetails baseClientDetails = ClientDetailCvt.INSTANCE.requestToPo(request);
        jdbcClientDetailsService.addClientDetails(baseClientDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClientDetails(ClientDetailRequest request) {
        BaseClientDetails baseClientDetails = ClientDetailCvt.INSTANCE.requestToPo(request);
        jdbcClientDetailsService.updateClientDetails(baseClientDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeClientDetails(String clientId) {
        jdbcClientDetailsService.removeClientDetails(clientId);
    }
}
