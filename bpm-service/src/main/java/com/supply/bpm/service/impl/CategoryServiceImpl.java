package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.supply.bpm.cvt.CategoryCvt;
import com.supply.bpm.model.po.CategoryPo;
import com.supply.bpm.model.request.CategoryRequest;
import com.supply.bpm.model.response.CategoryResponse;
import com.supply.bpm.repository.ICategoryRepository;
import com.supply.bpm.service.ICategoryService;
import com.supply.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
@Service
public class CategoryServiceImpl implements ICategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final ICategoryRepository categoryRepository;

    public CategoryServiceImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(CategoryRequest request) {
        logger.info("[新增流程分类]---实体信息为{}", JSON.toJSONString(request));
        if (null == request.getParentId()) {
            request.setParentId(0L);
        }
        final CategoryPo categoryPo = CategoryCvt.INSTANCE.requestToPo(request);
        categoryRepository.save(categoryPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryRequest request) {
        logger.info("[修改流程分类]---实体信息为{}", JSON.toJSONString(request));
        final CategoryPo categoryPo = CategoryCvt.INSTANCE.requestToPo(request);
        categoryRepository.updateById(categoryPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCategory(Long categoryId) {
        logger.info("[删除流程分类]---待删除的ID为{}", categoryId);
        final CategoryPo categoryPo = new CategoryPo();
        categoryPo.setId(categoryId);
        categoryPo.setStatus(Constant.STATUS_DEL);
        categoryRepository.updateById(categoryPo);
    }

    @Override
    public List<CategoryResponse> getCategoryTree(Long parentId, String name) {
        List<CategoryResponse> result = new ArrayList<>();
        CategoryRequest request = new CategoryRequest();
        request.setParentId(parentId);
        request.setOrderColumn(CategoryPo::getSort);
        request.setIsAsc(true);
        final List<CategoryPo> categoryPoList = categoryRepository.getListByParams(request);
        if (CollectionUtil.isEmpty(categoryPoList)) {
            return result;
        }
        // 实体转换
        final List<CategoryResponse> categoryResponseList = CategoryCvt.INSTANCE.poToResponseBatch(categoryPoList);
        // 获取根节点集
        if (null == parentId) {
            parentId = 0L;
        }
        final Long finalParentId = parentId;
        List<CategoryResponse> rootList = categoryResponseList.stream().
                filter(orgResponse -> Objects.equals(orgResponse.getParentId(), finalParentId)).collect(Collectors.toList());
        // 叶子节点
        result.addAll(rootList);
        this.getLeafNode(rootList, categoryResponseList);
        // 存在名称模糊查询条件则进行过滤
        if (StrUtil.isNotBlank(name)) {
            this.treeMatch(result, name);
        }
        return result;
    }

    private void getLeafNode(List<CategoryResponse> rootList, List<CategoryResponse> categoryResponseList) {
        for (CategoryResponse categoryResponse : rootList) {
            final Long parentId = categoryResponse.getId();
            final String parentName = categoryResponse.getName();
            // 获取该叶子节点的子集
            final List<CategoryResponse> childrenList = categoryResponseList.stream()
                    .filter(category -> Objects.equals(category.getParentId(), parentId))
                    .collect(Collectors.toList());
            childrenList.forEach(org -> org.setParentName(parentName));
            categoryResponse.setChildrenList(childrenList);
            this.getLeafNode(childrenList, categoryResponseList);
        }
    }

    /**
     * @description 过滤树形结构.
     * @author wjd
     * @date 2022/8/16
     * @param categoryList 待过滤的流程分类树结构
     *  @param name 匹配条件
     */
    private  void treeMatch(List<CategoryResponse> categoryList, String name) {
        Iterator<CategoryResponse> oneIter = categoryList.iterator();
        while (oneIter.hasNext()) {
            CategoryResponse oneCategory = oneIter.next();
            final List<CategoryResponse> childrenList = oneCategory.getChildrenList();
            // 如果包含则什么也不做(不移除),否则就看子级目录
            if(!oneCategory.getName().contains(name)){
                if(CollectionUtil.isNotEmpty(childrenList)){
                    this.treeMatch(childrenList, name);
                }
                // 如果子级目录全部被移除,则移除父级目录
                if(CollectionUtil.isEmpty(childrenList)){
                    oneIter.remove();
                }
            }
        }
    }
}
