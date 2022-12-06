package com.supply.system.model.response;


import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 租户资源关联关系信息响应实体.
 * @date 2022-08-09
 */
@ApiModel(value="租户资源关联关系信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class TenantResourceResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7567376584318428993L;


    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "资源ID")
    private Long resourceId;

}
