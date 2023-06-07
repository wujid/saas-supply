package com.supply.bpm.cvt;

import com.supply.bpm.model.po.UserNodePo;
import com.supply.bpm.model.request.UserNodeRequest;
import com.supply.bpm.model.response.UserNodeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description 流程节点实体转换.
 * @date 2023-01-04
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserNodeCvt {
    UserNodeCvt INSTANCE = Mappers.getMapper(UserNodeCvt.class);

    /**
     * @description 将request实体转换成po实体.
     * @author wjd
     * @date 2022/10/9
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    UserNodePo requestToPo(UserNodeRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    UserNodeResponse poToResponse(UserNodePo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<UserNodeResponse> poToResponseBatch(List<UserNodePo> list);
}
