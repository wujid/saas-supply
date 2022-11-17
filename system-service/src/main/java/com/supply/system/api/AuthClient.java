package com.supply.system.api;

import com.supply.common.annotation.Note;
import com.supply.common.model.Result;
import com.supply.common.model.response.auth.AuthTokenResponse;
import com.supply.system.model.request.ClientDetailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author wjd
 * @description
 * @date 2022-07-14
 */
@FeignClient("auth-service")
public interface AuthClient {

    @Note(description = "保存客户端详细信息")
    @PostMapping("/external/saveClientDetails")
    Result<Object> saveClientDetails(@RequestBody ClientDetailRequest request);

    @PostMapping(value = "/oauth/token",  produces = {"application/json"})
    Result<AuthTokenResponse> postAccessToken(@RequestParam Map<String, String> parameters);
}
