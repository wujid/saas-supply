package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.system.model.po.DictCategoryPo;
import com.supply.system.model.request.DictCategoryRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-11-08
 */
public interface IDictCategoryRepository extends IService<DictCategoryPo> {

    /**
      * @description 根据自定义条件修改.
      * @author wjd
      * @date 2022/11/8
      * @param dictCategoryPo 修改实体
      * @param request 条件值
      * @return int 受影响行数
      */
    int updateByParams(DictCategoryPo dictCategoryPo, @IgnoreFill DictCategoryRequest request);

    /**
     * @description 根据自定义条件查询条数信息.
     * @author wjd
     * @date 2022/11/8
     * @param request 查询条件
     * @return 条数
     */
    Long getCountByParams(DictCategoryRequest request);

    /**
     * @author wjd
     * @description 根据条件查询唯一字典分类信息.
     * @date 2022/11/8
     * @param request 待查询的条件
     * @return 字典分类信息
     **/
    DictCategoryPo getByParams(DictCategoryRequest request);

    /**
     * @author wjd
     * @description 根据条件查询字典分类信息集.
     * @date 2022/11/8
     * @param request 待查询的条件
     * @return 字典分类信息集
     **/
    List<DictCategoryPo> getListByParams(DictCategoryRequest request);

    /**
     * @description 根据自定义条件查询带分页的字典分类信息集.
     * @author wjd
     * @date 2022/8/29
     * @param page 分页条件
     * @param request 自定义查询条件
     * @return 带分页的字典分类信息集
     */
    Page<DictCategoryPo> getPageByParams(Page<DictCategoryPo> page, DictCategoryRequest request);
}
