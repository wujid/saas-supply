package com.supply.bpm.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author wjd
 * @description 流程节点审批人员信息数据库映射实体.
 * @date 2023-01-04
 */
@ApiModel(value="流程节点审批人员信息数据库映射实体")
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("bpm_node_user")
public class NodeUserPo extends Model<NodeUserPo> {
    private static final long serialVersionUID = 1278666864936003196L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "流程节点设置主键ID")
    private Long nodeSetId;

    @ApiModelProperty(value = "节点人员类型")
    private Integer nodeUserType;

    @ApiModelProperty(value = "关联ID")
    private Long relationId;

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
