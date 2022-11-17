package com.supply.file.service;

import com.supply.file.model.request.AttachmentRequest;
import com.supply.file.model.response.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-09-19
 */
public interface IAttachmentService {

    /**
      * @description 单附件上传.
      * @author wjd
      * @date 2022/9/20
      * @param file 待上传的附件
      * @param fileName 附件名称
      * @return 附件信息
      */
    AttachmentResponse upload(MultipartFile file, String fileName);

    /**
      * @description 根据自定义条件查询单附件下载.
      * @author wjd
      * @date 2022/9/21
      * @param request 查询条件
      * @param isDownload 是否下载
      */
    void downloadByParams(AttachmentRequest request, boolean isDownload, HttpServletResponse response);

    /**
      * @description 更新附件和业务关联状态.
      * @author wjd
      * @date 2022/9/22
      * @param attachmentId 附件ID
      * @param businessId 业务ID
      */
    void updateAttachmentRelation(Long attachmentId, Long businessId);

    /**
      * @description 批量更新附件和业务关联状态.
      * @author wjd
      * @date 2022/9/22
      * @param attachmentIds 附件ID集
      * @param businessId 业务ID
      * @return void
      */
    void updateBatchAttachmentRelation(Set<Long> attachmentIds, Long businessId);

    /**
      * @description 根据附件ID删除附件(仅删除未和业务关联的附件)
      * @author wjd
      * @date 2022/9/22
      * @param attachmentId 附件ID
      */
    void delUnRelationAttachmentById(Long attachmentId);

    /**
      * @description 删除当前日期前一天的所有未关联附件.
      * @author wjd
      * @date 2022/9/23
      */
    void delUnRelationAttachment();

    /**
      * @description 根据自定义条件查询唯一附件信息.
      * @author wjd
      * @date 2022/9/22
      * @param request 查询条件
      * @return 附件信息
      */
    AttachmentResponse getAttachmentByParams(AttachmentRequest request);
}
