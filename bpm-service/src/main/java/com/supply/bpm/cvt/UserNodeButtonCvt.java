package com.supply.bpm.cvt;

import com.supply.bpm.model.po.UserNodeButtonPo;
import com.supply.bpm.model.request.UserNodeButtonRequest;
import com.supply.bpm.model.response.UserNodeButtonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description 流程节点按钮实体转换.
 * @date 2023-01-04
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserNodeButtonCvt {
    UserNodeButtonCvt INSTANCE = Mappers.getMapper(UserNodeButtonCvt.class);

    /**
     * @description 将request实体转换成po实体.
     * @author wjd
     * @date 2022/10/9
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    UserNodeButtonPo requestToPo(UserNodeButtonRequest request);

    /**
     * @description 批量将request实体转换成po实体.
     * @author wjd
     * @date 2022/10/9
     * @param list 待转换的request实体集
     * @return 转换后的po实体集
     */
    List<UserNodeButtonPo> requestToPoBatch(List<UserNodeButtonRequest> list);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    UserNodeButtonResponse poToResponse(UserNodeButtonPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<UserNodeButtonResponse> poToResponseBatch(List<UserNodeButtonPo> list);
}
