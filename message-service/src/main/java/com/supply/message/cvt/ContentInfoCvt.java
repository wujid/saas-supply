package com.supply.message.cvt;

import com.supply.message.model.po.ContentInfoPo;
import com.supply.message.model.response.ContentInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-10-09
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContentInfoCvt {
    ContentInfoCvt INSTANCE = Mappers.getMapper(ContentInfoCvt.class);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<ContentInfoResponse> poToResponseBatch(List<ContentInfoPo> list);
}
