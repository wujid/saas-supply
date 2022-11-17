package com.supply.message.model.response;

import com.supply.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description
 * @date 2022-10-29
 */
@ApiModel(value="消息通知方式信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TemplateNotifyResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2177138489732747281L;

    @ApiModelProperty(value = "模板ID")
    private Long templateId;

    @ApiModelProperty(value = "通知方式")
    private Integer notifyType;

    @ApiModelProperty(value = "通知模板内容")
    private String notifyTemplate;
}
