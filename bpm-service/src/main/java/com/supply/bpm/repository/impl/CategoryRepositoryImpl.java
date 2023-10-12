package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.CategoryMapper;
import com.supply.bpm.model.po.CategoryPo;
import com.supply.bpm.model.request.CategoryRequest;
import com.supply.bpm.repository.ICategoryRepository;
import com.supply.common.constant.Constant;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
@Repository
public class CategoryRepositoryImpl extends ServiceImpl<CategoryMapper, CategoryPo> implements ICategoryRepository {

    private final CategoryMapper categoryMapper;

    public CategoryRepositoryImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(CategoryPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<CategoryPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(CategoryPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<CategoryPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(CategoryPo categoryPo, @IgnoreFill CategoryRequest request) {
        final LambdaQueryWrapper<CategoryPo> queryWrapper = this.getQueryWrapper(request);
        return categoryMapper.update(categoryPo, queryWrapper);
    }

    @Override
    public CategoryPo getByParams(CategoryRequest request) {
        final LambdaQueryWrapper<CategoryPo> queryWrapper = this.getQueryWrapper(request);
        return categoryMapper.selectOne(queryWrapper);
    }

    @Override
    public List<CategoryPo> getListByParams(CategoryRequest request) {
        final LambdaQueryWrapper<CategoryPo> queryWrapper = this.getQueryWrapper(request);
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public Page<CategoryPo> getPageByParams(Page<CategoryPo> page, CategoryRequest request) {
        final LambdaQueryWrapper<CategoryPo> queryWrapper = this.getQueryWrapper(request);
        return categoryMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<CategoryPo> getListByTenantId(Long tenantId) {
        final CategoryRequest request = new CategoryRequest();
        request.setTenantId(tenantId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final LambdaQueryWrapper<CategoryPo> queryWrapper = this.getQueryWrapper(request);
        return categoryMapper.selectList(queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/12
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<CategoryPo> getQueryWrapper(CategoryRequest request) {
        LambdaQueryWrapper<CategoryPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), CategoryPo::getId, request.getId());
        queryWrapper.eq(null != request.getParentId(), CategoryPo::getParentId, request.getParentId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getCode()), CategoryPo::getCode, request.getCode());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), CategoryPo::getName, request.getName());
        queryWrapper.eq(null != request.getSort(), CategoryPo::getSort, request.getSort());
        queryWrapper.eq(null != request.getStatus(), CategoryPo::getStatus, request.getStatus());
        queryWrapper.eq(null != request.getTenantId(), CategoryPo::getTenantId, request.getTenantId());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
