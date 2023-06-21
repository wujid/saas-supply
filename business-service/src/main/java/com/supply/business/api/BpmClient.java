package com.supply.business.api;

import com.supply.common.model.Result;
import com.supply.common.web.model.BpmRequestEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wjd
 * @description .
 * @date 2023-06-21
 */
@FeignClient("bpm-service")
public interface BpmClient {

    @ApiOperation(value = "流程发起")
    @PostMapping("/external/startProcess")
    Result<?> startProcess(@RequestBody BpmRequestEntity request);
}
