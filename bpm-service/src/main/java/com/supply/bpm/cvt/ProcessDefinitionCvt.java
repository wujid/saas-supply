package com.supply.bpm.cvt;

import com.supply.bpm.model.po.ProcessDefinitionPo;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.model.response.ProcessDefinitionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProcessDefinitionCvt {
    ProcessDefinitionCvt INSTANCE = Mappers.getMapper(ProcessDefinitionCvt.class);

    /**
     * @description 将request实体转换成po实体.
     * @author wjd
     * @date 2022/10/9
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    ProcessDefinitionPo requestToPo(ProcessDefinitionRequest request);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    ProcessDefinitionResponse poToResponse(ProcessDefinitionPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/10/9
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<ProcessDefinitionResponse> poToResponseBatch(List<ProcessDefinitionPo> list);
}
