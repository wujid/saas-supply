package com.supply.job.api;

import com.supply.common.model.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wjd
 * @description
 * @date 2022-09-23
 */
@FeignClient("file-service")
public interface FileClient {

    @ApiOperation(value = "删除当前日期前一天的所有未关联附件")
    @GetMapping("/external/delUnRelationAttachment")
    Result<Object> delUnRelationAttachment();

}
