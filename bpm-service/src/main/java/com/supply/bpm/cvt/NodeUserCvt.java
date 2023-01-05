package com.supply.bpm.cvt;

import com.supply.bpm.model.po.NodeUserPo;
import com.supply.bpm.model.request.NodeUserRequest;
import com.supply.bpm.model.response.NodeUserResponse;
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
public interface NodeUserCvt {
    NodeUserCvt INSTANCE = Mappers.getMapper(NodeUserCvt.class);

    /**
     * @description 将request实体转换成po实体.
     * @author wjd
     * @date 2022/10/9
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    NodeUserPo requestToPo(NodeUserRequest request);

    /**
     * @description 批量将request实体转换成po实体.
     * @author wjd
     * @date 2022/10/9
     * @param list 待转换的request实体集
     * @return 转换后的po实体集
     */
    List<NodeUserPo> requestToPoBatch(List<NodeUserRequest> list);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    NodeUserResponse poToResponse(NodeUserPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<NodeUserResponse> poToResponseBatch(List<NodeUserPo> list);
}
