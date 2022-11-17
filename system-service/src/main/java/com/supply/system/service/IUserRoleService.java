package com.supply.system.service;

import com.supply.system.model.request.UserRoleRequest;
import com.supply.system.model.response.UserRoleResponse;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-28
 */
public interface IUserRoleService {

    /**
      * @description 新增用户角色关联关系.
      * @author wjd
      * @date 2022/9/30
      * @param request 待关联的实体信息
      */
    void addUserRole(UserRoleRequest request);

    /**
      * @description 根据自定义条件删除用户角色关联关系.
      * @author wjd
      * @date 2022/9/30
      * @param request 删除条件
      */
    void delUserRoleByParams(UserRoleRequest request);

    /**
      * @description 根据自定义条件查询用户角色关联关系集.
      * @author wjd
      * @date 2022/9/28
      * @param request 查询条件
      * @return 用户角色关联关系集
      */
    List<UserRoleResponse> getUserRoleListByParams(UserRoleRequest request);
}
