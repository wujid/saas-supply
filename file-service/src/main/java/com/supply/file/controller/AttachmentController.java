package com.supply.file.controller;

import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.file.model.request.AttachmentRequest;
import com.supply.file.model.response.AttachmentResponse;
import com.supply.file.service.IAttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wjd
 * @description 附件控制层.
 * @date 2022-09-20
 */
@Api(tags="附件控制层")
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    private final IAttachmentService attachmentService;

    public AttachmentController(IAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @ApiOperation(value = "单附件上传")
    @PostMapping("/upload")
    public Result<AttachmentResponse> upload(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String fileName) {
        final AttachmentResponse attachment = attachmentService.upload(file, fileName);
        return Result.ok(attachment);
    }

    @ApiOperation(value = "单附件下载")
    @GetMapping("downloadByParams")
    public void downloadByParams(@RequestParam(required = false) Long businessId, @RequestParam(required = false) Long attachmentId,
                             @RequestParam(defaultValue = "false") boolean isDownload, HttpServletResponse response) {
        AttachmentRequest request = new AttachmentRequest();
        request.setId(attachmentId);
        request.setBusinessId(businessId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        attachmentService.downloadByParams(request, isDownload, response);
    }

    @ApiOperation(value = "根据附件ID删除附件")
    @GetMapping("delUnRelationAttachmentById")
    public Result<Object> delUnRelationAttachmentById(@RequestParam(required = false) Long attachmentId) {
        if (null != attachmentId) {
            attachmentService.delUnRelationAttachmentById(attachmentId);
        }
        return Result.ok();
    }

    @ApiOperation(value = "根据条件查询唯一附件信息")
    @GetMapping("getAttachmentByParams")
    public Result<AttachmentResponse> getAttachmentByParams(@RequestParam(required = false) Long businessId, @RequestParam(required = false) Long attachmentId) {
        AttachmentRequest request = new AttachmentRequest();
        request.setId(attachmentId);
        request.setBusinessId(businessId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final AttachmentResponse attachment = attachmentService.getAttachmentByParams(request);
        return Result.ok(attachment);
    }
}
