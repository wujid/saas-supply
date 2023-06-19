package com.supply.bpm.model.response;

import com.supply.common.model.response.sys.SysUserResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 节点审批人信息响应实体.
 * @Author wjd
 * @Date 2021/12/29
 */
@ApiModel(value="节点审批人信息响应实体")
@Data
public class AssigneeResponse implements Serializable {
    private static final long serialVersionUID = -7372615609691206644L;

    @ApiModelProperty(value = "节点ID")
    private String id;

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "节点审批人")
    private String assignee;

    @ApiModelProperty(value = "审批节点候选人员")
    private List<SysUserResponse> userList;

    @ApiModelProperty(value = "选中的人员ID")
    private Long selectedUserId;

    @ApiModelProperty(value = "节点类型")
    private Integer nodeType;

    @ApiModelProperty(value = "组任务审批人员")
    private String groupUserIds;

    @ApiModelProperty(value = "流程实例id")
    private String instanceId;

}
