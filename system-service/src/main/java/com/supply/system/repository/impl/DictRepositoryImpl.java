package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.system.mapper.DictMapper;
import com.supply.system.model.po.DictPo;
import com.supply.system.model.request.DictRequest;
import com.supply.system.repository.IDictRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
@Repository
public class DictRepositoryImpl extends ServiceImpl<DictMapper, DictPo> implements IDictRepository {

    private final DictMapper dictMapper;

    public DictRepositoryImpl(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(DictPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(DictPo entity) {
        return super.updateById(entity);
    }

    @Override
    public Long getCountByParams(DictRequest request) {
        final LambdaQueryWrapper<DictPo> queryWrapper = this.getQueryWrapper(request);
        return dictMapper.selectCount(queryWrapper);
    }

    @Override
    public DictPo getByParams(DictRequest request) {
        final LambdaQueryWrapper<DictPo> queryWrapper = this.getQueryWrapper(request);
        return dictMapper.selectOne(queryWrapper);
    }

    @Override
    public List<DictPo> getListByParams(DictRequest request) {
        final LambdaQueryWrapper<DictPo> queryWrapper = this.getQueryWrapper(request);
        return dictMapper.selectList(queryWrapper);
    }

    @Override
    public Page<DictPo> getPageByParams(Page<DictPo> page, DictRequest request) {
        final LambdaQueryWrapper<DictPo> queryWrapper = this.getQueryWrapper(request);
        return dictMapper.selectPage(page, queryWrapper);
    }



    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/8/3
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<DictPo> getQueryWrapper(DictRequest request) {
        LambdaQueryWrapper<DictPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), DictPo::getId, request.getId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), DictPo::getName, request.getName());
        queryWrapper.eq(StrUtil.isNotBlank(request.getCode()), DictPo::getCode, request.getCode());
        queryWrapper.eq(null != request.getStatus(), DictPo::getStatus, request.getStatus());
        queryWrapper.ne(null != request.getNeId(), DictPo::getId, request.getNeId());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getCodeList()), DictPo::getCode, request.getCodeList());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
