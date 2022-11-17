package com.supply.system.cvt;

import com.supply.system.model.po.TenantPo;
import com.supply.system.model.request.TenantRequest;
import com.supply.system.model.response.TenantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantCvt {
    TenantCvt INSTANCE = Mappers.getMapper(TenantCvt.class);


    TenantPo requestToPo(TenantRequest request);


    TenantResponse poToResponse(TenantPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/7/31
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<TenantResponse> poToResponseBatch(List<TenantPo> list);


}
