package com.supply.gateway.api;

import com.supply.common.annotation.Note;
import com.supply.common.model.Result;
import com.supply.gateway.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wjd
 * @description
 * @date 2022-07-14
 */
@FeignClient("auth-service")
public interface AuthClient {

    @Note(description = "根据token获取登录用户信息")
    @RequestMapping(value = "/external/getLoginInfoByToken", method = RequestMethod.GET)
    Result<LoginUser> getLoginUserByToken(@RequestParam String token);
}
