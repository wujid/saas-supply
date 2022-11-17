package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.request.ResourceRequest;

import java.util.List;
import java.util.Map;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
public interface IResourceRepository extends IService<ResourcePo> {

    /**
     * @description 根据自定义条件查询批量信息.
     * @author wjd
     * @date 2022/8/3
     * @param request 查询条件
     * @return 菜单信息集
     */
    List<ResourcePo> getListByParams(ResourceRequest request);

    /**
      * @description 根据自定义条件查询资源信息.
      * @author wjd
      * @date 2022/9/6
      * @param request 查询条件
      * @return 资源信息
      */
    ResourcePo getByParams(ResourceRequest request);

    /**
     * @description 根据自定义条件查询条数信息.
     * @author wjd
     * @date 2022/8/3
     * @param request 查询条件
     * @return int 条数信息
     */
    Long getCountByParams(ResourceRequest request);

    /**
     * @description 根据角色ID集查询对应的资源信息集.
     * @author wjd
     * @date 2022/8/3
     * @param roleIdList 角色ID集
     * @param type 资源类型
     * @return 资源信息集
     */
    List<ResourcePo> getByRoleIdList(List<Long> roleIdList, Integer type);

    /**
      * @description 根据租户ID查询对应的资源信息集.
      * @author wjd
      * @date 2022/8/10
      * @param tenantId 租户ID
      * @return 资源信息集
      */
    List<ResourcePo> getByTenantId(Long tenantId);

    /**
     * @description 根据自定义条件查询并转换成映射
     * @author wjd
     * @date 2022/9/8
     * @param request 查询条件
     * @return 映射 key:资源ID value: 资源信息
     */
    Map<Long, ResourcePo> getMapByParams(ResourceRequest request);
}
