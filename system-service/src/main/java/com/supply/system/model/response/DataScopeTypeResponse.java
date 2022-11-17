package com.supply.system.model.response;


import com.supply.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author wjd
 * @description 用户资源数据权限类型信息响应实体.
 * @date 2022-09-05
 */
@ApiModel(value="用户资源数据权限类型信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DataScopeTypeResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6577581074058148986L;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "资源菜单ID")
    private Long resourceId;

    @ApiModelProperty(value = "数据权限类型")
    private Integer dataScopeType;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "资源菜单名称")
    private String resourceName;

    @ApiModelProperty(value = "用户资源数据权限范围集")
    List<DataScopeRangeResponse> dataScopeRangeList;
}
