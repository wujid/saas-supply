package com.supply.system.model.request;

import com.supply.common.model.BaseRequestEntity;
import com.supply.system.model.po.ResourcePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author wjd
 * @description 资源信息请求实体.
 * @date 2022-07-27
 */
@ApiModel(value="资源信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class ResourceRequest extends BaseRequestEntity<ResourcePo> implements Serializable {


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

    @ApiModelProperty(value = "不等于ID")
    private Long neId;

    @ApiModelProperty(value = "ID集")
    private Set<Long> resourceIds;
}
