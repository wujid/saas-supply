package com.supply.file.cvt;

import com.supply.file.model.po.AttachmentPo;
import com.supply.file.model.request.AttachmentRequest;
import com.supply.file.model.response.AttachmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttachmentCvt {
    AttachmentCvt INSTANCE = Mappers.getMapper(AttachmentCvt.class);


    /**
     * @description 将request实体转换成po实体
     * @author wjd
     * @date 2022/9/19
     * @param request 待转换的request实体
     * @return 转换后的po实体
     */
    AttachmentPo requestToPo(AttachmentRequest request);

    /**
     * @description 批量将request实体转换成po实体.
     * @author wjd
     * @date 2022/9/19
     * @param list 待转换的request实体集
     * @return 转换后的po实体集
     */
    List<AttachmentPo> requestToPoBatch(List<AttachmentRequest> list);

    /**
     * @description 将po实体转换成response实体.
     * @author wjd
     * @date 2022/9/19
     * @param po 待转换的po实体
     * @return 转换后的response实体
     */
    AttachmentResponse poToResponse(AttachmentPo po);

    /**
     * @description 批量将po实体转换成response实体.
     * @author wjd
     * @date 2022/9/19
     * @param list 待转换的po实体集
     * @return 转换后的response实体集
     */
    List<AttachmentResponse> poToResponseBatch(List<AttachmentPo> list);
}
