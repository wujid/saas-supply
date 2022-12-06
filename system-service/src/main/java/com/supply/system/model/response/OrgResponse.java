package com.supply.system.model.response;


import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author wjd
 * @description 组织信息响应实体.
 * @date 2022-08-09
 */
@ApiModel(value="组织信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class OrgResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6119982185993811328L;

    @ApiModelProperty(value = "组织机构编码")
    private String code;

    @ApiModelProperty(value = "组织机构类型")
    private String orgType;

    @ApiModelProperty(value = "组织名称")
    private String name;

    @ApiModelProperty(value = "组织地址")
    private String address;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否主要")
    private Boolean isMain;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "父ID")
    private Long parentId;

    @ApiModelProperty(value = "父名称")
    private String parentName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "子菜单信息")
    private List<OrgResponse> childrenList;
}
