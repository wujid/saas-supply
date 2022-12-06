package com.supply.system.model.response;


import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 角色资源关联关系信息响应实体.
 * @date 2022-07-08
 */
@ApiModel(value="角色资源关联关系信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class RoleResourceResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -3721335971180501252L;


    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "资源ID")
    private Long resourceId;
}
