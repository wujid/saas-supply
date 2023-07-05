package com.supply.bpm.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.bpm.model.po.ProcessRunPo;
import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 流程运行实例信息请求实体.
 * @date 2023-06-20
 */
@ApiModel(value="流程运行实例信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class ProcessRunRequest extends BaseRequestEntity<ProcessRunPo> implements Serializable {
    private static final long serialVersionUID = -5127373223884844818L;

    @ApiModelProperty(value = "流程定义ID")
    private String definitionId;

    @ApiModelProperty(value = "流程运行实例id")
    private String instanceId;

    @ApiModelProperty(value = "流程业务ID")
    private String businessId;

    @ApiModelProperty(value = "流程业务标题")
    private String businessTitle;

    @ApiModelProperty(value = "流程发起人ID")
    private Long startUserId;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "流程持续时间")
    private Long duration;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}

