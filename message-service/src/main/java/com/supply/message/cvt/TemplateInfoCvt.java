package com.supply.message.cvt;

import com.supply.message.model.po.TemplateInfoPo;
import com.supply.message.model.request.TemplateInfoRequest;
import com.supply.message.model.response.TemplateInfoResponse;
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
public interface TemplateInfoCvt {
    TemplateInfoCvt INSTANCE = Mappers.getMapper(TemplateInfoCvt.class);

    /**
     * @description 将request实体转换成po实体.
     * @author wjd
     * @date 2022/10/9
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    TemplateInfoPo requestToPo(TemplateInfoRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    TemplateInfoResponse poToResponse(TemplateInfoPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<TemplateInfoResponse> poToResponseBatch(List<TemplateInfoPo> list);
}
