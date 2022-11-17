package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.DataScopeTypePo;
import com.supply.system.model.request.DataScopeTypeRequest;

/**
 * @author wjd
 * @description
 * @date 2022-09-08
 */
public interface IDataScopeTypeRepository extends IService<DataScopeTypePo> {

    /**
     * @author wjd
     * @description 根据条件查询唯一的用户资源数据权限类型信息.
     * @date 2022/9/8
     * @param request 待查询的条件
     * @return 用户数据权限类型信息
     **/
    DataScopeTypePo getByParams(DataScopeTypeRequest request);

    /**
      * @description 根据自定义条件查询条数信息.
      * @author wjd
      * @date 2022/9/30
      * @param request 自定义查询条件
      * @return 条数
      */
    Long getCountByParams(DataScopeTypeRequest request);

    /**
      * @description 根据自定义条件查询带分页的用户资源数据权限类型信息集.
      * @author wjd
      * @date 2022/9/8
      * @param page 分页条件
      * @param request 自定义查询条件
      * @return 带分页的用户资源数据权限类型信息集
      */
    Page<DataScopeTypePo> getPageByParams(Page<DataScopeTypePo> page, DataScopeTypeRequest request);
}
