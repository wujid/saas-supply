package com.supply.system.cvt;

import com.supply.system.model.po.DictCategoryPo;
import com.supply.system.model.request.DictCategoryRequest;
import com.supply.system.model.response.DictCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-11-08
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictCategoryCvt {
    DictCategoryCvt INSTANCE = Mappers.getMapper(DictCategoryCvt.class);

    /**
     * @description 将request实体转换成po实体
     * @author wjd
     * @date 2022/11/8
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    DictCategoryPo requestToPo(DictCategoryRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/11/8
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    DictCategoryResponse poToResponse(DictCategoryPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/11/8
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<DictCategoryResponse> poToResponseBatch(List<DictCategoryPo> list);
}
