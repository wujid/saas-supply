package com.supply.bpm.service;

import com.supply.bpm.model.request.CategoryRequest;
import com.supply.bpm.model.response.CategoryResponse;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
public interface ICategoryService {

    /**
      * @description 新增流程分类.
      * @author wjd
      * @date 2022/12/20
      * @param request 待新增实体
      */
    void addCategory(CategoryRequest request);

    /**
      * @description 修改流程分类.
      * @author wjd
      * @date 2022/12/20
      * @param request 待修改实体
      */
    void updateCategory(CategoryRequest request);

    /**
      * @description 删除流程分类.
      * @author wjd
      * @date 2022/12/20
      * @param categoryId 流程分类ID
      */
    void delCategory(Long categoryId);

    /**
      * @description 获取流程分类树结构.
      * @author wjd
      * @date 2022/12/20
      * @param parentId 父ID条件
      * @param name 名称条件
      * @return java.util.List<com.supply.bpm.model.response.CategoryResponse>
      */
    List<CategoryResponse> getCategoryTree(Long parentId, String name);
}
