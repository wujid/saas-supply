package com.supply.file.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.file.model.po.AttachmentPo;
import com.supply.file.model.request.AttachmentRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-19
 */
public interface IAttachmentRepository extends IService<AttachmentPo> {

    /**
      * @description 根据自定义条件修改.
      * @author wjd
      * @date 2022/9/22
      * @param dataScopeRangePo 修改实体
      * @param request 条件值
      * @return int 受影响行数
      */
    int updateByParams(AttachmentPo dataScopeRangePo, @IgnoreFill AttachmentRequest request);

    /**
      * @description 根据条件查询唯一的附件信息.
      * @author wjd
      * @date 2022/9/22
      * @param request 待查询的条件
      * @return 附件信息
      */
    AttachmentPo getByParams(AttachmentRequest request);

    /**
      * @description 根据条件查询附件信息集.
      * @author wjd
      * @date 2022/9/23
      * @param request 待查询的条件
      * @return 附件信息集
      */
    List<AttachmentPo> getListByParams(AttachmentRequest request);
}
