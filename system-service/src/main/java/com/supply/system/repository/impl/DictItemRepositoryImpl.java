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
import com.supply.system.mapper.DictItemMapper;
import com.supply.system.model.po.DictItemPo;
import com.supply.system.model.request.DictItemRequest;
import com.supply.system.repository.IDictItemRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
@Repository
public class DictItemRepositoryImpl extends ServiceImpl<DictItemMapper, DictItemPo> implements IDictItemRepository {

    private final DictItemMapper dictItemMapper;

    public DictItemRepositoryImpl(DictItemMapper dictItemMapper) {
        this.dictItemMapper = dictItemMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(DictItemPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<DictItemPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(DictItemPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(DictItemPo dictItemPo, @IgnoreFill DictItemRequest request) {
        final LambdaQueryWrapper<DictItemPo> queryWrapper = this.getQueryWrapper(request);
        return dictItemMapper.update(dictItemPo, queryWrapper);
    }

    @Override
    public Long getCountByParams(DictItemRequest request) {
        final LambdaQueryWrapper<DictItemPo> queryWrapper = this.getQueryWrapper(request);
        return dictItemMapper.selectCount(queryWrapper);
    }

    @Override
    public DictItemPo getByParams(DictItemRequest request) {
        final LambdaQueryWrapper<DictItemPo> queryWrapper = this.getQueryWrapper(request);
        return dictItemMapper.selectOne(queryWrapper);
    }

    @Override
    public List<DictItemPo> getListByParams(DictItemRequest request) {
        final LambdaQueryWrapper<DictItemPo> queryWrapper = this.getQueryWrapper(request);
        return dictItemMapper.selectList(queryWrapper);
    }

    @Override
    public Page<DictItemPo> getPageByParams(Page<DictItemPo> page, DictItemRequest request) {
        final LambdaQueryWrapper<DictItemPo> queryWrapper = this.getQueryWrapper(request);
        return dictItemMapper.selectPage(page, queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/4/13
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<DictItemPo> getQueryWrapper(DictItemRequest request) {
        LambdaQueryWrapper<DictItemPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), DictItemPo::getId, request.getId());
        queryWrapper.eq(null != request.getDictId(), DictItemPo::getDictId, request.getDictId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getLabel()), DictItemPo::getLabel, request.getLabel());
        queryWrapper.eq(StrUtil.isNotBlank(request.getValue()), DictItemPo::getValue, request.getValue());
        queryWrapper.eq(null != request.getSort(), DictItemPo::getSort, request.getSort());
        queryWrapper.eq(null != request.getStatus(), DictItemPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
