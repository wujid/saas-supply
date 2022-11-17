package com.supply.system.service;

import com.supply.system.model.request.OrgRequest;
import com.supply.system.model.response.OrgResponse;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-10
 */
public interface IOrgService {

    /**
     * @description 新增组织机构.
     * @author wjd
     * @date 2022/8/5
     * @param request 实体信息
     */
    void addOrg(OrgRequest request);

    /**
     * @description 修改组织机构.
     * @author wjd
     * @date 2022/8/5
     * @param request 实体信息
     */
    void updateOrg(OrgRequest request);

    /**
     * @description 新增组织机构.
     * @author wjd
     * @date 2022/8/5
     * @param orgId 实体信息
     */
    void delOrg(Long orgId);

    /**
     * @description 新增组织机构.
     * @author wjd
     * @date 2022/8/5
     * @param orgId 实体信息
     */
    void freezeOrg(Long orgId);


    /**
     * @description 新增组织机构.
     * @author wjd
     * @date 2022/8/5
     * @param orgId 实体信息
     */
    void activeOrg(Long orgId);

    /**
      * @description 根据自定义条件查询组织机构.
      * @author wjd
      * @date 2022/8/15
      * @param request 查询条件
      * @return 查询结构集
      */
    List<OrgResponse> getOrgListByParams(OrgRequest request);

    /**
     * @description 获取组织机构树结构并带父ID及名称过滤条件.
     * @author wjd
     * @date 2022/8/12
     * @param tenantId 租户ID
     * @param parentId 父ID
     * @param name 组织机构名称
     * @return 组织机构树结构
     */
    List<OrgResponse> getOrgTree(Long tenantId, Long parentId, String name);

}
