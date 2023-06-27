package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.system.mapper.DictCategoryMapper;
import com.supply.system.model.po.DictCategoryPo;
import com.supply.system.model.request.DictCategoryRequest;
import com.supply.system.repository.IDictCategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-11-08
 */
@Repository
public class DictCategoryRepositoryImpl extends ServiceImpl<DictCategoryMapper, DictCategoryPo> implements IDictCategoryRepository {

    private final DictCategoryMapper dictCategoryMapper;

    public DictCategoryRepositoryImpl(DictCategoryMapper dictCategoryMapper) {
        this.dictCategoryMapper = dictCategoryMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(DictCategoryPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<DictCategoryPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(DictCategoryPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<DictCategoryPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(DictCategoryPo dictCategoryPo, @IgnoreFill DictCategoryRequest request) {
        final LambdaQueryWrapper<DictCategoryPo> queryWrapper = this.getQueryWrapper(request);
        return dictCategoryMapper.update(dictCategoryPo, queryWrapper);
    }

    @Override
    public Long getCountByParams(DictCategoryRequest request) {
        final LambdaQueryWrapper<DictCategoryPo> queryWrapper = this.getQueryWrapper(request);
        return dictCategoryMapper.selectCount(queryWrapper);
    }

    @Override
    public DictCategoryPo getByParams(DictCategoryRequest request) {
        final LambdaQueryWrapper<DictCategoryPo> queryWrapper = this.getQueryWrapper(request);
        return dictCategoryMapper.selectOne(queryWrapper);
    }

    @Override
    public List<DictCategoryPo> getListByParams(DictCategoryRequest request) {
        final LambdaQueryWrapper<DictCategoryPo> queryWrapper = this.getQueryWrapper(request);
        return dictCategoryMapper.selectList(queryWrapper);
    }

    @Override
    public Page<DictCategoryPo> getPageByParams(Page<DictCategoryPo> page, DictCategoryRequest request) {
        final LambdaQueryWrapper<DictCategoryPo> queryWrapper = this.getQueryWrapper(request);
        return dictCategoryMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/11/8
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<DictCategoryPo> getQueryWrapper(DictCategoryRequest request) {
        LambdaQueryWrapper<DictCategoryPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), DictCategoryPo::getId, request.getId());
        queryWrapper.eq(null != request.getParentId(), DictCategoryPo::getParentId, request.getParentId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), DictCategoryPo::getName, request.getName());
        queryWrapper.eq(StrUtil.isNotBlank(request.getCode()), DictCategoryPo::getCode, request.getCode());
        queryWrapper.eq(null != request.getStatus(), DictCategoryPo::getStatus, request.getStatus());
        queryWrapper.eq(null != request.getLevel(), DictCategoryPo::getLevel, request.getLevel());
        queryWrapper.eq(null != request.getHasChildren(), DictCategoryPo::getHasChildren, request.getHasChildren());
        queryWrapper.ne(null != request.getNeId(), DictCategoryPo::getId, request.getNeId());
        queryWrapper.like(StrUtil.isNotBlank(request.getLikeName()), DictCategoryPo::getName, request.getLikeName());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
