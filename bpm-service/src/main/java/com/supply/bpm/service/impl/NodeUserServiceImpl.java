package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.constant.NodeUserTypeEnum;
import com.supply.bpm.cvt.NodeUserCvt;
import com.supply.bpm.model.po.NodeUserPo;
import com.supply.bpm.model.request.NodeUserRequest;
import com.supply.bpm.model.response.NodeUserResponse;
import com.supply.bpm.repository.INodeUserRepository;
import com.supply.bpm.service.INodeUserService;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.util.CommonUtil;
import com.supply.common.util.SystemUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
@Service
public class NodeUserServiceImpl implements INodeUserService {
    private static final Logger logger = LoggerFactory.getLogger(NodeUserServiceImpl.class);

    private final INodeUserRepository nodeUserRepository;

    private final SystemUserUtil userUtil;

    public NodeUserServiceImpl(INodeUserRepository nodeUserRepository, SystemUserUtil userUtil) {
        this.nodeUserRepository = nodeUserRepository;
        this.userUtil = userUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNodeUsers(List<NodeUserRequest> requests) {
        logger.info("[新增流程节点审批人信息集]---实体信息为{}", JSON.toJSONString(requests));
        // 验证是否已经关联
        this.addValidate(requests);
        final List<NodeUserPo> nodeUserPoList = NodeUserCvt.INSTANCE.requestToPoBatch(requests);
        nodeUserRepository.saveBatch(nodeUserPoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delNodeUser(Long id) {
        logger.info("[删除流程节点审批人信息]---待删除的主键ID为{}", id);
        NodeUserPo nodeUser = new NodeUserPo();
        nodeUser.setId(id);
        nodeUser.setStatus(Constant.STATUS_DEL);
        nodeUserRepository.updateById(nodeUser);
    }


    @Override
    public IPage<NodeUserResponse> getNodeUserPage(NodeUserRequest request) {
        Page<NodeUserPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<NodeUserPo> poPage = nodeUserRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<NodeUserPo> poList = poPage.getRecords();
        final List<NodeUserResponse> responseList = NodeUserCvt.INSTANCE.poToResponseBatch(poList);
        this.getExtData(responseList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    /**
      * @description 验证是否重复关联.
      * @author wjd
      * @date 2023/6/18
      * @param requests 待验证的
      */
    private void addValidate(List<NodeUserRequest> requests) {
        // 用户类型对应的关联ID集
        final Map<Integer, Set<Long>> userTypeMap = requests.stream()
                .collect(Collectors.groupingBy(NodeUserRequest::getNodeUserType, Collectors.mapping(NodeUserRequest::getRelationId, Collectors.toSet())));

        NodeUserRequest request = new NodeUserRequest();
        request.setStatus(Constant.STATUS_NOT_DEL);
        // 用户关联
        if (userTypeMap.containsKey(NodeUserTypeEnum.USER.getType())) {
            final Set<Long> relationIds = userTypeMap.get(NodeUserTypeEnum.USER.getType());
            request.setNodeUserType(NodeUserTypeEnum.USER.getType());
            request.setRelationIds(relationIds);
            final List<NodeUserPo> nodeUsers = nodeUserRepository.getListByParams(request);
            // 非空说明存在重复添加
            if (CollectionUtil.isNotEmpty(nodeUsers)) {
                final Set<Long> userIds = nodeUsers.stream().map(NodeUserPo::getRelationId).collect(Collectors.toSet());
                final List<SysUserResponse> users = userUtil.getUsersByIds(userIds);
                if (CollectionUtil.isEmpty(users)) {
                    return;
                }
                final Set<String> userNames = users.stream().map(SysUserResponse::getName).collect(Collectors.toSet());
                final String userName = StrUtil.join(",", userNames);
                final String errorMessage = StrUtil.format("用户{}已被关联", userName);
                throw new ApiException(errorMessage);
            }
        }
    }


    /**
      * @description 节点人员拓展信息.
      * @author wjd
      * @date 2023/6/18
      * @param list 待拓展的节点人员信息
      */
    private void getExtData(List<NodeUserResponse> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        // 用户类型对应的关联ID集
        final Map<Integer, Set<Long>> userTypeMap = list.stream()
                .collect(Collectors.groupingBy(NodeUserResponse::getNodeUserType, Collectors.mapping(NodeUserResponse::getRelationId, Collectors.toSet())));

        // 获取用户信息及用户ID对应的用户名称映射关系
        Map<Long, String> userMap = new HashMap<>();
        if (userTypeMap.containsKey(NodeUserTypeEnum.USER.getType())) {
            final Set<Long> userIds = userTypeMap.get(NodeUserTypeEnum.USER.getType());
            final List<SysUserResponse> users = userUtil.getUsersByIds(userIds);
            if (CollectionUtil.isNotEmpty(users)) {
                userMap = users.stream().collect(Collectors.toMap(SysUserResponse::getId, SysUserResponse::getName, (k1, k2) -> k1));
            }
        }

        // 信息组装
        for (NodeUserResponse nodeUser : list) {
            final Integer userType = nodeUser.getNodeUserType();
            final Long userId = nodeUser.getRelationId();
            if (userType == NodeUserTypeEnum.USER.getType() && userMap.containsKey(userId)) {
                final String userName = userMap.get(userId);
                nodeUser.setRelationName(userName);
            }
        }
    }
}
