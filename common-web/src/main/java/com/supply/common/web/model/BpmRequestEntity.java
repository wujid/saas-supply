package com.supply.common.web.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wjd
 * @description 流程发起请求通用数据封装.
 * @date 2023-06-20
 */
@Data
@ApiModel(value="流程发起请求通用数据封装")
@EqualsAndHashCode(callSuper=false)
public class BpmRequestEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3753798563257257421L;

    @ApiModelProperty(value = "流程定义ID")
    private String bpmDefinitionId;

    @ApiModelProperty(value = "流程节点对应选择人员")
    private List<Map<Long, Long>> bpmNodeUsers;

    @ApiModelProperty(value = "流程发起人ID")
    private Long bpmStartUserId;

    @ApiModelProperty(value = "业务关联值,一般是流程关联的实体的主键值,当主键ID为自增时需自定义唯一值")
    private String bpmBusinessId;

    @ApiModelProperty(value = "流程所需业务参数")
    private Map<String, Object> bpmBusinessVariableMap = new HashMap<>();
}
