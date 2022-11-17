package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.DictItemPo;
import com.supply.system.model.request.DictItemRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
public interface IDictItemRepository extends IService<DictItemPo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/8/29
     * @param dictItemPo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(DictItemPo dictItemPo, DictItemRequest request);

    /**
     * @description 根据自定义条件查询条数信息.
     * @author wjd
     * @date 2022/8/29
     * @param request 查询条件
     * @return int 条数信息
     */
    Long getCountByParams(DictItemRequest request);

    /**
     * @author wjd
     * @description 根据条件查询唯一的租户信息.
     * @date 2022/8/29
     * @param request 待查询的条件
     * @return 租户信息
     **/
    DictItemPo getByParams(DictItemRequest request);

    /**
     * @description 根据自定义条件查询信息集.
     * @author wjd
     * @date 2022/8/29
     * @param request 查询条件
     * @return 角色信息集
     */
    List<DictItemPo> getListByParams(DictItemRequest request);

    /**
     * @description 根据自定义条件查询带分页的角色信息集.
     * @author wjd
     * @date 2022/8/29
     * @param page 分页条件
     * @param request 自定义查询条件
     * @return 带分页的角色信息集
     */
    Page<DictItemPo> getPageByParams(Page<DictItemPo> page, DictItemRequest request);
}
