package com.supply.system.model.request;

import com.supply.common.web.model.BaseRequestEntity;
import com.supply.system.model.po.DictCategoryPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 字典分类信息请求实体.
 * @date 2022-08-29
 */
@ApiModel(value="字典分类信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DictCategoryRequest extends BaseRequestEntity<DictCategoryPo> implements Serializable {
    private static final long serialVersionUID = 4568483595637866722L;


    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty(value = "字典分类名称")
    private String name;

    @ApiModelProperty(value = "字典分类编码")
    private String code;

    @ApiModelProperty(value = "字典分类备注")
    private String description;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "是否含有子级")
    private Boolean hasChildren;

    @ApiModelProperty(value = "不包含的主键ID")
    private Long neId;

    @ApiModelProperty(value = "字典分类名称模糊查询")
    private String likeName;
}
