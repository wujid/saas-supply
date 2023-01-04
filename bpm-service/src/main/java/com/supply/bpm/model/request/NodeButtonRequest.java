package com.supply.bpm.model.request;

import com.supply.bpm.model.po.NodeButtonPo;
import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 流程节点按钮信息请求实体.
 * @date 2023-01-04
 */
@ApiModel(value="流程节点按钮信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class NodeButtonRequest extends BaseRequestEntity<NodeButtonPo> implements Serializable {
    private static final long serialVersionUID = 8156328250791670697L;

    @ApiModelProperty(value = "流程节点ID")
    private String nodeId;

    @ApiModelProperty(value = "按钮类型")
    private Integer buttonType;

    @ApiModelProperty(value = "按钮名称")
    private String buttonName;

    @ApiModelProperty(value = "执行脚本")
    private String script;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
