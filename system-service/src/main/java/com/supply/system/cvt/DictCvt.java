package com.supply.system.cvt;

import com.supply.system.model.po.DictPo;
import com.supply.system.model.request.DictRequest;
import com.supply.system.model.response.DictResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictCvt {
    DictCvt INSTANCE = Mappers.getMapper(DictCvt.class);

    /**
     * @description 将request实体转换成po实体
     * @author wjd
     * @date 2022/8/29
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    DictPo requestToPo(DictRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/8/29
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    DictResponse poToResponse(DictPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/8/29
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<DictResponse> poToResponseBatch(List<DictPo> list);
}
