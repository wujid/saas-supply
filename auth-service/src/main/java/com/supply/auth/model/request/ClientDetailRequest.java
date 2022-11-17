package com.supply.auth.model.request;

import com.baomidou.mybatisplus.annotation.TableName;
import com.supply.common.annotation.Note;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-07-27
 */
@TableName("bpm_business_variables")
@Note(description="oauth客户端详情请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class ClientDetailRequest implements Serializable {
    private static final long serialVersionUID = 6918333714496485999L;

    private String clientId;

    private String clientSecret;

    private Set<String> scope;

    private Set<String> resourceIds;

    private Set<String> authorizedGrantTypes;

    private String redirectUri;

    private Boolean autoApprove;

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    private Map<String, Object> additionalInformation;
}
