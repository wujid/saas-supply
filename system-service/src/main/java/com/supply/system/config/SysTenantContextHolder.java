package com.supply.system.config;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;

import java.util.Map;


@UtilityClass
public class SysTenantContextHolder {

    private final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_TENANT_ID";
    private final Map<String, Object> PRE_TENANT_CONTEXT = Maps.newConcurrentMap();

    /**
      * @description 设置租户Id.
      * @author wjd
      * @date 2022/7/8
      * @param id 租户ID
      */
    public void setCurrentTenantId(Long id) {
        PRE_TENANT_CONTEXT.put(KEY_CURRENT_TENANT_ID, id);
    }

    /**
      * @description 获取租户ID.
      * @author wjd
      * @date 2022/7/8
      * @return 租户ID
      */
    public Long getCurrentTenantId() {
        return (Long) PRE_TENANT_CONTEXT.get(KEY_CURRENT_TENANT_ID);
    }

    /**
      * @description 清除租户信息.
      * @author wjd
      * @date 2022/7/8
      */
    public void clear() {
        PRE_TENANT_CONTEXT.clear();
    }
}
