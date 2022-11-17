package com.supply.common.api;

import com.supply.common.annotation.Note;
import com.supply.common.model.Result;
import com.supply.common.model.request.file.FileAttachmentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wjd
 * @description
 * @date 2022-09-22
 */
@FeignClient("file-service")
public interface FileClient {

    @Note(description = "根据附件ID更新附件和业务关联状态为已关联")
    @GetMapping("/external/updateAttachmentRelation")
    Result<Object> updateAttachmentRelation(@RequestParam Long attachmentId, @RequestParam Long businessId);

    @Note(description = "批量根据附件ID更新附件和业务关联状态为已关联")
    @PostMapping("/updateBatchAttachmentRelation")
    Result<Object> updateBatchAttachmentRelation(@RequestBody FileAttachmentRequest request);

}
