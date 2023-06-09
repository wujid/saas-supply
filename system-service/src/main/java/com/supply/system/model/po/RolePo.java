package com.supply.system.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 角色信息数据库映射实体.
 * @date 2022-07-27
 */
@ApiModel(value="角色信息数据库映射实体")
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_role")
public class RolePo extends Model<RolePo> implements Serializable {
    private static final long serialVersionUID = -5978701426769462372L;


    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    /**
      * @description 管理员角色和系统角色区别:
      * 管理员角色为租户创建时的角色,定义了该租户所拥有的所有资源权限,一个租户有且仅有一个
      * 主要角色为某些场景下该租户一定存在的角色且不能删除冻结等操作的角色
      */
    @ApiModelProperty(value = "是否系统管理角色")
    private Boolean isSystem;

    @ApiModelProperty(value = "是否主要角色")
    private Boolean isMain;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

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
}
