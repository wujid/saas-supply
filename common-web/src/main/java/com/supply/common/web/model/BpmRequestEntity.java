package com.supply.common.web.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
    private String definitionId;

    @ApiModelProperty(value = "流程节点对应选择人员")
    private List<Map<Long, Long>> nodeUsers;
}
