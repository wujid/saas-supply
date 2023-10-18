package com.supply.system.model.response;


import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author wjd
 * @description 用户信息响应实体.
 * @date 2022-07-07
 */
@ApiModel(value="用户信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7406662071340201559L;

    @ApiModelProperty(value = "工号")
    private String workNumber;

    @ApiModelProperty(value = "登录账号")
    private String account;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "头像")
    private Long avatarId;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "组织id")
    private Long orgId;

    @ApiModelProperty(value = "部门id")
    private Long departId;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "是否管理员")
    private Boolean isManage;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "角色ID集")
    private Set<Long> roleIds;

    @ApiModelProperty(value = "部门名称")
    private String departName;
}
