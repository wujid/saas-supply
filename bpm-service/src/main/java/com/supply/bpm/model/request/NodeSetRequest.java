package com.supply.bpm.model.request;

import com.supply.bpm.model.po.NodeSetPo;
import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 流程节点设置信息请求实体.
 * @date 2023-01-04
 */
@ApiModel(value="流程节点设置信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class NodeSetRequest extends BaseRequestEntity<NodeSetPo> implements Serializable {
    private static final long serialVersionUID = -3651383738958307082L;

    @ApiModelProperty(value = "流程定义ID")
    private String definitionId;

    @ApiModelProperty(value = "节点ID")
    private String nodeId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "用户节点类型--1:开始节点 2:用户任务节点--个人任务 3:用户任务节点--候选人任务 4:用户任务节点--组任务 5:结束")
    private Integer nodeType;

    @ApiModelProperty(value = "表单URL")
    private String formUrl;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
