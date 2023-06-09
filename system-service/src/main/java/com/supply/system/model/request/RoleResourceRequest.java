package com.supply.system.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.common.web.model.BaseRequestEntity;
import com.supply.system.model.po.RoleResourcePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author wjd
 * @description 角色资源关联关系信息请求实体.
 * @date 2022-07-08
 */
@ApiModel(value="角色资源关联关系信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class RoleResourceRequest extends BaseRequestEntity<RoleResourcePo> implements Serializable {
    private static final long serialVersionUID = -4568527466180066153L;


    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "资源ID")
    private Long resourceId;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "角色ID集")
    private Set<Long> roleIds;

    @ApiModelProperty(value = "资源ID集")
    private Set<Long> resourceIds;
}
