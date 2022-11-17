package com.supply.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.system.model.request.DataScopeRangeRequest;
import com.supply.system.model.request.DataScopeTypeRequest;
import com.supply.system.model.response.DataScopeRangeResponse;
import com.supply.system.model.response.DataScopeTypeResponse;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-08
 */
public interface IDataScopeService {

    /**
      * @description 新增用户资源数据权限.
      * @author wjd
      * @date 2022/9/8
      * @param request 待新增的实体
      */
    void addDataScope(DataScopeTypeRequest request);


    /**
     * @description 修改用户资源数据权限.
     * @author wjd
     * @date 2022/9/8
     * @param request 待新增的实体
     */
    void updateDataScope(DataScopeTypeRequest request);

    /**
      * @description 删除用户资源数据权限.
      * @author wjd
      * @date 2022/9/8
      * @param dataScopeTypeId 用户资源数据权限类型ID
      */
    void delDataScopeById(Long dataScopeTypeId);

    /**
      * @description 根据自定义条件查询带分页的用户资源数据权限信息集.
      * @author wjd
      * @date 2022/9/8
      * @param request 查询条件
      * @return 带分页的用户资源数据权限信息集
      */
    IPage<DataScopeTypeResponse> getPageByParams(DataScopeTypeRequest request);

    /**
      * @description 根据自定义条件查询用户资源权限信息.
      * @author wjd
      * @date 2022/9/8
      * @param request 查询条件
      * @return 用户资源权限信息
      */
    DataScopeTypeResponse getDataScopeByParams(DataScopeTypeRequest request);

    /**
      * @description 根据自定义条件查询数据权限范围信息集.
      * @author wjd
      * @date 2022/9/27
      * @param request 查询条件
      * @return 权限范围信息集
      */
    List<DataScopeRangeResponse> getDataScopeRangeListByParams(DataScopeRangeRequest request);
}
