package com.supply.auth.controller;

import com.supply.auth.constant.AuthConstant;
import com.supply.common.model.Result;
import com.supply.common.model.response.auth.AuthTokenResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wjd
 * @description 权限验证控制层.
 * @date 2022-07-22
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

    private final TokenEndpoint tokenEndpoint;

    public AuthController(TokenEndpoint tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    @PostMapping(value = "/token")
    public Result<AuthTokenResponse> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        AuthTokenResponse tokenResponse = AuthTokenResponse.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken() == null ? null : oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .scope(oAuth2AccessToken.getScope())
                .tokenType(oAuth2AccessToken.getTokenType())
                .build();

        return Result.ok(tokenResponse);
    }

    @PostMapping(value = "/login")
    public Result<AuthTokenResponse> login(Principal principal, @RequestParam Long tenantId, @RequestParam String tenantCode,
                                           @RequestParam String account, @RequestParam String password) throws HttpRequestMethodNotSupportedException {
        String username =  account + "&" + tenantId;
        Map<String, String> parameters = new HashMap<>();
        parameters.put(AuthConstant.CLIENT_ID, tenantId.toString());
        parameters.put(AuthConstant.CLIENT_SECRET, tenantCode);
        parameters.put(AuthConstant.USER_NAME, username);
        parameters.put(AuthConstant.PASSWORD, password);
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        AuthTokenResponse tokenResponse = AuthTokenResponse.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .scope(oAuth2AccessToken.getScope())
                .tokenType(oAuth2AccessToken.getTokenType())
                .build();

        return Result.ok(tokenResponse);
    }
}
