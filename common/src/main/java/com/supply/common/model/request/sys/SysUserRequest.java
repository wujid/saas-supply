package com.supply.common.model.request.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.common.annotation.Note;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author wjd
 * @description 用户信息请求实体.
 * @date 2022-07-07
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SysUserRequest implements Serializable {
    private static final long serialVersionUID = -3777622238599656825L;

    @Note(description = "ID")
    private Long id;

    @Note(description = "工号")
    private String workNumber;

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

    @Note(description = "是否管理员")
    private Boolean isManage;

    @Note(description = "租户ID")
    private Long tenantId;

    @Note(description = "创建人")
    private Long createUserId;

    @Note(description = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @Note(description = "修改人")
    private Long updateUserId;

    @Note(description = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

    @Note(description = "状态")
    private Integer status;

    @Note(description = "租户ID集")
    private Set<Long> tenantIds;

    @Note(description = "用户ID集")
    private Set<Long> userIds;
}
