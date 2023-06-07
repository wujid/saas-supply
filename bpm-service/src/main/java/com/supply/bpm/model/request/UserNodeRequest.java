package com.supply.bpm.model.request;

import com.supply.bpm.model.po.UserNodePo;
import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 流程节点信息请求实体.
 * @date 2023-01-04
 */
@ApiModel(value="流程节点信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserNodeRequest extends BaseRequestEntity<UserNodePo> implements Serializable {
    private static final long serialVersionUID = -3651383738958307082L;

    @ApiModelProperty(value = "流程定义ID")
    private String definitionId;

    @ApiModelProperty(value = "节点ID")
    private String nodeId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "用户节点类型--1:个人任务  2:候选人任务 3:组任务")
    private Integer nodeType;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
