package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.UserRolePo;
import com.supply.system.model.request.UserRoleRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
public interface IUserRoleRepository extends IService<UserRolePo> {

    /**
      * @description 根据自定义条件修改.
      * @author wjd
      * @date 2022/7/31
      * @param userRolePo 修改实体
      * @param request 条件值
      * @return boolean 是否修改成功
      */
    boolean updateByParams(UserRolePo userRolePo, UserRoleRequest request);

    /**
     * @author wjd
     * @description 根据条件查询用户角色关联关系信息集.
     * @date 2022/7/13
     * @param request 待查询的条件
     * @return 用户信息集
     **/
    List<UserRolePo> getListByParams(UserRoleRequest request);

    /**
      * @description 根据自定义条件查询数量信息.
      * @author wjd
      * @date 2022/9/30
      * @param request 查询条件
      * @return 条数
      */
    Long getCountByParams(UserRoleRequest request);
}
