package com.supply.bpm.cvt;

import com.supply.bpm.model.po.ActModelPo;
import com.supply.bpm.model.request.ActModelRequest;
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
}
