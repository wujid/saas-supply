package com.supply.message.model.response;

import com.supply.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 消息类型为公共消息且通知方式为指定租户时关联关系.
 * @date 2022-10-31
 */
@ApiModel(value="消息模板租户关联关系信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TemplateTenantResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -6253710454436630937L;

    @ApiModelProperty(value = "模板ID")
    private Long templateId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;


}
