package com.supply.bpm.model.request;

import com.supply.bpm.model.po.NodePo;
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
public class NodeRequest extends BaseRequestEntity<NodePo> implements Serializable {
    private static final long serialVersionUID = -3651383738958307082L;

    @ApiModelProperty(value = "流程定义ID")
    private String definitionId;

    @ApiModelProperty(value = "节点ID")
    private String nodeId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "节点名称模糊查询匹配")
    private String likeNodeName;
}
