package com.supply.bpm.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description .
 * @date 2023-06-25
 */
@ApiModel(value="流程任务响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TaskResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -2871582615535724612L;

    @ApiModelProperty(value = "流程运行表主键ID")
    private Long runId;

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "流程执行实例ID")
    private String executionId;

    @ApiModelProperty(value = "流程运行实例ID")
    private String instanceId;

    @ApiModelProperty(value = "流程定义ID")
    private String definitionId;

    @ApiModelProperty(value = "流程定义key")
    private String processKey;

    @ApiModelProperty(value = "流程定义名称")
    private String processName;

    @ApiModelProperty(value = "流程节点key")
    private String nodeId;

    @ApiModelProperty(value = "流程节点名称")
    private String nodeName;

    @ApiModelProperty(value = "流程业务ID")
    private String businessId;

    @ApiModelProperty(value = "流程业务标题")
    private String businessTitle;

    @ApiModelProperty(value = "流程发起人ID")
    private Long startUserId;

    @ApiModelProperty(value = "流程发起人名称")
    private String startUserName;

    @ApiModelProperty(value = "流程分类ID")
    private String categoryId;

    @ApiModelProperty(value = "流程分类名称")
    private String categoryName;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "流程节点名称集")
    private String nodeNames;

    @ApiModelProperty(value = "流程节点审批人ID集")
    private String nodeUserIds;

}
