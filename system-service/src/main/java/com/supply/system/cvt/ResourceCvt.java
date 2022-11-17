package com.supply.system.cvt;

import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.request.ResourceRequest;
import com.supply.system.model.response.ResourceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceCvt {
    ResourceCvt INSTANCE = Mappers.getMapper(ResourceCvt.class);

    /**
     * @description 将request实体转换成po实体.
     * @author wjd
     * @date 2022/5/16
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    ResourcePo requestToPo(ResourceRequest request);

    /**
      * @description 将po实体转换成response实体.
      * @author wjd
      * @date 2022/9/6
      * @param po 待转换的po实体
      * @return 转换后的response实体
      */
    ResourceResponse poToResponse(ResourcePo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/5/16
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<ResourceResponse> poToResponseBatch(List<ResourcePo> list);
}
