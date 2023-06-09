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

import java.util.Date;

/**
 * @author wjd
 * @description 组织机构信息数据库映射实体.
 * @date 2022-08-09
 */
@ApiModel(value="组织机构信息数据库映射实体")
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_org")
public class OrgPo extends Model<OrgPo> {
    private static final long serialVersionUID = 1802255826796670386L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "组织机构类型")
    private String orgType;

    @ApiModelProperty(value = "组织机构编码")
    private String code;

    @ApiModelProperty(value = "组织机构名称")
    private String name;

    @ApiModelProperty(value = "组织机构地址")
    private String address;

    @ApiModelProperty(value = "组织机构描述")
    private String description;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "是否主要")
    private Boolean isMain;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "父ID")
    private Long parentId;

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
