package com.supply.system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.common.web.model.BaseRequestEntity;
import com.supply.system.model.po.UserRolePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author wjd
 * @description 用户角色关联关系信息请求实体.
 * @date 2022-07-08
 */
@ApiModel(value="用户角色关联关系信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserRoleRequest extends BaseRequestEntity<UserRolePo> implements Serializable {
    private static final long serialVersionUID = -7722348518713529497L;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "用户ID集")
    private Set<Long> userIds;

    @ApiModelProperty(value = "角色ID集")
    private Set<Long> roleIds;
}
