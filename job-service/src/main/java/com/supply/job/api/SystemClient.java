package com.supply.job.api;

import com.supply.common.model.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
@FeignClient("system-service")
public interface SystemClient {

    @ApiOperation(value = "根据用户ID获取用户信息")
    @GetMapping("/external/expireTenant")
    Result<Object> expireTenant();
}
