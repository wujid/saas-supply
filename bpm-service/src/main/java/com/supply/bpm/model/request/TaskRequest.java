package com.supply.bpm.model.request;

import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author wjd
 * @description .
 * @date 2023-06-25
 */
@ApiModel(value="流程任务请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TaskRequest extends BaseRequestEntity<Object> implements Serializable {
    private static final long serialVersionUID = -5974758069976525781L;

    @ApiModelProperty(value = "流程定义名称")
    private String processName;

    @ApiModelProperty(value = "流程业务标题")
    private String businessTitle;

    @ApiModelProperty(value = "流程发起人ID")
    private Long startUserId;

    @ApiModelProperty(value = "流程分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "审批人")
    private String assigneeId;

    @ApiModelProperty(value = "审批人所在组")
    private Set<String> assigneeGroups;
}
