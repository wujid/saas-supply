package com.supply.business.model.response;

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
 * @description 请假申请.
 * @date 2023-06-19
 */
@ApiModel(value="请假申请")
@Data
@EqualsAndHashCode(callSuper=false)
public class WorkLeaveResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2155388658714812014L;

    @ApiModelProperty(value = "申请人ID")
    private Long applyUserId;

    @ApiModelProperty(value = "申请人名称")
    private String applyUserName;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "天数")
    private Integer days;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "业务唯一ID")
    private String businessId;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    
}
