package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.CategoryPo;
import com.supply.bpm.model.request.CategoryRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
public interface ICategoryRepository extends IService<CategoryPo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/11/3
     * @param categoryPo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(CategoryPo categoryPo, CategoryRequest request);

    /**
     * @description 根据自定义条件查询流程定义信息.
     * @author wjd
     * @date 2022/10/10
     * @param request 查询条件
     * @return 流程定义信息
     */
    CategoryPo getByParams(CategoryRequest request);

    /**
     * @description 根据自定义条件查询流程定义信息集.
     * @author wjd
     * @date 2022/10/10
     * @param request 查询条件
     * @return 流程定义信息集
     */
    List<CategoryPo> getListByParams(CategoryRequest request);

    /**
     * @description 根据自定义条件查询带分页信息.
     * @author wjd
     * @date 2022/11/1
     * @param page 分页信息
     * @param request 查询条件
     * @return 带分页的结果
     */
    Page<CategoryPo> getPageByParams(Page<CategoryPo> page, CategoryRequest request);
}
