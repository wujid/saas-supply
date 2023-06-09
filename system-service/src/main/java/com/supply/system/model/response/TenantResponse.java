package com.supply.system.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 租户信息响应实体.
 * @date 2022-07-08
 */
@ApiModel(value="租户信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TenantResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2898839521102884572L;

    @ApiModelProperty(value = "租户编号")
    private String code;

    @ApiModelProperty(value = "租户名称")
    private String name;

    @ApiModelProperty(value = "租户类型")
    private Integer type;

    @ApiModelProperty(value = "授权开始时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "授权天数")
    private Integer days;

    @ApiModelProperty(value = "授权结束时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "客户端ID")
    private String clientId;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;
}
