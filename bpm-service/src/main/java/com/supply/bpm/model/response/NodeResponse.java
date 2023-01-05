package com.supply.bpm.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 流程节点信息响应实体.
 * @date 2023-01-04
 */
@ApiModel(value="流程节点信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class NodeResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2865616781541573939L;

    @ApiModelProperty(value = "流程定义ID")
    private String definitionId;

    @ApiModelProperty(value = "节点ID")
    private String nodeId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
