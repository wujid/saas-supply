package com.supply.message.model.po;

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
 * @description 消息模板信息数据库映射实体.
 * @date 2022-10-09
 */
@ApiModel(value="消息模板信息数据库映射实体")
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("msg_template_info")
public class TemplateInfoPo extends Model<TemplateInfoPo> {
    private static final long serialVersionUID = -4282897516522148214L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "模板编码")
    private String code;

    @ApiModelProperty(value = "模板标题")
    private String title;

    @ApiModelProperty(value = "详情URL")
    private String detailUrl;

    @ApiModelProperty(value = "消息类型")
    private Integer msgType;

    @ApiModelProperty(value = "公共消息通知类型")
    private Integer publishMsgNotifyType;

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
}
