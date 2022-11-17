package com.supply.system.cvt;

import com.supply.system.model.po.DataScopeTypePo;
import com.supply.system.model.request.DataScopeTypeRequest;
import com.supply.system.model.response.DataScopeTypeResponse;
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
public interface DataScopeTypeCvt {
    DataScopeTypeCvt INSTANCE = Mappers.getMapper(DataScopeTypeCvt.class);

    /**
     * @description 将request实体转换成po实体
     * @author wjd
     * @date 2022/9/7
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    DataScopeTypePo requestToPo(DataScopeTypeRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/9/7
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    DataScopeTypeResponse poToResponse(DataScopeTypePo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/9/7
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<DataScopeTypeResponse> poToResponseBatch(List<DataScopeTypePo> list);

}
