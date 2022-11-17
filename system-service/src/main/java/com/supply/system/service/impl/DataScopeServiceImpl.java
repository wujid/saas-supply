package com.supply.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.util.CommonUtil;
import com.supply.common.web.constant.DataScopeRangeEnum;
import com.supply.system.cvt.DataScopeRangeCvt;
import com.supply.system.cvt.DataScopeTypeCvt;
import com.supply.system.model.po.DataScopeRangePo;
import com.supply.system.model.po.DataScopeTypePo;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.po.RolePo;
import com.supply.system.model.po.UserPo;
import com.supply.system.model.request.DataScopeRangeRequest;
import com.supply.system.model.request.DataScopeTypeRequest;
import com.supply.system.model.request.ResourceRequest;
import com.supply.system.model.request.RoleRequest;
import com.supply.system.model.request.UserRequest;
import com.supply.system.model.response.DataScopeRangeResponse;
import com.supply.system.model.response.DataScopeTypeResponse;
import com.supply.system.repository.IDataScopeRangeRepository;
import com.supply.system.repository.IDataScopeTypeRepository;
import com.supply.system.repository.IResourceRepository;
import com.supply.system.repository.IRoleRepository;
import com.supply.system.repository.IUserRepository;
import com.supply.system.service.IDataScopeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-09-08
 */
@Service
public class DataScopeServiceImpl implements IDataScopeService {
    private static final Logger logger = LoggerFactory.getLogger(DataScopeServiceImpl.class);

    private final IDataScopeTypeRepository dataScopeTypeRepository;

    private final IDataScopeRangeRepository dataScopeRangeRepository;

    private final IUserRepository userRepository;

    private final IResourceRepository resourceRepository;

    private final IRoleRepository roleRepository;

    public DataScopeServiceImpl(IDataScopeTypeRepository dataScopeTypeRepository, IDataScopeRangeRepository dataScopeRangeRepository,
                                IUserRepository userRepository, IResourceRepository resourceRepository, IRoleRepository roleRepository) {
        this.dataScopeTypeRepository = dataScopeTypeRepository;
        this.dataScopeRangeRepository = dataScopeRangeRepository;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDataScope(DataScopeTypeRequest request) {
        logger.info("[新增用户资源数据权限]---待新增的实体信息为{}", JSON.toJSONString(request));
        // 数据验证
        DataScopeTypeRequest dataScopeTypeRequest = new DataScopeTypeRequest();
        dataScopeTypeRequest.setUserId(request.getUserId());
        dataScopeTypeRequest.setResourceId(request.getResourceId());
        dataScopeTypeRequest.setStatus(Constant.STATUS_NOT_DEL);
        final Long count = dataScopeTypeRepository.getCountByParams(dataScopeTypeRequest);
        if (count > 0) {
            final String message = "该用户在该菜单上已存在数据权限信息";
            logger.info(message);
            throw new ApiException(message);
        }

        final DataScopeTypePo dataScopeTypePo = DataScopeTypeCvt.INSTANCE.requestToPo(request);
        dataScopeTypeRepository.save(dataScopeTypePo);
        final Long id = dataScopeTypePo.getId();

        final List<DataScopeRangeRequest> dataScopeRangeList = request.getDataScopeRangeList();
        if (CollectionUtil.isEmpty(dataScopeRangeList)) {
            return;
        }
        dataScopeRangeList.forEach(dataScopeRangeRequest -> dataScopeRangeRequest.setDataScopeTypeId(id));
        final List<DataScopeRangePo> dataScopeRangePoList = DataScopeRangeCvt.INSTANCE.requestToPoBatch(dataScopeRangeList);
        dataScopeRangeRepository.saveBatch(dataScopeRangePoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataScope(DataScopeTypeRequest request) {
        logger.info("[修改用户资源数据权限]---待修改的实体信息为{}", JSON.toJSONString(request));
        // 修改用户资源权限类型
        final DataScopeTypePo dataScopeTypePo = DataScopeTypeCvt.INSTANCE.requestToPo(request);
        dataScopeTypeRepository.updateById(dataScopeTypePo);

        // 删除历史资源权限范围
        final Long typeId = dataScopeTypePo.getId();
        DataScopeRangePo dataScopeRangePo = new DataScopeRangePo();
        dataScopeRangePo.setStatus(Constant.STATUS_DEL);
        DataScopeRangeRequest dataScopeRangeRequest = new DataScopeRangeRequest();
        dataScopeRangeRequest.setDataScopeTypeId(typeId);
        dataScopeRangeRequest.setStatus(Constant.STATUS_NOT_DEL);
        dataScopeRangeRepository.updateByParams(dataScopeRangePo, dataScopeRangeRequest);

        // 新增历史资源权限范围
        final List<DataScopeRangeRequest> dataScopeRangeList = request.getDataScopeRangeList();
        if (CollectionUtil.isEmpty(dataScopeRangeList)) {
            return;
        }
        dataScopeRangeList.forEach(rangeRequest -> {
            rangeRequest.setId(null);
            rangeRequest.setDataScopeTypeId(typeId);
        });
        final List<DataScopeRangePo> dataScopeRangePoList = DataScopeRangeCvt.INSTANCE.requestToPoBatch(dataScopeRangeList);
        dataScopeRangeRepository.saveBatch(dataScopeRangePoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delDataScopeById(Long dataScopeTypeId) {
        logger.info("[删除用户资源数据权限]---其中用户资源数据权限类型ID为{}", dataScopeTypeId);
        // 删除用户资源权限类型
        final DataScopeTypePo dataScopeTypePo = new DataScopeTypePo();
        dataScopeTypePo.setId(dataScopeTypeId);
        dataScopeTypePo.setStatus(Constant.STATUS_DEL);
        dataScopeTypeRepository.updateById(dataScopeTypePo);

        // 删除资源权限范围
        DataScopeRangePo dataScopeRangePo = new DataScopeRangePo();
        dataScopeRangePo.setStatus(Constant.STATUS_DEL);
        DataScopeRangeRequest dataScopeRangeRequest = new DataScopeRangeRequest();
        dataScopeRangeRequest.setDataScopeTypeId(dataScopeTypeId);
        dataScopeRangeRequest.setStatus(Constant.STATUS_NOT_DEL);
        dataScopeRangeRepository.updateByParams(dataScopeRangePo, dataScopeRangeRequest);
    }

    @Override
    public IPage<DataScopeTypeResponse> getPageByParams(DataScopeTypeRequest request) {
        Page<DataScopeTypePo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<DataScopeTypePo> poPage = dataScopeTypeRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<DataScopeTypePo> poList = page.getRecords();
        final List<DataScopeTypeResponse> responseList = DataScopeTypeCvt.INSTANCE.poToResponseBatch(poList);
        this.getPageDetail(responseList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    @Override
    public DataScopeTypeResponse getDataScopeByParams(DataScopeTypeRequest request) {
        // 查询并转换
        final DataScopeTypePo dataScopeTypePo = dataScopeTypeRepository.getByParams(request);
        if (null == dataScopeTypePo) {
            return null;
        }
        final DataScopeTypeResponse dataScopeTypeResponse = DataScopeTypeCvt.INSTANCE.poToResponse(dataScopeTypePo);
        final Long typeId = dataScopeTypeResponse.getId();
        // 根据类型ID查询对应的范围信息集
        DataScopeRangeRequest dataScopeRangeRequest = new DataScopeRangeRequest();
        dataScopeRangeRequest.setDataScopeTypeId(typeId);
        dataScopeRangeRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<DataScopeRangePo> dataScopeRangePoList = dataScopeRangeRepository.getListByParams(dataScopeRangeRequest);
        final List<DataScopeRangeResponse> dataScopeRangeResponseList = DataScopeRangeCvt.INSTANCE.poToResponseBatch(dataScopeRangePoList);

        dataScopeTypeResponse.setDataScopeRangeList(dataScopeRangeResponseList);
        return dataScopeTypeResponse;
    }

    @Override
    public List<DataScopeRangeResponse> getDataScopeRangeListByParams(DataScopeRangeRequest request) {
        final List<DataScopeRangePo> dataScopeRangePoList = dataScopeRangeRepository.getListByParams(request);
        final List<DataScopeRangeResponse> list = DataScopeRangeCvt.INSTANCE.poToResponseBatch(dataScopeRangePoList);
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }
        final Set<Long> userIds = list.stream()
                .filter(item -> item.getDataScopeRange() == DataScopeRangeEnum.DATA_SCOPE_RANGE_USER.getType())
                .map(DataScopeRangeResponse::getDataScopeId).collect(Collectors.toSet());
        UserRequest userRequest = new UserRequest();
        userRequest.setUserIds(userIds);
        final Map<Long, UserPo> userMap = userRepository.getMapByParams(userRequest);

        final Set<Long> roleIds = list.stream()
                .filter(item -> item.getDataScopeRange() == DataScopeRangeEnum.DATA_SCOPE_RANGE_ROLE.getType())
                .map(DataScopeRangeResponse::getDataScopeId).collect(Collectors.toSet());
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRoleIds(roleIds);
        final Map<Long, RolePo> roleMap = roleRepository.getMapByParams(roleRequest);

        for (DataScopeRangeResponse dataScopeRange : list) {
            if (dataScopeRange.getDataScopeRange() == DataScopeRangeEnum.DATA_SCOPE_RANGE_USER.getType()) {
                if (userMap.containsKey(dataScopeRange.getDataScopeId())) {
                    final UserPo userPo = userMap.get(dataScopeRange.getDataScopeId());
                    dataScopeRange.setName(userPo.getName());
                }
            }
            if (dataScopeRange.getDataScopeRange() == DataScopeRangeEnum.DATA_SCOPE_RANGE_ROLE.getType()) {
                if (roleMap.containsKey(dataScopeRange.getDataScopeId())) {
                    final RolePo rolePo = roleMap.get(dataScopeRange.getDataScopeId());
                    dataScopeRange.setName(rolePo.getName());
                }
            }
        }
        return list;
    }

    /**
      * @description 分页信息补充.
      * @author wjd
      * @date 2022/9/8
      * @param responseList 待补充的
      */
    private void getPageDetail(List<DataScopeTypeResponse> responseList) {
        // 查询出用户名称&资源名称
        // 根据用户ID集获取用户信息集
        final Set<Long> userIds = responseList.stream().map(DataScopeTypeResponse::getUserId).collect(Collectors.toSet());
        UserRequest userRequest = new UserRequest();
        userRequest.setUserIds(userIds);
        final Map<Long, UserPo> userMap = userRepository.getMapByParams(userRequest);
        // 根据资源ID集获取资源信息集
        final Set<Long> resourceIds = responseList.stream().map(DataScopeTypeResponse::getResourceId).collect(Collectors.toSet());
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setResourceIds(resourceIds);
        final Map<Long, ResourcePo> resourceMap = resourceRepository.getMapByParams(resourceRequest);

        // 信息组装
        for (DataScopeTypeResponse dataScope : responseList) {
            final Long userId = dataScope.getUserId();
            if (userMap.containsKey(userId)) {
                final UserPo userPo = userMap.get(userId);
                dataScope.setUserName(userPo.getName());
            }
            final Long resourceId = dataScope.getResourceId();
            if (resourceMap.containsKey(resourceId)) {
                final ResourcePo resourcePo = resourceMap.get(resourceId);
                dataScope.setResourceName(resourcePo.getName());
            }
        }
    }
}
