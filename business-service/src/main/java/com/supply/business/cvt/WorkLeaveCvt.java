package com.supply.business.cvt;

import com.supply.business.model.po.WorkLeavePo;
import com.supply.business.model.request.WorkLeaveRequest;
import com.supply.business.model.response.WorkLeaveResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-06-19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkLeaveCvt {

    WorkLeaveCvt INSTANCE = Mappers.getMapper(WorkLeaveCvt.class);

    /**
     * @description 将request实体转换成po实体
     * @author wjd
     * @date 2022/8/29
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    WorkLeavePo requestToPo(WorkLeaveRequest request);

    /**
     * @description 将response实体转换成po实体
     * @author wjd
     * @date 2022/8/29
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    WorkLeaveResponse poToResponse(WorkLeavePo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/9/7
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<WorkLeaveResponse> poToResponseBatch(List<WorkLeavePo> list);

}
