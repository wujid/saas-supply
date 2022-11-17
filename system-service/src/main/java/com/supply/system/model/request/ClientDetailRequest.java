package com.supply.system.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author wjd
 * @description oauth客户端详情请求实体.
 * @date 2022-07-27
 */
@ApiModel(value="oauth客户端详情请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class ClientDetailRequest implements Serializable {
    private static final long serialVersionUID = 6204711460508667036L;


    private String clientId;

    private String clientSecret;

    private Set<String> scope = Collections.emptySet();

    private Set<String> resourceIds = Collections.emptySet();

    private Set<String> authorizedGrantTypes = Collections.emptySet();

    private String redirectUri;

    private Boolean autoApprove;

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    private Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();
}
