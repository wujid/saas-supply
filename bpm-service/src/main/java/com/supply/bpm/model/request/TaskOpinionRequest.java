package com.supply.bpm.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.bpm.model.po.TaskOpinionPo;
import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 流程审批意见信息请求实体.
 * @date 2023-07-04
 */
@ApiModel(value="流程审批意见信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TaskOpinionRequest extends BaseRequestEntity<TaskOpinionPo> implements Serializable {
    private static final long serialVersionUID = -293081538375167390L;

    @ApiModelProperty(value = "流程运行实例id")
    private String instanceId;

    @ApiModelProperty(value = "流程任务id")
    private String taskId;

    @ApiModelProperty(value = "流程审批节点id")
    private String nodeId;

    @ApiModelProperty(value = "流程审批节点名称")
    private String nodeName;

    @ApiModelProperty(value = "流程当前节点任务所属人id---交办/委托原处理人")
    private Long ownerUserId;

    @ApiModelProperty(value = "流程当前节点任务审批人")
    private Long assigneeUserId;

    @ApiModelProperty(value = "审批状态")
    private Integer checkStatus;

    @ApiModelProperty(value = "审批意见")
    private String opinion;

    @ApiModelProperty(value = "审批结束时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "审批持续时间(毫秒)")
    private Long duration;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
