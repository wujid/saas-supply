package com.supply.auth.service;

import com.supply.auth.model.LoginUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author wjd
 * @description
 * @date 2022-07-27
 */
public interface UserService extends UserDetailsService {


    LoginUser getLoginInfoByToken(String token);
}
