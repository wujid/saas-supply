package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.system.mapper.DataScopeRangeMapper;
import com.supply.system.model.po.DataScopeRangePo;
import com.supply.system.model.request.DataScopeRangeRequest;
import com.supply.system.repository.IDataScopeRangeRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-08
 */
@Repository
public class DataScopeRangeRepositoryImpl extends ServiceImpl<DataScopeRangeMapper, DataScopeRangePo> implements IDataScopeRangeRepository {

    private final DataScopeRangeMapper dataScopeRangeMapper;

    public DataScopeRangeRepositoryImpl(DataScopeRangeMapper dataScopeRangeMapper) {
        this.dataScopeRangeMapper = dataScopeRangeMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(DataScopeRangePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<DataScopeRangePo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(DataScopeRangePo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<DataScopeRangePo> entityList, int batchSize) {
        return super.updateBatchById(entityList, batchSize);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(DataScopeRangePo dataScopeRangePo, @IgnoreFill DataScopeRangeRequest request) {
        final LambdaQueryWrapper<DataScopeRangePo> queryWrapper = this.getQueryWrapper(request);
        return dataScopeRangeMapper.update(dataScopeRangePo, queryWrapper);
    }

    @Override
    public DataScopeRangePo getByParams(DataScopeRangeRequest request) {
        final LambdaQueryWrapper<DataScopeRangePo> queryWrapper = this.getQueryWrapper(request);
        return dataScopeRangeMapper.selectOne(queryWrapper);
    }

    @Override
    public List<DataScopeRangePo> getListByParams(DataScopeRangeRequest request) {
        final LambdaQueryWrapper<DataScopeRangePo> queryWrapper = this.getQueryWrapper(request);
        return dataScopeRangeMapper.selectList(queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/9/8
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<DataScopeRangePo> getQueryWrapper(DataScopeRangeRequest request) {
        LambdaQueryWrapper<DataScopeRangePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), DataScopeRangePo::getId, request.getId());
        queryWrapper.eq(null != request.getDataScopeTypeId(), DataScopeRangePo::getDataScopeTypeId, request.getDataScopeTypeId());
        queryWrapper.eq(null != request.getDataScopeRange(), DataScopeRangePo::getDataScopeRange, request.getDataScopeRange());
        queryWrapper.eq(null != request.getDataScopeId(), DataScopeRangePo::getDataScopeId, request.getDataScopeId());
        queryWrapper.eq(null != request.getStatus(), DataScopeRangePo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(), request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
