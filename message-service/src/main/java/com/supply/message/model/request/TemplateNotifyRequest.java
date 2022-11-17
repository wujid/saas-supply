package com.supply.message.model.request;

import com.supply.common.model.BaseRequestEntity;
import com.supply.message.model.po.TemplateNotifyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 消息通知方式信息请求实体.
 * @date 2022-10-29
 */
@ApiModel(value="消息通知方式信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TemplateNotifyRequest extends BaseRequestEntity<TemplateNotifyPo> implements Serializable {
    private static final long serialVersionUID = -5787714412422465885L;

    @ApiModelProperty(value = "模板ID")
    private Long templateId;

    @ApiModelProperty(value = "通知方式")
    private Integer notifyType;

    @ApiModelProperty(value = "通知模板内容")
    private String notifyTemplate;
}
