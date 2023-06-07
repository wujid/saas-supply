package com.supply.bpm.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author wjd
 * @description 流程分类信息响应实体.
 * @date 2022-12-20
 */
@ApiModel(value="流程分类信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class CategoryResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7356638476463078279L;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty(value = "唯一编码")
    private String code;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "序号")
    private Integer sort;

    @ApiModelProperty(value = "父名称")
    private String parentName;

    @ApiModelProperty(value = "流程分类子级信息")
    private List<CategoryResponse> childrenList;

}
