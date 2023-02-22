package com.supply.system.cvt;

import com.supply.system.model.po.UserPo;
import com.supply.system.model.request.UserRequest;
import com.supply.system.model.response.UserResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-07
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserCvt {
    UserCvt INSTANCE = Mappers.getMapper(UserCvt.class);

    /**
     * @author wjd
     * @description request对象转换成po对象.
     * @date 2022/7/31
     * @param request 待转换的
     * @return 转换后的po对象
     **/
    UserPo requestToPo(UserRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/7/31
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    @Named("poToResponse")
    UserResponse poToResponse(UserPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/7/31
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    @IterableMapping(qualifiedByName = "poToResponse")
    List<UserResponse> poToResponseBatch(List<UserPo> list);
}
