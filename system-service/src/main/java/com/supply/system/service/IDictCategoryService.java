package com.supply.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.system.model.request.DictCategoryRequest;
import com.supply.system.model.response.DictCategoryResponse;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-11-08
 */
public interface IDictCategoryService {

    /**
      * @description 新增字典分类.
      * @author wjd
      * @date 2022/11/8
      * @param request 待新增实体
      */
    void addDictCategory(DictCategoryRequest request);

    /**
      * @description 修改字典分类.
      * @author wjd
      * @date 2022/11/8
      * @param request 待修改实体
      */
    void updateDictCategory(DictCategoryRequest request);

    /**
      * @description 根据ID删除字典分类.
      * @author wjd
      * @date 2022/11/8
      * @param dictCategoryId 字典分类ID
      */
    void delDictCategory(Long dictCategoryId);

    /**
     * @description 根据自定义条件查询带分页的字典分类信息集.
     * @author wjd
     * @date 202211/8
     * @param request 查询条件
     * @return 带分页的字典分类信息集
     */
    IPage<DictCategoryResponse> getPageByParams(DictCategoryRequest request);

    /**
      * @description 根据自定义条件查询字典分类信息集.
      * @author wjd
      * @date 2022/11/8
      * @param request 查询条件
      * @return 字典分类信息集
      */
    List<DictCategoryResponse> getListByParams(DictCategoryRequest request);
}
