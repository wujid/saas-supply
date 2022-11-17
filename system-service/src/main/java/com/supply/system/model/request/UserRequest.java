package com.supply.system.model.request;

import com.supply.common.model.BaseRequestEntity;
import com.supply.system.model.po.UserPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author wjd
 * @description 用户信息请求实体.
 * @date 2022-07-07
 */
@ApiModel(value="用户信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserRequest extends BaseRequestEntity<UserPo> implements Serializable {
    private static final long serialVersionUID = -241617171310573603L;

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

    @ApiModelProperty(value = "用户ID集")
    private Set<Long> userIds;

    @ApiModelProperty(value = "不包含的ID")
    private Long neId;

    @ApiModelProperty(value = "用户名称模糊匹配")
    private String likeName;

    @ApiModelProperty(value = "手机号模糊匹配")
    private String likeTelephone;

    @ApiModelProperty(value = "账号模糊匹配")
    private String likeAccount;

    @ApiModelProperty(value = "邮箱模糊匹配")
    private String likeEmail;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "租户ID集")
    private Set<Long> tenantIds;

    @ApiModelProperty(value = "部门ID集")
    private Set<Long> deptIds;
}
