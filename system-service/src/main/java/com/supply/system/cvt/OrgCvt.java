package com.supply.system.cvt;

import com.supply.system.model.po.OrgPo;
import com.supply.system.model.request.OrgRequest;
import com.supply.system.model.response.OrgResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-09
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrgCvt {
    OrgCvt INSTANCE = Mappers.getMapper(OrgCvt.class);

    /**
     * @description 将request实体转换成po实体
     * @author wjd
     * @date 2022/8/9
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    OrgPo requestToPo(OrgRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/8/10
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    OrgResponse poToResponse(OrgPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/8/12
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<OrgResponse> poToResponseBatch(List<OrgPo> list);
}
