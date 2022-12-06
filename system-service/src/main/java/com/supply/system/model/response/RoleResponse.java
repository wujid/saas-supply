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
 * @description 角色信息响应实体.
 * @date 2022-07-27
 */
@ApiModel(value="角色信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class RoleResponse extends BaseEntity implements Serializable {


    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "是否系统管理角色")
    private Boolean isSystem;

    @ApiModelProperty(value = "是否主要角色")
    private Boolean isMain;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "菜单信息集")
    private List<ResourceResponse> menuResponseList;
}
