package com.supply.bpm.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supply.bpm.model.po.CategoryPo;
import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 流程分类信息请求实体.
 * @date 2022-12-20
 */
@ApiModel(value="流程分类信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryRequest extends BaseRequestEntity<CategoryPo> implements Serializable {
    private static final long serialVersionUID = -1447317606024384248L;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty(value = "唯一编码")
    private String code;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "序号")
    private Integer sort;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

}
