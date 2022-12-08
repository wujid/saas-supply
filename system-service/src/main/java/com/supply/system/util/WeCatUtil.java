package com.supply.system.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.supply.common.exception.ApiException;
import com.supply.system.config.WeCatProperties;
import com.supply.system.constant.ThirdTypeEnum;
import com.supply.system.model.response.UserThirdResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author wjd
 * @description .
 * @date 2022-12-08
 */
@Component
public class WeCatUtil {
    private static final Logger logger = LoggerFactory.getLogger(WeCatUtil.class);

    private final RestTemplate restTemplate;

    private final WeCatProperties weCatProperties;

    public WeCatUtil(RestTemplate restTemplate, WeCatProperties weCatProperties) {
        this.restTemplate = restTemplate;
        this.weCatProperties = weCatProperties;
    }

    private static final String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={}&secret={}&code={}&grant_type=authorization_code";

    private static final String baseRefreshTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid={}&grant_type={}&refresh_token=REFRESH_TOKEN";

    private static final String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token={}&openid={}";

    /**
      * @description 根据授权码获取微信开放平台用户信息.
      * @author wjd
      * @date 2022/12/8
      * @param code 授权码
      * @return 开放平台用户信息
      */
    public UserThirdResponse getOpenInfoByCode(String code) {
        final String appId = weCatProperties.getAppId();
        final String appSecret = weCatProperties.getAppSecret();
        // 根据code获取accessToken
        String accessTokenUrl = StrUtil.format(baseAccessTokenUrl, appId, appSecret, code);
        final ResponseEntity<String> forEntity = restTemplate.getForEntity(accessTokenUrl, String.class);
        JSONObject resultJson = JSONObject.parseObject(forEntity.getBody());
        if(null == resultJson || resultJson.getString("errcode") != null){
            logger.error("获取access_token失败{}", resultJson);
            throw new ApiException("获取access_token失败");
        }
        String accessToken = resultJson.getString("access_token");
        String openId = resultJson.getString("openid");
        final String userInfoUrl = StrUtil.format(baseUserInfoUrl, accessToken, openId);
        final ResponseEntity<String> userInfoEntity = restTemplate.getForEntity(userInfoUrl, String.class);
        JSONObject userInfoJson = JSONObject.parseObject(userInfoEntity.getBody());
        if(null == userInfoJson || userInfoJson.getString("errcode") != null){
            logger.error("获取用户信息失败{}", userInfoJson);
            throw new ApiException("获取用户信息失败");
        }
        // 解析用户信息
        String nickname = userInfoJson.getString("nickname");
        String headImageUrl = userInfoJson.getString("headimgurl");
        UserThirdResponse userThird = new UserThirdResponse();
        userThird.setOpenId(openId);
        userThird.setThirdType(ThirdTypeEnum.WE_CAT.getType());
        userThird.setNickName(nickname);
        userThird.setHeadImageUrl(headImageUrl);
        return userThird;
    }
}
