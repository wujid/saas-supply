package com.supply.bpm.cvt;

import com.supply.bpm.model.po.ActModelPo;
import com.supply.bpm.model.request.ActModelRequest;
import com.supply.bpm.model.response.ActModelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author wjd
 * @description .
 * @date 2022-12-19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActModelCvt {
    ActModelCvt INSTANCE = Mappers.getMapper(ActModelCvt.class);

    /**
     * @description 将request实体转换成po实体.
     * @author wjd
     * @date 2022/10/9
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    ActModelPo requestToPo(ActModelRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    ActModelResponse poToResponse(ActModelPo po);

}
