package com.supply.system.cvt;

import com.supply.system.model.po.DataScopeRangePo;
import com.supply.system.model.request.DataScopeRangeRequest;
import com.supply.system.model.response.DataScopeRangeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-07
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataScopeRangeCvt {
    DataScopeRangeCvt INSTANCE = Mappers.getMapper(DataScopeRangeCvt.class);



    /**
     * @description 批量将request实体转换成po实体.
     * @author wjd
     * @date 2022/9/7
     * @param list 待转换的request实体集
     * @return 转换后的po实体集
     */
    List<DataScopeRangePo> requestToPoBatch(List<DataScopeRangeRequest> list);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/9/7
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<DataScopeRangeResponse> poToResponseBatch(List<DataScopeRangePo> list);
}
