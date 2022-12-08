package com.supply.system.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 用户和第三方平台关联关系信息请求实体.
 * @date 2022-12-08
 */
@ApiModel(value="用户和第三方平台关联关系信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserThirdResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -2303178950808671082L;


    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "开放平台ID")
    private String openId;

    @ApiModelProperty(value = "开放平台用户昵称")
    private String nickName;

    @ApiModelProperty(value = "开放平台类型")
    private Integer thirdType;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "头像")
    private String headImageUrl;
}
