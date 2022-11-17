package com.supply.system.model.request;

import com.supply.common.model.BaseRequestEntity;
import com.supply.system.model.po.DataScopeRangePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 用户数据权限范围信息请求实体.
 * @date 2022-09-05
 */
@ApiModel(value="用户数据权限范围信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DataScopeRangeRequest extends BaseRequestEntity<DataScopeRangePo> implements Serializable {
    private static final long serialVersionUID = 1651831924102203353L;


    @ApiModelProperty(value = "用户资源数据权限类型ID")
    private Long dataScopeTypeId;

    @ApiModelProperty(value = "数据权限范围")
    private Integer dataScopeRange;

    @ApiModelProperty(value = "数据权限ID")
    private Long dataScopeId;

}
