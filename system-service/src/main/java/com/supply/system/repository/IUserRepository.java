package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.UserPo;
import com.supply.system.model.request.UserRequest;

import java.util.List;
import java.util.Map;

/**
 * @description: 用户信息数据仓储层接口.
 * @Author wjd
 * @date 2022/4/1
 */
public interface IUserRepository extends IService<UserPo> {


    /**
      * @description 根据条件查询条数信息.
      * @author wjd
      * @date 2022/8/4
      * @param request 待查询的条件
      * @return 条数信息
      */
    Long getCountByParams(UserRequest request);

    /**
     * @author wjd
     * @description 根据条件查询唯一的用户信息.
     * @date 2022/7/13
     * @param request 待查询的条件
     * @return 用户信息
     **/
    UserPo getByParams(UserRequest request);


    /**
     * @author wjd
     * @description 根据条件查询用户信息集.
     * @date 2022/7/13
     * @param request 待查询的条件
     * @return 用户信息集
     **/
    List<UserPo> getListByParams(UserRequest request);

    /**
     * @author wjd
     * @description 根据自定义条件查询带分页信息.
     * @date 2022/7/20
     * @param page 分页信息
     * @param request 查询条件
     * @return 带分页的结果
     **/
    Page<UserPo> getPageByParams(Page<UserPo> page, UserRequest request);

    /**
      * @description 根据自定义条件查询并转换成映射
      * @author wjd
      * @date 2022/9/8
      * @param request 查询条件
      * @return 映射 key:用户ID value: 用户信息
      */
    Map<Long, UserPo> getMapByParams(UserRequest request);

}
