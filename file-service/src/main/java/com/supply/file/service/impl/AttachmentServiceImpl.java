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
        logger.info("[????????????]---????????????{}", fileName);
        final long size = file.getSize();
        final String fileExtName = FilenameUtils.getExtension(file.getOriginalFilename());
        String path = null;
        String group = null;
        try (final InputStream inputStream = file.getInputStream()) {
            StorePath storePath = fastFileStorageClient.uploadFile(inputStream, size, fileExtName, null);
            path = storePath.getPath();
            group = storePath.getGroup();
        } catch (IOException e) {
           logger.error("[????????????]---????????????", e);
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
            throw new ApiException("????????????");
        }
        byte[] bytes = fastFileStorageClient.downloadFile(attachmentPo.getPathGroup(), attachmentPo.getPath(), new DownloadByteArray());
        response.reset();
        ServletOutputStream outputStream = null;
        try {
            FileUtil.fileOperator(attachmentPo.getName(), attachmentPo.getExtensionName(), isDownload, response);
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (Exception e) {
            final String message = "????????????";
            logger.error(message, e);
            throw new ApiException(message);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttachmentRelation(Long attachmentId, Long businessId) {
        logger.info("[????????????????????????]---??????ID???{}, ??????ID???{}", attachmentId, businessId);
        // ????????????ID????????????????????????
       this.delAttachmentByBusinessId(businessId);
        // ???????????????
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
        logger.info("[??????????????????????????????]---??????ID??????{}, ??????ID???{}", attachmentIds, businessId);
        // ????????????ID????????????????????????
        this.delAttachmentByBusinessId(businessId);

        // ???????????????
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
        logger.info("[????????????????????????]---??????ID???{}", attachmentId);
        final AttachmentPo attachmentPo = attachmentRepository.getById(attachmentId);
        // ???????????????????????????????????????????????????
        if (attachmentPo.getBusinessStatus() == BusinessStatusEnum.WAIT_RELATION.getStatus()) {
            AttachmentPo attachment = new AttachmentPo();
            attachment.setId(attachmentId);
            attachment.setStatus(Constant.STATUS_DEL);
            attachmentRepository.updateById(attachment);
            // ?????????????????????????????????
            fastFileStorageClient.deleteFile(attachmentPo.getPathGroup(), attachmentPo.getPath());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delUnRelationAttachment() {
        logger.info("[????????????]---????????????");
        // ?????????????????????????????????
        AttachmentRequest request = new AttachmentRequest();
        request.setBusinessStatus(BusinessStatusEnum.WAIT_RELATION.getStatus());
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<AttachmentPo> attachmentList = attachmentRepository.getListByParams(request);
        // ??????????????????????????????????????????
        final DateTime dateTime = DateUtil.offsetDay(DateUtil.date(), -1);
        for (AttachmentPo attachmentPo : attachmentList) {
            final Date createTime = attachmentPo.getCreateTime();
            if (DateUtil.compare(createTime, dateTime, DatePattern.NORM_DATE_PATTERN) <= 0) {
                AttachmentPo attachment = new AttachmentPo();
                attachment.setId(attachmentPo.getId());
                attachment.setStatus(Constant.STATUS_DEL);
                attachmentRepository.updateById(attachment);
                // ?????????????????????????????????
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
      * @description ????????????ID????????????.
      * @author wjd
      * @date 2022/9/22
      * @param businessId ??????ID
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
