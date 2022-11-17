package com.supply.system.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supply.common.model.BaseRequestEntity;
import com.supply.system.model.po.RolePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author wjd
 * @description 角色信息请求实体.
 * @date 2022-07-27
 */
@ApiModel(value="角色信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRequest extends BaseRequestEntity<RolePo> implements Serializable {
    private static final long serialVersionUID = -7625926896743879787L;


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

    @ApiModelProperty(value = "不等于ID")
    private Long neId;

    @ApiModelProperty(value = "资源ID集")
    private List<Long> resourceIdList;

    @ApiModelProperty(value = "角色ID集")
    private Set<Long> roleIds;

    @ApiModelProperty(value = "不包含角色ID集")
    private Set<Long> notInRoleIds;
}
