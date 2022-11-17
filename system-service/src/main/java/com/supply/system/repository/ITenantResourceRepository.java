package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.TenantResourcePo;
import com.supply.system.model.request.TenantResourceRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-09
 */
public interface ITenantResourceRepository extends IService<TenantResourcePo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/8/10
     * @param tenantResourcePo 修改实体
     * @param request 条件值
     * @return boolean 是否修改成功
     */
    boolean updateByParams(TenantResourcePo tenantResourcePo, TenantResourceRequest request);

    /**
     * @author wjd
     * @description 根据条件查询租户资源关联关系信息集.
     * @date 2022/8/10
     * @param request 待查询的条件
     * @return 租户资源关联关系信息集
     **/
    List<TenantResourcePo> getListByParams(TenantResourceRequest request);
}
