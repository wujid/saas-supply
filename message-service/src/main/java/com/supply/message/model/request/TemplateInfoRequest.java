package com.supply.message.model.request;

import com.supply.common.web.model.BaseRequestEntity;
import com.supply.message.model.po.TemplateInfoPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author wjd
 * @description 消息模板信息请求实体.
 * @date 2022-10-09
 */
@ApiModel(value="消息模板信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TemplateInfoRequest extends BaseRequestEntity<TemplateInfoPo> implements Serializable {
    private static final long serialVersionUID = 5571270333938734646L;


    @ApiModelProperty(value = "模板编码")
    private String code;

    @ApiModelProperty(value = "模板标题")
    private String title;

    @ApiModelProperty(value = "详情URL")
    private String detailUrl;

    @ApiModelProperty(value = "消息类型")
    private Integer msgType;

    @ApiModelProperty(value = "消息通知类型")
    private Set<Integer> notifyTypes;

    @ApiModelProperty(value = "公共消息通知类型")
    private Integer publishMsgNotifyType;

    @ApiModelProperty(value = "通知内容模板(系统)")
    private String notifyTemplateSystem;

    @ApiModelProperty(value = "通知内容模板(邮件)")
    private String notifyTemplateEmail;

    @ApiModelProperty(value = "通知内容模板(短信)")
    private String notifyTemplateSms;

    @ApiModelProperty(value = "不包含的主键ID")
    private Long neId;

    @ApiModelProperty(value = "公共消息通知类型为指定租户时对应的租户ID集")
    private Set<Long> tenantIds;



}
