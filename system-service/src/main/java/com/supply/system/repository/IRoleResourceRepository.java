package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.RoleResourcePo;
import com.supply.system.model.request.RoleResourceRequest;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
public interface IRoleResourceRepository extends IService<RoleResourcePo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/8/5
     * @param roleResourcePo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(RoleResourcePo roleResourcePo, RoleResourceRequest request);
}
