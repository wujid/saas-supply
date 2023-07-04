package com.supply.file.model.po;

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
 * @description 附件信息数据库映射实体.
 * @date 2022-09-19
 */
@ApiModel(value="附件信息数据库映射实体")
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("file_attachment")
public class AttachmentPo extends Model<AttachmentPo> implements Serializable {
    private static final long serialVersionUID = -3687353216001243576L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "附件名称")
    private String name;

    @ApiModelProperty(value = "附件所属组")
    private String pathGroup;

    @ApiModelProperty(value = "附件地址")
    private String path;

    @ApiModelProperty(value = "附件拓展名")
    private String extensionName;

    @ApiModelProperty(value = "附件大小")
    private Long size;

    @ApiModelProperty(value = "业务ID")
    private Long businessId;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

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
