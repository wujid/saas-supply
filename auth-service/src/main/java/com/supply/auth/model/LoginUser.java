package com.supply.auth.model;

import com.supply.common.model.response.sys.SysUserResponse;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-19
 */
@Data
public class LoginUser implements UserDetails {

    private String username;

    private String password;

    private List<GrantedAuthority> authorities = new ArrayList<>();

    private Long tenantId;

    private Long userId;

    private String tenantCode;

    public LoginUser(String username, String password, Long tenantId, Long userId, String tenantCode) {
        this.username = username;
        this.password = password;
        this.tenantId = tenantId;
        this.userId = userId;
        this.tenantCode = tenantCode;
    }

    public LoginUser(SysUserResponse userResponse) {
        this.username = userResponse.getAccount();
        this.password = userResponse.getPassword();
        this.tenantId = userResponse.getTenantId();
        this.userId = userResponse.getId();
        this.tenantCode = userResponse.getTenantCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
