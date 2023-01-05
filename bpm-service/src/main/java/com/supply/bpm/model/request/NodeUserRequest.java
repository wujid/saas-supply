package com.supply.bpm.model.request;

import com.supply.bpm.model.po.NodeUserPo;
import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 流程节点审批人员信息请求实体.
 * @date 2023-01-04
 */
@ApiModel(value="流程节点审批人员信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class NodeUserRequest extends BaseRequestEntity<NodeUserPo> implements Serializable {
    private static final long serialVersionUID = -3651383738958307082L;


    @ApiModelProperty(value = "节点ID")
    private String nodeId;

    @ApiModelProperty(value = "节点人员类型")
    private Integer nodeUserType;

    @ApiModelProperty(value = "关联ID")
    private Long relationId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
