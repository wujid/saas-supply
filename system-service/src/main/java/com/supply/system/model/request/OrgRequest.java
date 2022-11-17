package com.supply.system.model.request;

import com.supply.common.model.BaseRequestEntity;
import com.supply.system.model.po.OrgPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 组织信息请求实体.
 * @date 2022-08-09
 */
@ApiModel(value="组织信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class OrgRequest extends BaseRequestEntity<OrgPo> implements Serializable {
    private static final long serialVersionUID = 3169036604318417200L;

    @ApiModelProperty(value = "组织机构编码")
    private String code;

    @ApiModelProperty(value = "组织机构类型")
    private String orgType;

    @ApiModelProperty(value = "组织名称")
    private String name;

    @ApiModelProperty(value = "组织地址")
    private String address;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "是否主要")
    private Boolean isMain;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "父ID")
    private Long parentId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
