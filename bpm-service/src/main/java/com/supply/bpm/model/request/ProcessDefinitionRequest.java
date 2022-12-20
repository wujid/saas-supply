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

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "流程图片名称")
    private String diagramName;

    @ApiModelProperty(value = "xml文件名称")
    private String xmlName;

    @ApiModelProperty(value = "是否主流程")
    private Boolean isMain;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
