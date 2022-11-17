package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.TenantPo;
import com.supply.system.model.request.TenantRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
public interface ITenantRepository extends IService<TenantPo> {

    /**
     * @author wjd
     * @description 根据条件查询唯一的租户信息.
     * @date 2022/8/8
     * @param request 待查询的条件
     * @return 租户信息
     **/
    TenantPo getByParams(TenantRequest request);

    /**
     * @author wjd
     * @description 根据条件查询租户信息集.
     * @date 2022/7/13
     * @param request 待查询的条件
     * @return 租户信息集
     **/
    List<TenantPo> getListByParams(TenantRequest request);

    /**
      * @description 根据自定义条件查询条数信息.
      * @author wjd
      * @date 2022/9/30
      * @param request 查询条件
      * @return 条数
      */
    Long getCountByParams(TenantRequest request);

    /**
     * @author wjd
     * @description 根据自定义条件查询带分页信息.
     * @date 2022/8/8
     * @param page 分页信息
     * @param request 查询条件
     * @return 带分页的结果
     **/
    Page<TenantPo> getPageByParams(Page<TenantPo> page, TenantRequest request);
}
