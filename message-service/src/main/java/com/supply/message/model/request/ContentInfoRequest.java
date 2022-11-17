package com.supply.message.model.request;

import com.supply.common.model.BaseRequestEntity;
import com.supply.message.model.po.ContentInfoPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 消息内容信息请求实体.
 * @date 2022-10-09
 */
@ApiModel(value="消息内容信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class ContentInfoRequest extends BaseRequestEntity<ContentInfoPo> implements Serializable {
    private static final long serialVersionUID = 8888470390471698761L;

    @ApiModelProperty(value = "模板ID")
    private Long templateId;

    @ApiModelProperty(value = "消息类型")
    private Integer msgType;

    @ApiModelProperty(value = "通知类型")
    private Integer notifyType;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "消息接收人")
    private Long receiverUserId;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "详情URL")
    private String detailUrl;
}
