package com.supply.system.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author wjd
 * @description 租户信息请求实体.
 * @date 2022-08-09
 */
@ApiModel(value="租户信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantSaveRequest implements Serializable {
    private static final long serialVersionUID = 5984727851606125761L;

    @ApiModelProperty(value = "租户信息")
    private TenantRequest tenant;

    @ApiModelProperty(value = "管理员信息")
    private UserRequest user;

    @ApiModelProperty(value = "租户所拥有的资源ID集")
    private List<Long> resourceIdList;

    @ApiModelProperty(value = "租户所属组织信息")
    private OrgRequest org;

    @ApiModelProperty(value = "角色信息集")
    private List<RoleRequest> roleList;

}
