package com.supply.system.cvt;

import com.supply.system.model.po.TenantResourcePo;
import com.supply.system.model.request.TenantResourceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author wjd
 * @description
 * @date 2022-08-09
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantResourceCvt {
    TenantResourceCvt INSTANCE = Mappers.getMapper(TenantResourceCvt.class);

    /**
     * @author wjd
     * @description request对象转换成po对象.
     * @date 2022/8/9
     * @param request 待转换的
     * @return 转换后的po对象
     **/
    TenantResourcePo requestToPo(TenantResourceRequest request);

}
