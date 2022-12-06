package com.supply.system.model.response;


import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wjd
 * @description 资源信息响应实体.
 * @date 2022-07-27
 */
@ApiModel(value="资源信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class ResourceResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 931369536454141558L;

    @ApiModelProperty(value = "父ID")
    private Long parentId;

    @ApiModelProperty(value = "资源编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "路由")
    private String path;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "资源类型 1:菜单 2:按钮")
    private Integer type;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "是否固定")
    private Boolean isFix;

    @ApiModelProperty(value = "是否选中")
    private Boolean isChecked;

    @ApiModelProperty(value = "子菜单信息")
    private List<ResourceResponse> childrenList = new ArrayList<>();

    @ApiModelProperty(value = "该菜单下的按钮信息集")
    private List<ResourceResponse> buttonResponseList = new ArrayList<>();
}
