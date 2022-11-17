package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.system.mapper.OrgMapper;
import com.supply.system.model.po.OrgPo;
import com.supply.system.model.request.OrgRequest;
import com.supply.system.repository.IOrgRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-08-09
 */
@Repository
public class OrgRepositoryImpl extends ServiceImpl<OrgMapper, OrgPo> implements IOrgRepository {

    private final OrgMapper orgMapper;

    public OrgRepositoryImpl(OrgMapper orgMapper) {
        this.orgMapper = orgMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(OrgPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(OrgPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public int updateByParams(OrgPo roleResourcePo, @IgnoreFill OrgRequest request) {
        final LambdaQueryWrapper<OrgPo> queryWrapper = this.getQueryWrapper(request);
        return orgMapper.update(roleResourcePo, queryWrapper);
    }

    @Override
    public OrgPo getByParams(OrgRequest request) {
        final LambdaQueryWrapper<OrgPo> queryWrapper = this.getQueryWrapper(request);
        return orgMapper.selectOne(queryWrapper);
    }

    @Override
    public List<OrgPo> getListByParams(OrgRequest request) {
        final LambdaQueryWrapper<OrgPo> queryWrapper = this.getQueryWrapper(request);
        return orgMapper.selectList(queryWrapper);
    }

    @Override
    public Map<Long, OrgPo> getMapByIds(Set<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new HashMap<>();
        }
        final List<OrgPo> list = orgMapper.selectBatchIds(ids);
        if (CollectionUtil.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(OrgPo::getId, e -> e, (k1, k2) -> k1));
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/8/3
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<OrgPo> getQueryWrapper(OrgRequest request) {
        LambdaQueryWrapper<OrgPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), OrgPo::getId, request.getId());
        queryWrapper.eq(null != request.getParentId(), OrgPo::getParentId, request.getParentId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), OrgPo::getName, request.getName());
        queryWrapper.eq(null != request.getTenantId(), OrgPo::getTenantId, request.getTenantId());
        queryWrapper.eq(null != request.getBusinessStatus(), OrgPo::getBusinessStatus, request.getBusinessStatus());
        queryWrapper.eq(null != request.getStatus(), OrgPo::getStatus, request.getStatus());
        queryWrapper.eq(null != request.getIsMain(), OrgPo::getIsMain, request.getIsMain());
        queryWrapper.apply(null != request.getApplySql(), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(), request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
