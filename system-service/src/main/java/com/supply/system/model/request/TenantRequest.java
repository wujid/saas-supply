package com.supply.system.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.supply.common.model.BaseRequestEntity;
import com.supply.system.model.po.TenantPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 租户信息请求实体.
 * @date 2022-07-08
 */
@ApiModel(value="租户信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TenantRequest extends BaseRequestEntity<TenantPo> implements Serializable {
    private static final long serialVersionUID = -6737358973410698386L;


    @ApiModelProperty(value = "租户编号")
    private String code;

    @ApiModelProperty(value = "租户名称")
    private String name;

    @ApiModelProperty(value = "租户类型")
    private Integer type;

    @ApiModelProperty(value = "授权开始时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "授权天数")
    private Integer days;

    @ApiModelProperty(value = "授权结束时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "客户端ID")
    private String clientId;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "修改时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "租户名称模糊查询")
    private String likeName;

    @ApiModelProperty(value = "到期开始时间")
    private String endTimeStart;

    @ApiModelProperty(value = "到期结束时间")
    private String endTimeEnd;
}
