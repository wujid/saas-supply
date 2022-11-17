package com.supply.system.cvt;

import com.supply.system.model.po.UserRolePo;
import com.supply.system.model.request.UserRoleRequest;
import com.supply.system.model.response.UserRoleResponse;
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
public interface UserRoleCvt {
    UserRoleCvt INSTANCE = Mappers.getMapper(UserRoleCvt.class);


    /**
     * @author wjd
     * @description request对象转换成po对象.
     * @date 2022/9/30
     * @param request 待转换的
     * @return 转换后的po对象
     **/
    UserRolePo requestToPo(UserRoleRequest request);


    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/9/28
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<UserRoleResponse> poToResponseBatch(List<UserRolePo> list);
}
