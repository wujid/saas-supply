package com.supply.bpm.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * @author wjd
 * @description 流程任务处理请求实体.
 * @date 2023-06-27
 */
@ApiModel(value="流程任务处理请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TaskHandleRequest implements Serializable {
    private static final long serialVersionUID = 6670774159525858087L;

    @ApiModelProperty(value = "流程节点设置主键ID")
    private Long nodeSetId;

    @ApiModelProperty(value = "流程任务ID")
    private String taskId;

    @ApiModelProperty(value = "审批方式")
    private Integer buttonType;

    @ApiModelProperty(value = "流程节点ID对应选择人员")
    private Map<Long, Long> nodeUserMap;

    @ApiModelProperty(value = "审批人ID")
    private Long assigneeId;

    @ApiModelProperty(value = "审批意见")
    private String opinion;
}
