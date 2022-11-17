package com.supply.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.system.model.request.TenantRequest;
import com.supply.system.model.request.TenantSaveRequest;
import com.supply.system.model.response.TenantDetailResponse;
import com.supply.system.model.response.TenantResponse;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
public interface ITenantService {

    /**
      * @description 保存租户信息.
      * @author wjd
      * @date 2022/7/27
      * @param request 待保存的实体
      */
    void addTenant(TenantSaveRequest request);

    /**
      * @description 修改租户信息.
      * @author wjd
      * @date 2022/8/10
      * @param request 待修改的实体
      */
    void updateTenant(TenantSaveRequest request);

    /**
     * @description 删除租户.
     * @author wjd
     * @date 2022/8/8
     * @param tenantId 待删除的租户ID
     */
    void delTenant(Long tenantId);

    /**
     * @description 冻结租户.
     * @author wjd
     * @date 2022/8/8
     * @param tenantId 待冻结的租户ID
     */
    void freezeTenant(Long tenantId);

    /**
     * @description 激活租户.
     * @author wjd
     * @date 2022/8/8
     * @param tenantId 待激活的租户ID
     */
    void activeTenant(Long tenantId);

    /**
      * @description 租户过期.
      * @author wjd
      * @date 2022/8/31
      */
    void expireTenant();

    /**
      * @description 租户续期.
      * @author wjd
      * @date 2022/9/1
      * @param tenantId 租户ID
      * @param days 续期天数
      * @return void
      */
    void renewalDays(Long tenantId, Integer days);

    /**
     * @description 根据自定义条件查询租户信息.
     * @author wjd
     * @date 2022/8/8
     * @param request 查询条件
     * @return 租户信息
     */
    TenantResponse getTenantByParams(TenantRequest request);

    /**
     * @description 根据自定义条件查询租户信息集.
     * @author wjd
     * @date 2022/8/10
     * @param request 查询条件
     * @return 租户信息集
     */
    List<TenantResponse> getTenantListByParams(TenantRequest request);

    /**
     * @description 根据自定义条件查询带分页的租户信息体.
     * @author wjd
     * @date 2022/8/8
     * @param request 查询条件
     * @return 带分页的租户信息体
     */
    IPage<TenantResponse> getTenantPage(TenantRequest request);

    /**
      * @description 根据租户ID获取该租户详细信息.
      * @author wjd
      * @date 2022/8/10
      * @param tenantId 租户ID
      * @return 该租户详细信息
      */
    TenantDetailResponse getDetailById(Long tenantId);
}
