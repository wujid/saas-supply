package com.supply.common.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.common.web.annotation.LongNotEmpty;
import com.supply.common.web.validate.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 公共基础实体信息.
 * @date 2022-05-12
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @LongNotEmpty(message = "主键ID不能为空", groups = UpdateGroup.class)
    private Long id;

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
