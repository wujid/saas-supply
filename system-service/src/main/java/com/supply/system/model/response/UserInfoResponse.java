package com.supply.system.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wjd
 * @description 用户详细信息响应实体.
 * @date 2022-07-27
 */
@ApiModel(value="用户详细信息响应实体")
@Data
public class UserInfoResponse implements Serializable {

    @ApiModelProperty(value = "用户信息")
    private UserResponse userResponse;

    @ApiModelProperty(value = "租户信息")
    private TenantResponse tenantResponse;

    @ApiModelProperty(value = "角色信息")
    private List<RoleResponse> roleResponseList;

    @ApiModelProperty(value = "资源菜单信息")
    private List<ResourceResponse> menuResponseList;

    @ApiModelProperty(value = "资源按钮信息")
    private List<ResourceResponse> buttonResponseList;
}
