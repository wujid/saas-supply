package com.supply.bpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.cvt.NodeUserCvt;
import com.supply.bpm.model.po.NodeUserPo;
import com.supply.bpm.model.request.NodeUserRequest;
import com.supply.bpm.model.response.NodeUserResponse;
import com.supply.bpm.repository.INodeUserRepository;
import com.supply.bpm.service.INodeUserService;
import com.supply.common.constant.Constant;
import com.supply.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
@Service
public class NodeUserServiceImpl implements INodeUserService {
    private static final Logger logger = LoggerFactory.getLogger(NodeUserServiceImpl.class);

    private final INodeUserRepository nodeUserRepository;

    public NodeUserServiceImpl(INodeUserRepository nodeUserRepository) {
        this.nodeUserRepository = nodeUserRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNodeUser(List<NodeUserRequest> requests) {
        logger.info("[新增流程节点审批人信息集]---实体信息为{}", JSON.toJSONString(requests));
        final List<NodeUserPo> nodeUserPoList = NodeUserCvt.INSTANCE.requestToPoBatch(requests);
        nodeUserRepository.saveBatch(nodeUserPoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNodeUser(List<NodeUserRequest> requests) {
        logger.info("[修改流程节点审批人信息集]---实体信息为{}", JSON.toJSONString(requests));
        // 删除历史审批人信息
        final Long nodeSetId = requests.stream().findFirst().get().getNodeSetId();
        NodeUserPo nodeUserPo = new NodeUserPo();
        nodeUserPo.setStatus(Constant.STATUS_DEL);
        NodeUserRequest request = new NodeUserRequest();
        request.setNodeSetId(nodeSetId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        nodeUserRepository.updateByParams(nodeUserPo, request);
        // 保存新审批人信息
        final List<NodeUserPo> nodeUserPoList = NodeUserCvt.INSTANCE.requestToPoBatch(requests);
        nodeUserRepository.saveBatch(nodeUserPoList);
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
        return CommonUtil.pageCvt(responseList, poPage);
    }
}
