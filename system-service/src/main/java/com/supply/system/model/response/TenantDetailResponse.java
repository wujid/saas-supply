package com.supply.system.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author wjd
 * @description 租户信息响应实体.
 * @date 2022-08-09
 */
@ApiModel(value="租户信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TenantDetailResponse implements Serializable {
    private static final long serialVersionUID = 5984727851606125761L;

    @ApiModelProperty(value = "租户信息")
    private TenantResponse tenant;

    @ApiModelProperty(value = "管理员信息")
    private UserResponse user;

    @ApiModelProperty(value = "租户所属组织信息")
    private OrgResponse org;

    @ApiModelProperty(value = "菜单信息集")
    private List<ResourceResponse> menuResponseList;

    @ApiModelProperty(value = "角色信息集")
    private List<RoleResponse> roleList;

}
