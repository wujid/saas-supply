package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.system.mapper.DataScopeTypeMapper;
import com.supply.system.model.po.DataScopeTypePo;
import com.supply.system.model.request.DataScopeTypeRequest;
import com.supply.system.repository.IDataScopeTypeRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wjd
 * @description
 * @date 2022-09-08
 */
@Repository
public class DataScopeTypeRepositoryImpl extends ServiceImpl<DataScopeTypeMapper, DataScopeTypePo> implements IDataScopeTypeRepository {

    private final DataScopeTypeMapper dataScopeTypeMapper;

    public DataScopeTypeRepositoryImpl(DataScopeTypeMapper dataScopeTypeMapper) {
        this.dataScopeTypeMapper = dataScopeTypeMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(DataScopeTypePo entity) {
        return super.save(entity);
    }


    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(DataScopeTypePo entity) {
        return super.updateById(entity);
    }

    @Override
    public DataScopeTypePo getByParams(DataScopeTypeRequest request) {
        final LambdaQueryWrapper<DataScopeTypePo> queryWrapper = this.getQueryWrapper(request);
        return dataScopeTypeMapper.selectOne(queryWrapper);
    }

    @Override
    public Long getCountByParams(DataScopeTypeRequest request) {
        final LambdaQueryWrapper<DataScopeTypePo> queryWrapper = this.getQueryWrapper(request);
        return dataScopeTypeMapper.selectCount(queryWrapper);
    }

    @Override
    public Page<DataScopeTypePo> getPageByParams(Page<DataScopeTypePo> page, DataScopeTypeRequest request) {
        final LambdaQueryWrapper<DataScopeTypePo> queryWrapper = this.getQueryWrapper(request);
        return dataScopeTypeMapper.selectPage(page, queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/9/8
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<DataScopeTypePo> getQueryWrapper(DataScopeTypeRequest request) {
        LambdaQueryWrapper<DataScopeTypePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), DataScopeTypePo::getId, request.getId());
        queryWrapper.eq(null != request.getUserId(), DataScopeTypePo::getUserId, request.getUserId());
        queryWrapper.eq(null != request.getResourceId(), DataScopeTypePo::getResourceId, request.getResourceId());
        queryWrapper.eq(null != request.getDataScopeType(), DataScopeTypePo::getDataScopeType, request.getDataScopeType());
        queryWrapper.eq(null != request.getTenantId(), DataScopeTypePo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), DataScopeTypePo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(), request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
