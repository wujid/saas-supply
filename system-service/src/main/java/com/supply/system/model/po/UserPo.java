package com.supply.system.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 用户信息数据库映射实体.
 * @date 2022-07-07
 */
@ApiModel(value="用户信息数据库映射实体")
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_user")
public class UserPo extends Model<UserPo> implements Serializable {
    private static final long serialVersionUID = 4822075380384435437L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "登录账号")
    private String account;

    @ApiModelProperty(value = "加密密码")
    private String password;

    @ApiModelProperty(value = "登录密码")
    private String encodePassword;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "头像Id")
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

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "修改时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "状态")
    private Integer status;
}
