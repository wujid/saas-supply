package com.supply.bpm.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 流程运行实例信息响应实体.
 * @date 2023-06-20
 */
@ApiModel(value="流程运行实例信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class ProcessRunResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8107781431842052605L;

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

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
