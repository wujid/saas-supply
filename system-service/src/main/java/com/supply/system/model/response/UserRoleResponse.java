package com.supply.system.model.response;

import com.supply.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 用户角色关联关系信息响应实体.
 * @date 2022-07-08
 */
@ApiModel(value="用户角色关联关系信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserRoleResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -3320519963690967545L;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;
}
