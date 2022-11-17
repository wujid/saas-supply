package com.supply.auth.api;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
@FeignClient("system-service")
public interface SystemClient {
}
