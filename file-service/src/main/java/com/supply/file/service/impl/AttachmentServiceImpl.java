package com.supply.file.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.file.cvt.AttachmentCvt;
import com.supply.file.model.po.AttachmentPo;
import com.supply.file.model.request.AttachmentRequest;
import com.supply.file.model.response.AttachmentResponse;
import com.supply.file.repository.IAttachmentRepository;
import com.supply.file.service.IAttachmentService;
import com.supply.file.util.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-09-19
 */
@Service
public class AttachmentServiceImpl implements IAttachmentService {
    private static final Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final IAttachmentRepository attachmentRepository;

    private final FastFileStorageClient fastFileStorageClient;


    public AttachmentServiceImpl(IAttachmentRepository attachmentRepository, FastFileStorageClient fastFileStorageClient) {
        this.attachmentRepository = attachmentRepository;
        this.fastFileStorageClient = fastFileStorageClient;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttachmentResponse upload(MultipartFile file, String fileName) {
        if (StrUtil.isBlank(fileName)) {
            fileName = file.getOriginalFilename();
        }
        logger.info("[附件上传]---附件名为{}", fileName);
        final long size = file.getSize();
        final String fileExtName = FilenameUtils.getExtension(file.getOriginalFilename());
        String path = null;
        String group = null;
        try (final InputStream inputStream = file.getInputStream()) {
            StorePath storePath = fastFileStorageClient.uploadFile(inputStream, size, fileExtName, null);
            path = storePath.getPath();
            group = storePath.getGroup();
        } catch (IOException e) {
           logger.error("[附件上传]---上传异常", e);
        }
        AttachmentPo attachmentPo = new AttachmentPo();
        attachmentPo.setName(fileName);
        attachmentPo.setPathGroup(group);
        attachmentPo.setPath(path);
        attachmentPo.setExtensionName(fileExtName);
        attachmentPo.setSize(size);
        attachmentPo.setBusinessStatus(BusinessStatusEnum.WAIT_RELATION.getStatus());
        attachmentRepository.save(attachmentPo);
        return AttachmentCvt.INSTANCE.poToResponse(attachmentPo);
    }

    @Override
    public void downloadByParams(AttachmentRequest request, boolean isDownload, HttpServletResponse response) {
        final AttachmentPo attachmentPo = attachmentRepository.getByParams(request);
        if (null == attachmentPo) {
            throw new ApiException("下载失败");
        }
        byte[] bytes = fastFileStorageClient.downloadFile(attachmentPo.getPathGroup(), attachmentPo.getPath(), new DownloadByteArray());
        response.reset();
        ServletOutputStream outputStream = null;
        try {
            FileUtil.fileOperator(attachmentPo.getName(), attachmentPo.getExtensionName(), isDownload, response);
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (Exception e) {
            final String message = "下载失败";
            logger.error(message, e);
            throw new ApiException(message);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttachmentRelation(Long attachmentId, Long businessId) {
        logger.info("[附件关联关系更新]---附件ID为{}, 业务ID为{}", attachmentId, businessId);
        // 根据业务ID删除历史附件信息
       this.delAttachmentByBusinessId(businessId);
        // 绑定新附件
        if (null == attachmentId) {
            return;
        }
        AttachmentPo attachmentPo = new AttachmentPo();
        attachmentPo.setId(attachmentId);
        attachmentPo.setBusinessId(businessId);
        attachmentPo.setBusinessStatus(BusinessStatusEnum.RELATION.getStatus());
        attachmentPo.setStatus(Constant.STATUS_NOT_DEL);
        attachmentRepository.updateById(attachmentPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchAttachmentRelation(Set<Long> attachmentIds, Long businessId) {
        logger.info("[批量附件关联关系更新]---附件ID集为{}, 业务ID为{}", attachmentIds, businessId);
        // 根据业务ID删除历史附件信息
        this.delAttachmentByBusinessId(businessId);

        // 绑定新附件
        if (CollectionUtil.isEmpty(attachmentIds)) {
            return;
        }
        AttachmentPo attachmentPo = new AttachmentPo();
        attachmentPo.setBusinessId(businessId);
        attachmentPo.setBusinessStatus(BusinessStatusEnum.RELATION.getStatus());
        attachmentPo.setStatus(Constant.STATUS_NOT_DEL);

        AttachmentRequest request = new AttachmentRequest();
        request.setIds(attachmentIds);

        attachmentRepository.updateByParams(attachmentPo, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUnRelationAttachmentById(Long attachmentId) {
        logger.info("[删除未关联的附件]---附件ID为{}", attachmentId);
        final AttachmentPo attachmentPo = attachmentRepository.getById(attachmentId);
        // 如果当前附件状态为待关联则直接删除
        if (attachmentPo.getBusinessStatus() == BusinessStatusEnum.WAIT_RELATION.getStatus()) {
            AttachmentPo attachment = new AttachmentPo();
            attachment.setId(attachmentId);
            attachment.setStatus(Constant.STATUS_DEL);
            attachmentRepository.updateById(attachment);
            // 删除附件服务器上的附件
            fastFileStorageClient.deleteFile(attachmentPo.getPathGroup(), attachmentPo.getPath());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delUnRelationAttachment() {
        logger.info("[附件删除]---开始执行");
        // 查询出所有未关联的附件
        AttachmentRequest request = new AttachmentRequest();
        request.setBusinessStatus(BusinessStatusEnum.WAIT_RELATION.getStatus());
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<AttachmentPo> attachmentList = attachmentRepository.getListByParams(request);
        // 当前日期前一天的附件进行删除
        final DateTime dateTime = DateUtil.offsetDay(DateUtil.date(), -1);
        for (AttachmentPo attachmentPo : attachmentList) {
            final Date createTime = attachmentPo.getCreateTime();
            if (DateUtil.compare(createTime, dateTime, DatePattern.NORM_DATE_PATTERN) <= 0) {
                AttachmentPo attachment = new AttachmentPo();
                attachment.setId(attachmentPo.getId());
                attachment.setStatus(Constant.STATUS_DEL);
                attachmentRepository.updateById(attachment);
                // 删除附件服务器上的附件
                fastFileStorageClient.deleteFile(attachmentPo.getPathGroup(), attachmentPo.getPath());
            }
        }
    }

    @Override
    public AttachmentResponse getAttachmentByParams(AttachmentRequest request) {
        final AttachmentPo attachmentPo = attachmentRepository.getByParams(request);
        return AttachmentCvt.INSTANCE.poToResponse(attachmentPo);
    }


    /**
      * @description 根据业务ID删除附件.
      * @author wjd
      * @date 2022/9/22
      * @param businessId 业务ID
      */
    private void delAttachmentByBusinessId(Long businessId) {
        if (null == businessId) {
            return;
        }
        AttachmentPo attachmentPo = new AttachmentPo();
        attachmentPo.setStatus(Constant.STATUS_DEL);

        AttachmentRequest request = new AttachmentRequest();
        request.setBusinessId(businessId);
        request.setStatus(Constant.STATUS_NOT_DEL);

        attachmentRepository.updateByParams(attachmentPo, request);
    }


}
