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
 * @description 用户数据权限范围信息数据库映射实体.
 * @date 2022-09-05
 */
@ApiModel(value="用户数据权限范围信息数据库映射实体")
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_data_scope_range")
public class DataScopeRangePo extends Model<DataScopeRangePo> implements Serializable {
    private static final long serialVersionUID = -5546168079414619193L;


    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户资源数据权限类型ID")
    private Long dataScopeTypeId;

    @ApiModelProperty(value = "数据权限范围")
    private Integer dataScopeRange;

    @ApiModelProperty(value = "数据权限ID")
    private Long dataScopeId;

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
