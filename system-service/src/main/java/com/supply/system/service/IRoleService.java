package com.supply.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.system.model.request.RoleRequest;
import com.supply.system.model.response.RoleResponse;

import java.util.List;

/**
 * @author wjd
 * @description 系统角色信息服务接口层.
 * @date 2022-08-03
 */
public interface IRoleService {

    /**
     * @description 新增角色.
     * @author wjd
     * @date 2022/8/3
     * @param request 待新增角色的信息体
     */
    void addRole(RoleRequest request);

    /**
     * @description 修改角色.
     * @author wjd
     * @date 2022/8/3
     * @param request 待修改角色的信息体
     */
    void updateRole(RoleRequest request);

    /**
     * @description 删除角色.
     * @author wjd
     * @date 2022/5/17
     * @param roleId 待删除的角色ID
     */
    void delRole(Long roleId);

    /**
      * @description 冻结角色.
      * @author wjd
      * @date 2022/8/3
      * @param roleId 待冻结的角色ID
      */
    void freezeRole(Long roleId);

    /**
     * @description 解冻角色.
     * @author wjd
     * @date 2022/8/3
     * @param roleId 待解冻的角色ID
     */
    void activeUser(Long roleId);

    /**
      * @description 设置角色资源权限.
      * @author wjd
      * @date 2022/8/5
      * @param request 请求实体
      */
    void setResourceAuth(RoleRequest request);

    /**
     * @description 根据自定义条件查询信息集.
     * @author wjd
     * @date 2022/8/3
     * @param request 查询条件
     * @return 角色信息集
     */
    List<RoleResponse> getListByParams(RoleRequest request);

    /**
     * @description 根据自定义条件查询带分页的角色信息集.
     * @author wjd
     * @date 2022/8/4
     * @param request 查询条件
     * @return 带分页的角色信息集
     */
    IPage<RoleResponse> getPageByParams(RoleRequest request);

    /**
     * @description 根据角色ID获取该角色对应的权限信息.
     * @author wjd
     * @date 2022/8/3
     * @param roleId 角色ID
     * @param tenantId 租户ID
     * @return 权限信息
     */
    RoleResponse getAuthByRoleId(Long roleId, Long tenantId);
}
