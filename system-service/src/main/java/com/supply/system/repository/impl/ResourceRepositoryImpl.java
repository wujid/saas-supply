package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.system.mapper.ResourceMapper;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.request.ResourceRequest;
import com.supply.system.repository.IResourceRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
@Repository
public class ResourceRepositoryImpl extends ServiceImpl<ResourceMapper, ResourcePo> implements IResourceRepository {

    private final ResourceMapper resourceMapper;

    public ResourceRepositoryImpl(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(ResourcePo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<ResourcePo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(ResourcePo entity) {
        return super.updateById(entity);
    }

    @Override
    public List<ResourcePo> getListByParams(ResourceRequest request) {
        LambdaQueryWrapper<ResourcePo> queryWrapper = this.getQueryWrapper(request);
        return resourceMapper.selectList(queryWrapper);
    }

    @Override
    public ResourcePo getByParams(ResourceRequest request) {
        LambdaQueryWrapper<ResourcePo> queryWrapper = this.getQueryWrapper(request);
        return resourceMapper.selectOne(queryWrapper);
    }

    @Override
    public Long getCountByParams(ResourceRequest request) {
        LambdaQueryWrapper<ResourcePo> queryWrapper = this.getQueryWrapper(request);
        return resourceMapper.selectCount(queryWrapper);
    }

    @Override
    public List<ResourcePo> getByRoleIdList(List<Long> roleIdList, Integer type) {
        if (CollectionUtil.isEmpty(roleIdList)) {
            return null;
        }
        return resourceMapper.getByRoleIdList(roleIdList, type);
    }

    @Override
    public List<ResourcePo> getByTenantId(Long tenantId) {
        if (null == tenantId) {
            return null;
        }
        return resourceMapper.getByTenantId(tenantId);
    }

    @Override
    public Map<Long, ResourcePo> getMapByParams(ResourceRequest request) {
        Map<Long, ResourcePo> map = new HashMap<>();
        final List<ResourcePo> list = this.getListByParams(request);
        if (CollectionUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(ResourcePo::getId, e -> e, (k1, k2) -> k1));
        }
        return map;
    }


    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/8/3
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<ResourcePo> getQueryWrapper(ResourceRequest request) {
        LambdaQueryWrapper<ResourcePo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), ResourcePo::getId, request.getId());
        queryWrapper.eq(null != request.getParentId(), ResourcePo::getParentId, request.getParentId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getCode()), ResourcePo::getCode, request.getCode());
        queryWrapper.eq(StrUtil.isNotBlank(request.getName()), ResourcePo::getName, request.getName());
        queryWrapper.eq(null != request.getType(), ResourcePo::getType, request.getType());
        queryWrapper.eq(null != request.getBusinessStatus(), ResourcePo::getBusinessStatus, request.getBusinessStatus());
        queryWrapper.eq(null != request.getIsFix(), ResourcePo::getIsFix, request.getIsFix());
        queryWrapper.eq(null != request.getStatus(), ResourcePo::getStatus, request.getStatus());
        queryWrapper.in(CollectionUtil.isNotEmpty(request.getResourceIds()), ResourcePo::getId, request.getResourceIds());
        queryWrapper.ne(null != request.getNeId(), ResourcePo::getId, request.getNeId());
        queryWrapper.apply(StrUtil.isNotBlank(request.getApplySql()), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(), request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
