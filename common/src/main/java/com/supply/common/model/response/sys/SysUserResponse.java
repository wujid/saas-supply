package com.supply.common.model.response.sys;


import com.supply.common.annotation.Note;
import com.supply.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 用户信息响应实体.
 * @date 2022-07-07
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SysUserResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7406662071340201559L;

    @Note(description = "登录账号")
    private String account;

    @Note(description = "登录密码")
    private String password;

    @Note(description = "手机号")
    private String telephone;

    @Note(description = "姓名")
    private String name;

    @Note(description = "性别")
    private Integer sex;

    @Note(description = "头像")
    private Long avatarId;

    @Note(description = "邮箱")
    private String email;

    @Note(description = "描述")
    private String description;

    @Note(description = "组织id")
    private Long orgId;

    @Note(description = "部门id")
    private Long departId;

    @Note(description = "业务状态")
    private Integer businessStatus;

    @Note(description = "是否管理员")
    private Boolean isManage;

    @Note(description = "租户ID")
    private Long tenantId;

    @Note(description = "租户编码")
    private String tenantCode;
}
