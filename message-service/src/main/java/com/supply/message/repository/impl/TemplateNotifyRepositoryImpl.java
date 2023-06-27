package com.supply.message.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.message.mapper.TemplateNotifyMapper;
import com.supply.message.model.po.TemplateNotifyPo;
import com.supply.message.model.request.TemplateNotifyRequest;
import com.supply.message.repository.ITemplateNotifyRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-10-29
 */
@Repository
public class TemplateNotifyRepositoryImpl extends ServiceImpl<TemplateNotifyMapper, TemplateNotifyPo> implements ITemplateNotifyRepository {

    private final TemplateNotifyMapper templateNotifyMapper;

    public TemplateNotifyRepositoryImpl(TemplateNotifyMapper templateNotifyMapper) {
        this.templateNotifyMapper = templateNotifyMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(TemplateNotifyPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<TemplateNotifyPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(TemplateNotifyPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<TemplateNotifyPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(TemplateNotifyPo templateNotifyPo, @IgnoreFill TemplateNotifyRequest request) {
        final LambdaQueryWrapper<TemplateNotifyPo> queryWrapper = this.getQueryWrapper(request);
        return templateNotifyMapper.update(templateNotifyPo, queryWrapper);
    }

    @Override
    public List<TemplateNotifyPo> getListByParams(TemplateNotifyRequest request) {
        final LambdaQueryWrapper<TemplateNotifyPo> queryWrapper = this.getQueryWrapper(request);
        return templateNotifyMapper.selectList(queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/9/8
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<TemplateNotifyPo> getQueryWrapper(TemplateNotifyRequest request) {
        LambdaQueryWrapper<TemplateNotifyPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), TemplateNotifyPo::getId, request.getId());
        queryWrapper.eq(null != request.getTemplateId(), TemplateNotifyPo::getTemplateId, request.getTemplateId());
        queryWrapper.eq(null != request.getNotifyType(), TemplateNotifyPo::getNotifyType, request.getNotifyType());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNotifyTemplate()), TemplateNotifyPo::getNotifyTemplate, request.getNotifyTemplate());
        queryWrapper.eq(null != request.getStatus(), TemplateNotifyPo::getStatus, request.getStatus());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(), request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
