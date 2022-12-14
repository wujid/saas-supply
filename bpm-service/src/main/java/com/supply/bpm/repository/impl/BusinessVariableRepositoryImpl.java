package com.supply.bpm.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.bpm.mapper.BusinessVariableMapper;
import com.supply.bpm.model.po.BusinessVariablePo;
import com.supply.bpm.model.request.BusinessVariableRequest;
import com.supply.bpm.repository.IBusinessVariableRepository;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-04
 */
@Repository
public class BusinessVariableRepositoryImpl extends ServiceImpl<BusinessVariableMapper, BusinessVariablePo> implements IBusinessVariableRepository {

    private final BusinessVariableMapper businessVariableMapper;

    public BusinessVariableRepositoryImpl(BusinessVariableMapper businessVariableMapper) {
        this.businessVariableMapper = businessVariableMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(BusinessVariablePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<BusinessVariablePo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(BusinessVariablePo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<BusinessVariablePo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public int updateByParams(BusinessVariablePo businessVariablePo, @IgnoreFill BusinessVariableRequest request) {
        final LambdaQueryWrapper<BusinessVariablePo> queryWrapper = this.getQueryWrapper(request);
        return businessVariableMapper.update(businessVariablePo, queryWrapper);
    }

    @Override
    public BusinessVariablePo getByParams(BusinessVariableRequest request) {
        final LambdaQueryWrapper<BusinessVariablePo> queryWrapper = this.getQueryWrapper(request);
        return businessVariableMapper.selectOne(queryWrapper);
    }

    @Override
    public List<BusinessVariablePo> getListByParams(BusinessVariableRequest request) {
        final LambdaQueryWrapper<BusinessVariablePo> queryWrapper = this.getQueryWrapper(request);
        return businessVariableMapper.selectList(queryWrapper);
    }

    @Override
    public Page<BusinessVariablePo> getPageByParams(Page<BusinessVariablePo> page, BusinessVariableRequest request) {
        final LambdaQueryWrapper<BusinessVariablePo> queryWrapper = this.getQueryWrapper(request);
        return businessVariableMapper.selectPage(page, queryWrapper);
    }


    /**
     * @author wjd
     * @description ????????????????????????.
     * @date 2022/10/12
     * @param request ??????
     * @return ????????????????????????
     **/
    private LambdaQueryWrapper<BusinessVariablePo> getQueryWrapper(BusinessVariableRequest request) {
        LambdaQueryWrapper<BusinessVariablePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), BusinessVariablePo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getDefinitionId()), BusinessVariablePo::getDefinitionId, request.getDefinitionId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getVariableKey()), BusinessVariablePo::getVariableKey, request.getVariableKey());
        queryWrapper.eq(StrUtil.isNotBlank(request.getVariableName()), BusinessVariablePo::getVariableName, request.getVariableName());
        queryWrapper.eq(null != request.getTenantId(), BusinessVariablePo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), BusinessVariablePo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
