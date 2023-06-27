package com.supply.message.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.message.mapper.TemplateInfoMapper;
import com.supply.message.model.po.TemplateInfoPo;
import com.supply.message.model.request.TemplateInfoRequest;
import com.supply.message.repository.ITemplateInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author wjd
 * @description
 * @date 2022-10-10
 */
@Repository
public class TemplateInfoRepositoryImpl extends ServiceImpl<TemplateInfoMapper, TemplateInfoPo> implements ITemplateInfoRepository {

    private final TemplateInfoMapper templateInfoMapper;

    public TemplateInfoRepositoryImpl(TemplateInfoMapper templateInfoMapper) {
        this.templateInfoMapper = templateInfoMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(TemplateInfoPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<TemplateInfoPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(TemplateInfoPo entity) {
        return super.updateById(entity);
    }

    @Override
    public TemplateInfoPo getByParams(TemplateInfoRequest request) {
        final LambdaQueryWrapper<TemplateInfoPo> queryWrapper = this.getQueryWrapper(request);
        return templateInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public Long getCountByParams(TemplateInfoRequest request) {
        final LambdaQueryWrapper<TemplateInfoPo> queryWrapper = this.getQueryWrapper(request);
        return templateInfoMapper.selectCount(queryWrapper);
    }

    @Override
    public Page<TemplateInfoPo> getPageByParams(Page<TemplateInfoPo> page, TemplateInfoRequest request) {
        final LambdaQueryWrapper<TemplateInfoPo> queryWrapper = this.getQueryWrapper(request);
        return templateInfoMapper.selectPage(page, queryWrapper);
    }




    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/10/10
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<TemplateInfoPo> getQueryWrapper(TemplateInfoRequest request) {
        LambdaQueryWrapper<TemplateInfoPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), TemplateInfoPo::getId, request.getId());
        queryWrapper.eq(null != request.getCode(), TemplateInfoPo::getCode, request.getCode());
        queryWrapper.eq(null != request.getMsgType(), TemplateInfoPo::getMsgType, request.getMsgType());
        queryWrapper.ne(null != request.getNeId(), TemplateInfoPo::getId, request.getNeId());
        queryWrapper.apply(null != request.getAuthSql(), request.getAuthSql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.eq(null != request.getStatus(), TemplateInfoPo::getStatus, request.getStatus());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
