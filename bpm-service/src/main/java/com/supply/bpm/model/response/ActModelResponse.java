package com.supply.bpm.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 流程模型信息响应实体.
 * @date 2022-12-19
 */
@ApiModel(value="流程模型信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class ActModelResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8572992760696819247L;

    @ApiModelProperty(value = "流程分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "流程模型ID")
    private String modelId;

    @ApiModelProperty(value = "流程模型key")
    private String modelKey;

    @ApiModelProperty(value = "流程模型名称")
    private String modelName;

    @ApiModelProperty(value = "流程备注")
    private String description;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;


}
