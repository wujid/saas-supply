package com.supply.message.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author wjd
 * @description 消息模板信息响应实体.
 * @date 2022-10-09
 */
@ApiModel(value="消息模板信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TemplateInfoResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6568234839826257266L;


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

    @ApiModelProperty(value = "消息通知类型")
    private Set<Integer> notifyTypes;

    @ApiModelProperty(value = "通知内容模板(系统)")
    private String notifyTemplateSystem;

    @ApiModelProperty(value = "通知内容模板(邮件)")
    private String notifyTemplateEmail;

    @ApiModelProperty(value = "通知内容模板(短信)")
    private String notifyTemplateSms;

    @ApiModelProperty(value = "公共消息通知类型为指定租户时对应的租户ID集")
    private Set<Long> tenantIds;
}
