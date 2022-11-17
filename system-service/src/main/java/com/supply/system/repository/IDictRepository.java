package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.DictPo;
import com.supply.system.model.request.DictRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
public interface IDictRepository extends IService<DictPo> {


    /**
      * @description 根据自定义条件查询条数信息.
      * @author wjd
      * @date 2022/11/7
      * @param request 查询条件
      * @return 条数
      */
    Long getCountByParams(DictRequest request);

    /**
     * @author wjd
     * @description 根据条件查询唯一的数据字典信息.
     * @date 2022/8/29
     * @param request 待查询的条件
     * @return 数据字典信息
     **/
    DictPo getByParams(DictRequest request);

    /**
     * @description 根据自定义条件查询信息集.
     * @author wjd
     * @date 2022/8/29
     * @param request 查询条件
     * @return 数据字典信息集
     */
    List<DictPo> getListByParams(DictRequest request);

    /**
     * @description 根据自定义条件查询带分页的数据字典信息集.
     * @author wjd
     * @date 2022/8/29
     * @param page 分页条件
     * @param request 自定义查询条件
     * @return 带分页的数据字典信息集
     */
    Page<DictPo> getPageByParams(Page<DictPo> page, DictRequest request);
}
