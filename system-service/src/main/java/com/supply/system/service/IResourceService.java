package com.supply.system.service;

import com.supply.system.model.request.ResourceRequest;
import com.supply.system.model.response.ResourceResponse;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-05
 */
public interface IResourceService {

    /**
      * @description 新增资源.
      * @author wjd
      * @date 2022/8/5
      * @param request 实体信息
      */
    void addResource(ResourceRequest request);

    /**
      * @description 修改资源.
      * @author wjd
      * @date 2022/8/5
      * @param request 实体信息
      */
    void updateResource(ResourceRequest request);

    /**
      * @description 删除资源.
      * @author wjd
      * @date 2022/8/5
      * @param resourceId 资源ID
      */
    void delResource(Long resourceId);

    /**
      * @description 资源冻结.
      * @author wjd
      * @date 2022/8/5
      * @param resourceId 资源ID
      */
    void freezeResource(Long resourceId);

    /**
      * @description 资源解冻.
      * @author wjd
      * @date 2022/8/5
      * @param resourceId 资源ID
      */
    void activeResource(Long resourceId);

    /**
      * @description 查询资源树结构.
      * @author wjd
      * @date 2022/8/5
      * @param request 查询条件
      * @return 资源树结构
      */
    List<ResourceResponse> getResourceTree(ResourceRequest request);

    /**
      * @description 根据自定义条件查询资源信息.
      * @author wjd
      * @date 2022/9/6
      * @param request 查询条件
      * @return 资源信息
      */
    ResourceResponse getResourceByParams(ResourceRequest request);
}
