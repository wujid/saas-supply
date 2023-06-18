package com.supply.bpm.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 流程节点按钮信息响应实体.
 * @date 2023-01-04
 */
@ApiModel(value="流程节点按钮信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class NodeButtonResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -298169580192768910L;

    @ApiModelProperty(value = "流程节点设置主键ID")
    private Long nodeSetId;

    @ApiModelProperty(value = "按钮类型")
    private Integer buttonType;

    @ApiModelProperty(value = "按钮名称")
    private String buttonName;

    @ApiModelProperty(value = "执行脚本")
    private String script;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
