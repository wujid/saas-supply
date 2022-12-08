package com.supply.system.cvt;

import com.supply.system.model.po.UserThirdPo;
import com.supply.system.model.request.UserThirdRequest;
import com.supply.system.model.response.UserThirdResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-08
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserThirdCvt {
    UserThirdCvt INSTANCE = Mappers.getMapper(UserThirdCvt.class);

    /**
     * @author wjd
     * @description request实体转换成po实体.
     * @date 2022/12/8
     * @param request 待转换的
     * @return 转换后的po实体
     **/
    UserThirdPo requestToPo(UserThirdRequest request);

    /**
      * @description po实体转换成response实体.
      * @author wjd
      * @date 2022/12/8
      * @param po 待转换的po实体
      * @return 转换后的response实体
      */
    UserThirdResponse poToResponse(UserThirdPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/12/8
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<UserThirdResponse> poToResponseBatch(List<UserThirdPo> list);
}
