package com.supply.system.cvt;

import com.supply.system.model.po.RolePo;
import com.supply.system.model.request.RoleRequest;
import com.supply.system.model.response.RoleResponse;
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
public interface RoleCvt {
    RoleCvt INSTANCE = Mappers.getMapper(RoleCvt.class);


    /**
     * @description 将request实体转换成po实体.
     * @author wjd
     * @date 2022/8/3
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    RolePo requestToPo(RoleRequest request);

    /**
     * @description 批量将request实体转换成po实体.
     * @author wjd
     * @date 2022/8/3
     * @param list 待转换的request实体集
     * @return 转换后的po实体集
     */
    List<RolePo> requestToPoBatch(List<RoleRequest> list);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/8/5
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    RoleResponse poToResponse(RolePo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/5/16
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<RoleResponse> poToResponseBatch(List<RolePo> list);



}
