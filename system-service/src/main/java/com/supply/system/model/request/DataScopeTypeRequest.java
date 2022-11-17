package com.supply.system.model.request;

import com.supply.common.model.BaseRequestEntity;
import com.supply.system.model.po.DataScopeTypePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author wjd
 * @description 用户资源数据权限类型信息请求实体.
 * @date 2022-09-05
 */
@ApiModel(value="用户资源数据权限类型信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DataScopeTypeRequest extends BaseRequestEntity<DataScopeTypePo> implements Serializable {
    private static final long serialVersionUID = -3598022556748064146L;


    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "资源ID")
    private Long resourceId;

    @ApiModelProperty(value = "数据权限类型")
    private Integer dataScopeType;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "用户资源数据权限范围集")
    List<DataScopeRangeRequest> dataScopeRangeList;

}
