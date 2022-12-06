package com.supply.system.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 字典分类信息响应实体.
 * @date 2022-08-29
 */
@ApiModel(value="字典分类信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DictCategoryResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3897218647307383836L;


    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "字典备注")
    private String description;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "是否含有子级")
    private Boolean hasChildren;
}
