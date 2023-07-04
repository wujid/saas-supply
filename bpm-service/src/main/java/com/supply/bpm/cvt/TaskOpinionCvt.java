package com.supply.bpm.cvt;

import com.supply.bpm.model.po.TaskOpinionPo;
import com.supply.bpm.model.request.TaskOpinionRequest;
import com.supply.bpm.model.response.TaskOpinionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-07-04
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskOpinionCvt {
    TaskOpinionCvt INSTANCE = Mappers.getMapper(TaskOpinionCvt.class);

    /**
     * @description 将request实体转换成po实体.
     * @author wjd
     * @date 2022/10/9
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    TaskOpinionPo requestToPo(TaskOpinionRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    TaskOpinionResponse poToResponse(TaskOpinionPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<TaskOpinionResponse> poToResponseBatch(List<TaskOpinionPo> list);
}
