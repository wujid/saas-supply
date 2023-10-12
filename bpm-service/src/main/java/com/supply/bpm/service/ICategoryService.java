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
      * @return 流程分类树
      */
    List<CategoryResponse> getCategoryTree(Long parentId, String name, Long tenantId);

    /**
      * @description 流程分类行.
      * @author wjd
      * @date 2023/10/12
      * @param tenantId 租户ID
      * @param name 名称条件
      * @return 流程分类行
      */
    List<CategoryResponse> getCategoryRows(Long tenantId, String name);
}
