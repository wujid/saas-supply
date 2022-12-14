package com.supply.system.model.response;


import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 用户数据权限范围信息响应实体.
 * @date 2022-09-05
 */
@ApiModel(value="用户数据权限范围信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DataScopeRangeResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2880231603706732270L;


    @ApiModelProperty(value = "用户资源数据权限类型ID")
    private Long dataScopeTypeId;

    @ApiModelProperty(value = "数据权限范围")
    private Integer dataScopeRange;

    @ApiModelProperty(value = "数据权限ID")
    private Long dataScopeId;

    @ApiModelProperty(value = "数据权限对应的名称")
    private String name;
}
