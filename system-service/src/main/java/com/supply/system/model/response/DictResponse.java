package com.supply.system.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wjd
 * @description 字典类型信息响应实体.
 * @date 2022-08-29
 */
@ApiModel(value="字典类型信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DictResponse extends BaseEntity {
    private static final long serialVersionUID = 3997095818170209256L;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "字典备注")
    private String description;
}
