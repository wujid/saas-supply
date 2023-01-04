package com.supply.bpm.model.request;

import com.supply.bpm.model.po.BusinessVariablePo;
import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description .
 * @date 2023-01-04
 */
@ApiModel(value="流程业务参数信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class BusinessVariableRequest extends BaseRequestEntity<BusinessVariablePo> implements Serializable {
    private static final long serialVersionUID = 8671208299531661635L;

    @ApiModelProperty(value = "流程定义ID")
    private String definitionId;

    @ApiModelProperty(value = "流程参数key值")
    private String variableKey;

    @ApiModelProperty(value = "流程参数名称")
    private String variableName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
