package com.supply.auth.service.impl;

import com.supply.auth.service.TokenService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author wjd
 * @description
 * @date 2022-07-29
 */
public class TokenServiceImpl implements TokenService {

    private final TokenStore tokenStore;

    public TokenServiceImpl(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public void t() {
        final OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken("1");
        oAuth2AccessToken.getTokenType();
    }
}
