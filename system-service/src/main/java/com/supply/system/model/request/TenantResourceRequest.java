package com.supply.system.model.request;

import com.supply.common.model.BaseRequestEntity;
import com.supply.system.model.po.TenantResourcePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 租户资源关联关系信息请求实体.
 * @date 2022-08-09
 */
@ApiModel(value="租户资源关联关系信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TenantResourceRequest extends BaseRequestEntity<TenantResourcePo> implements Serializable {
    private static final long serialVersionUID = -3390750306809334489L;


    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "资源ID")
    private Long resourceId;

}
