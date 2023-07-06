package com.supply.bpm.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 流程审批意见信息数据库映射实体.
 * @date 2023-07-04
 */
@ApiModel(value="流程审批意见信息数据库映射实体")
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("bpm_task_opinion")
public class TaskOpinionPo extends Model<ProcessRunPo> implements Serializable {
    private static final long serialVersionUID = -1431792797813794030L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "流程运行实例id")
    private String instanceId;

    @ApiModelProperty(value = "流程任务id")
    private String taskId;

    @ApiModelProperty(value = "流程审批节点id")
    private String nodeId;

    @ApiModelProperty(value = "流程审批节点名称")
    private String nodeName;

    @ApiModelProperty(value = "流程当前节点任务所属人id---交办/委托人")
    private Long ownerUserId;

    @ApiModelProperty(value = "流程当前节点任务审批人")
    private Long assigneeUserId;

    @ApiModelProperty(value = "审批状态")
    private Integer checkStatus;

    @ApiModelProperty(value = "审批意见")
    private String opinion;

    @ApiModelProperty(value = "审批结束时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "审批持续时间(毫秒)")
    private Long duration;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "状态")
    private Integer status;
}
