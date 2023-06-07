package com.supply.bpm.model.request;

import com.supply.bpm.model.po.ProcessDefinitionPo;
import com.supply.common.web.model.BaseRequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
@ApiModel(value="流程定义信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class ProcessDefinitionRequest extends BaseRequestEntity<ProcessDefinitionPo> implements Serializable {
    private static final long serialVersionUID = -2839224806437704551L;

    @ApiModelProperty(value = "流程分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "流程部署ID")
    private String deploymentId;

    @ApiModelProperty(value = "流程定义ID")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程名称")
    private String processName;

    @ApiModelProperty(value = "流程标识名称")
    private String processKey;

    @ApiModelProperty(value = "是否默认流程")
    private Boolean isDefault;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "流程组ID")
    private Long groupId;

    @ApiModelProperty(value = "在流程组中是否生效")
    private Boolean isGroupUse;

    @ApiModelProperty(value = "表单URL")
    private String formUrl;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "流程xml")
    private String bpmnXml;
}
