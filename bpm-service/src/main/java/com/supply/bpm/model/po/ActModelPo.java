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
 * @description 流程模型信息数据库映射实体.
 * @date 2022-12-19
 */
@ApiModel(value="流程模型信息数据库映射实体")
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("bpm_act_model")
public class ActModelPo extends Model<ActModelPo> {
    private static final long serialVersionUID = -8859249573471760262L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "流程模型ID")
    private String modelId;

    @ApiModelProperty(value = "流程模型key")
    private String modelKey;

    @ApiModelProperty(value = "流程模型名称")
    private String modelName;

    @ApiModelProperty(value = "流程备注")
    private String description;

    @ApiModelProperty(value = "版本号")
    private Integer version;

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
