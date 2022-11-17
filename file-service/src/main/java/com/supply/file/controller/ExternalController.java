package com.supply.file.controller;

import com.supply.common.model.Result;
import com.supply.common.model.request.file.FileAttachmentRequest;
import com.supply.file.service.IAttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description
 * @date 2022-09-22
 */
@Api(tags = "外部服务调用")
@RestController
@RequestMapping("/external")
public class ExternalController {

    private final IAttachmentService attachmentService;


    public ExternalController(IAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }


    @ApiOperation(value = "根据附件ID更新附件和业务关联状态为已关联")
    @GetMapping("/updateAttachmentRelation")
    public Result<Object> updateAttachmentRelation(@RequestParam(required = false) Long attachmentId, @RequestParam(required = false) Long businessId) {
        attachmentService.updateAttachmentRelation(attachmentId, businessId);
        return Result.ok();
    }

    @ApiOperation(value = "批量根据附件ID更新附件和业务关联状态为已关联")
    @PostMapping("/updateBatchAttachmentRelation")
    public Result<Object> updateBatchAttachmentRelation(@RequestBody FileAttachmentRequest request) {
        attachmentService.updateBatchAttachmentRelation(request.getIds(), request.getBusinessId());
        return Result.ok();
    }

    @ApiOperation(value = "删除当前日期前一天的所有未关联附件")
    @GetMapping("/delUnRelationAttachment")
    public Result<Object> delUnRelationAttachment() {
        attachmentService.delUnRelationAttachment();
        return Result.ok();
    }





}
