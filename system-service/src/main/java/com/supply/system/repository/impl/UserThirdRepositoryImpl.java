package com.supply.system.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.system.mapper.UserThirdMapper;
import com.supply.system.model.po.UserThirdPo;
import com.supply.system.model.request.UserThirdRequest;
import com.supply.system.repository.IUserThirdRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-08
 */
@Repository
public class UserThirdRepositoryImpl extends ServiceImpl<UserThirdMapper, UserThirdPo> implements IUserThirdRepository {

    private final UserThirdMapper userThirdMapper;

    public UserThirdRepositoryImpl(UserThirdMapper userThirdMapper) {
        this.userThirdMapper = userThirdMapper;
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean save(UserThirdPo entity) {
        return super.save(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.INSERT)
    public boolean saveBatch(Collection<UserThirdPo> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateById(UserThirdPo entity) {
        return super.updateById(entity);
    }

    @Override
    @BaseData(fill = OperatorTypeEnum.UPDATE)
    public boolean updateBatchById(Collection<UserThirdPo> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public UserThirdPo getByParams(UserThirdRequest request) {
        final LambdaQueryWrapper<UserThirdPo> queryWrapper = this.getQueryWrapper(request);
        return userThirdMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserThirdPo> getListByParams(UserThirdRequest request) {
        final LambdaQueryWrapper<UserThirdPo> queryWrapper = this.getQueryWrapper(request);
        return userThirdMapper.selectList(queryWrapper);
    }

    /**
     * @author wjd
     * @description 通用非空查询条件.
     * @date 2022/4/13
     * @param request 条件
     * @return 查询条件组装结果
     **/
    private LambdaQueryWrapper<UserThirdPo> getQueryWrapper(UserThirdRequest request) {
        LambdaQueryWrapper<UserThirdPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(null != request.getId(), UserThirdPo::getId, request.getId());
        queryWrapper.eq(null != request.getUserId(), UserThirdPo::getUserId, request.getUserId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getOpenId()), UserThirdPo::getOpenId, request.getOpenId());
        queryWrapper.eq(StrUtil.isNotBlank(request.getNickName()), UserThirdPo::getNickName, request.getNickName());
        queryWrapper.eq(null != request.getThirdType(), UserThirdPo::getNickName, request.getNickName());
        queryWrapper.eq(null != request.getTenantId(), UserThirdPo::getNickName, request.getNickName());
        queryWrapper.eq(null != request.getStatus(), UserThirdPo::getStatus, request.getStatus());
        queryWrapper.apply(StrUtil.isNotBlank(request.getApplySql()), request.getApplySql());
        queryWrapper.orderBy(null != request.getOrderColumn(),  request.getIsAsc(), request.getOrderColumn());
        queryWrapper.orderBy(CollectionUtil.isNotEmpty(request.getOrderColumnList()), request.getIsAsc(), request.getOrderColumnList());
        return queryWrapper;
    }
}
