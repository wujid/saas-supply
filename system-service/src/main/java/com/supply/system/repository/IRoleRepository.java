package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.RolePo;
import com.supply.system.model.request.RoleRequest;

import java.util.List;
import java.util.Map;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
public interface IRoleRepository extends IService<RolePo> {

    /**
      * @description 查询用户对应的角色信息集.
      * @author wjd
      * @date 2022/8/3
      * @param userId 用户ID
      * @return 角色信息集
      */
    List<RolePo> getRoleByUserId(Long userId);

    /**
     * @description 根据自定义条件查询条数信息.
     * @author wjd
     * @date 2022/8/3
     * @param request 查询条件
     * @return int 条数信息
     */
    Long getCountByParams(RoleRequest request);

    /**
     * @author wjd
     * @description 根据条件查询唯一的租户信息.
     * @date 2022/8/10
     * @param request 待查询的条件
     * @return 租户信息
     **/
    RolePo getByParams(RoleRequest request);

    /**
     * @description 根据自定义条件查询信息集.
     * @author wjd
     * @date 2022/8/3
     * @param request 查询条件
     * @return 角色信息集
     */
    List<RolePo> getListByParams(RoleRequest request);

    /**
     * @description 根据自定义条件查询带分页的角色信息集
     * @author wjd
     * @date 2022/8/4
     * @param page 分页条件
     * @param request 自定义查询条件
     * @return 带分页的角色信息集
     */
    Page<RolePo> getPageByParams(Page<RolePo> page, RoleRequest request);

    /**
     * @description 根据自定义条件查询并转换成映射
     * @author wjd
     * @date 2022/9/28
     * @param request 查询条件
     * @return 映射 key:角色ID value: 角色信息
     */
    Map<Long, RolePo> getMapByParams(RoleRequest request);
}
