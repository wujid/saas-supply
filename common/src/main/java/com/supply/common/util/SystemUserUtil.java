package com.supply.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.supply.common.api.SystemClient;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.model.Result;
import com.supply.common.model.request.sys.SysResourceRequest;
import com.supply.common.model.request.sys.SysTenantRequest;
import com.supply.common.model.request.sys.SysUserRequest;
import com.supply.common.model.response.sys.SysDataScopeTypeResponse;
import com.supply.common.model.response.sys.SysResourceResponse;
import com.supply.common.model.response.sys.SysRoleResponse;
import com.supply.common.model.response.sys.SysTenantResponse;
import com.supply.common.model.response.sys.SysUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-09-29
 */
@Component
public class SystemUserUtil {
    private static final Logger logger = LoggerFactory.getLogger(SystemUserUtil.class);

    private final SystemClient systemClient;

    public SystemUserUtil(SystemClient systemClient) {
        this.systemClient = systemClient;
    }

    /**
     * @description 根据用户ID + 资源ID获取用户数据权限.
     * @author wjd
     * @date 2022/9/7
     * @param userId 用户ID
     * @param resourceId 资源ID
     * @return 用户数据权限集
     */
    public SysDataScopeTypeResponse getDataScope(Long userId, Long resourceId) {
        // 根据资源ID&用户ID查询对应的数据权限信息
        final Result<SysDataScopeTypeResponse> result = systemClient.getDataScopeByParams(userId, resourceId);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据用户ID{},资源ID{}查询用户资源权限信息异常", userId, resourceId);
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
     * @description 根据资源编码获取资源信息.
     * @author wjd
     * @date 2022/9/7
     * @param code 资源编码
     * @return 资源信息
     */
    public SysResourceResponse getResourceByCode(String code) {
        SysResourceRequest resourceRequest = new SysResourceRequest();
        resourceRequest.setCode(code);
        final Result<SysResourceResponse> result = systemClient.getResourceByParams(resourceRequest);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据资源编码{}查询资源信息异常!", code);
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
     * @description 根据角色ID集查询对应的用户ID集.
     * @author wjd
     * @date 2022/9/28
     * @param roleIds 角色ID集
     * @return 用户ID集
     */
    public Set<Long> getUserIdsByRoleIds(Set<Long> roleIds) {
        final Result<Set<Long>> result = systemClient.getUserIdsByRoleIds(roleIds);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据角色ID集{}查询用户ID集信息异常!", JSON.toJSONString(roleIds));
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
      * @description 根据租户ID查询未删除的用户信息集.
      * @author wjd
      * @date 2022/10/31
      * @param tenantId 租户ID
      * @return 用户信息集
      */
    public List<SysUserResponse> getUsersByTenantId(Long tenantId) {
        SysUserRequest request = new SysUserRequest();
        request.setTenantId(tenantId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final Result<List<SysUserResponse>> result = systemClient.getUsesByParams(request);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据租户ID{}查询用户集信息异常!", tenantId);
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
     * @description 根据租户ID集查询未删除的用户信息集.
     * @author wjd
     * @date 2022/10/31
     * @param tenantIds 租户ID集
     * @return 用户信息集
     */
    public List<SysUserResponse> getUsersByTenantIds(Set<Long> tenantIds) {
        SysUserRequest request = new SysUserRequest();
        request.setTenantIds(tenantIds);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final Result<List<SysUserResponse>> result = systemClient.getUsesByParams(request);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据租户ID集{}查询用户集信息异常!", JSON.toJSONString(tenantIds));
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
     * @description 根据用户ID查询用户信息.
     * @author wjd
     * @date 2022/11/1
     * @param userId 用户ID
     * @return 用户信息
     */
    public SysUserResponse getUserById(Long userId) {
        final Result<SysUserResponse> result = systemClient.getUserById(userId);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据用户ID{}查询用户信息异常!", userId);
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
      * @description 根据用户ID集查询用户信息集.
      * @author wjd
      * @date 2022/11/1
      * @param userIds 用户ID集
      * @return 用户信息集
      */
    public List<SysUserResponse> getUsersByIds(Set<Long> userIds) {
        SysUserRequest request = new SysUserRequest();
        request.setUserIds(userIds);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final Result<List<SysUserResponse>> result = systemClient.getUsesByParams(request);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据用户ID集{}查询用户集信息异常!", JSON.toJSONString(userIds));
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
     * @description 根据自定义条件查询用户信息集.
     * @author wjd
     * @date 2022/11/1
     * @param request 自定义条件
     * @return 用户信息集
     */
    public List<SysUserResponse> getUsersByParams(SysUserRequest request) {
        final Result<List<SysUserResponse>> result = systemClient.getUsesByParams(request);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据自定义条件{}查询用户集信息异常!", JSON.toJSONString(request));
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
     * @description 根据自定义条件查询用户信息.
     * @author wjd
     * @date 2022/11/1
     * @param request 自定义条件
     * @return 用户信息
     */
    public SysUserResponse getUserByParams(SysUserRequest request) {
        final Result<SysUserResponse> result = systemClient.getUseByParams(request);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据自定义条件{}查询用户信息异常!", JSON.toJSONString(request));
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
      * @description 根据自定义条件查询租户信息.
      * @author wjd
      * @date 2022/11/4
      * @param request 查询条件
      * @return 租户信息
      */
    public SysTenantResponse getTenantByParams(SysTenantRequest request) {
        final Result<SysTenantResponse> result = systemClient.getTenantByParams(request);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据自定义条件{}查询租户信息异常!", JSON.toJSONString(request));
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
      * @description 根据角色ID集查询角色信息集.
      * @author wjd
      * @date 2023/6/21
      * @param roleIds 角色ID集
      * @return 角色信息集
      */
    public List<SysRoleResponse> getRolesByRoleId(Set<Long> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return null;
        }
        final Result<List<SysRoleResponse>> result = systemClient.getRolesByIds(roleIds);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据用角色ID{}查询角色信息异常!", JSON.toJSONString(roleIds));
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

    /**
      * @description 根据用户ID查询对应的角色ID集.
      * @author wjd
      * @date 2023/6/25
      * @param userId 用户ID
      * @return 角色ID集
      */
    public Set<Long> getRoleIdsByUserId(Long userId) {
        if (null == userId) {
            return null;
        }
        final Result<Set<Long>> result = systemClient.getRoleIdsByUserId(userId);
        if (!result.isOk()) {
            final String message = StrUtil.format("根据用户ID{}查询对应的角色ID集异常!", userId);
            logger.error(message);
            throw new ApiException(message);
        }
        return result.getData();
    }

}
