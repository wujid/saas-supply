package com.supply.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.system.model.request.UserRequest;
import com.supply.system.model.response.UserResponse;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-07
 */
public interface IUserService {

    /**
      * @description 新增用户.
      * @author wjd
      * @date 2022/8/4
      * @param request 待新增的用户实体
      */
    void addUser(UserRequest request);

    /**
      * @description 修改用户.
      * @author wjd
      * @date 2022/7/31
      * @param request 待修改的实体
      */
    void updateUser(UserRequest request);

    /**
     * @description 重置密码
     * @author wjd
     * @date 2022/8/3
     * @param userId 用户主键ID
     * @param password 新密码
     */
    void resetPassword(Long userId, String password);

    /**
      * @description 根据用户ID删除用户.
      * @author wjd
      * @date 2022/7/28
      * @param userId 用户ID
      */
    void delUser(Long userId);

    /**
      * @description 根据用户ID冻结用户.
      * @author wjd
      * @date 2022/7/28
      * @param userId 用户ID
      */
    void freezeUser(Long userId);

    /**
      * @description 根据用户ID激活用户.
      * @author wjd
      * @date 2022/7/28
      * @param userId 用户ID
      */
    void activeUser(Long userId);

    /**
     * @description 根据自定义条件查询带分页的用户信息体.
     * @author wjd
     * @date 2022/7/31
     * @param request 查询条件
     * @return 带分页的用户信息体
     */
    IPage<UserResponse> getUserPage(UserRequest request);

    /**
      * @description 根据自定义条件查询用户信息集.
      * @author wjd
      * @date 2022/9/28
      * @param request 查询条件
      * @return 用户信息集
      */
    List<UserResponse> getUserListByParams(UserRequest request);

    /**
     * @description 根据自定义条件查询用户信息.
     * @author wjd
     * @date 2022/7/31
     * @param request 查询条件
     * @return 用户信息
     */
    UserResponse getUseByParams(UserRequest request);

    /**
     * @description 根据客户端ID获取管理员用户信息.
     * @author wjd
     * @date 2022/7/27
     * @param clientId 客户端ID
     * @return 管理员用户
     */
    UserResponse getManageUserByClientId(String clientId);
}
