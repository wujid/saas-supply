package com.supply.message.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.message.mapper.TemplateTenantMapper;
import com.supply.message.model.po.TemplateTenantPo;
import com.supply.message.model.request.TemplateTenantRequest;
import com.supply.message.repository.ITemplateTenantRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-10-31
 */
@Repository
public class TemplateTenantRepositoryImpl extends ServiceImpl<TemplateTenantMapper, TemplateTenantPo> implements ITemplateTenantRepository {

    private final TemplateTenantMapper templateTenantMapper;

    public TemplateTenantRepositoryImpl(TemplateTenantMapper templateTenantMapper) {
        this.templateTenantMapper = templateTenantMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(TemplateTenantPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<TemplateTenantPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean updateById(TemplateTenantPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean updateBatchById(Collection<TemplateTenantPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(TemplateTenantPo templateTenantPo, @IgnoreFill TemplateTenantRequest request) {
        final LambdaQueryWrapper<TemplateTenantPo> queryWrapper = this.getQueryWrapper(request);
        return templateTenantMapper.update(templateTenantPo, queryWrapper);
    }

    @Override
    public List<TemplateTenantPo> getListByParams(TemplateTenantRequest request) {
        final LambdaQueryWrapper<TemplateTenantPo> queryWrapper = this.getQueryWrapper(request);
        return templateTenantMapper.selectList(queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/9/8
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<TemplateTenantPo> getQueryWrapper(TemplateTenantRequest request) {
        LambdaQueryWrapper<TemplateTenantPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), TemplateTenantPo::getId, request.getId());
        queryWrapper.eq(null != request.getTemplateId(), TemplateTenantPo::getTemplateId, request.getTemplateId());
        queryWrapper.eq(null != request.getTenantId(), TemplateTenantPo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getStatus(), TemplateTenantPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(), request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
