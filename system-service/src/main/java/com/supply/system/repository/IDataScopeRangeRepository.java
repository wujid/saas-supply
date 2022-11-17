package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.DataScopeRangePo;
import com.supply.system.model.request.DataScopeRangeRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-08
 */
public interface IDataScopeRangeRepository extends IService<DataScopeRangePo> {


    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/8/29
     * @param dataScopeRangePo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(DataScopeRangePo dataScopeRangePo, DataScopeRangeRequest request);

    /**
     * @author wjd
     * @description 根据条件查询唯一的用户数据权限范围信息.
     * @date 2022/9/8
     * @param request 待查询的条件
     * @return 用户数据权限范围信息
     **/
    DataScopeRangePo getByParams(DataScopeRangeRequest request);

    /**
     * @author wjd
     * @description 根据条件查询用户数据权限范围信息集.
     * @date 2022/9/8
     * @param request 待查询的条件
     * @return 用户数据权限范围信息集
     **/
    List<DataScopeRangePo> getListByParams(DataScopeRangeRequest request);
}
