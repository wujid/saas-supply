package com.supply.bpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.supply.bpm.cvt.NodeButtonCvt;
import com.supply.bpm.model.po.NodeButtonPo;
import com.supply.bpm.model.request.NodeButtonRequest;
import com.supply.bpm.model.response.NodeButtonResponse;
import com.supply.bpm.repository.INodeButtonRepository;
import com.supply.bpm.service.INodeButtonService;
import com.supply.common.constant.Constant;
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
public class NodeButtonServiceImpl implements INodeButtonService {
    private static final Logger logger = LoggerFactory.getLogger(NodeButtonServiceImpl.class);

    private final INodeButtonRepository nodeButtonRepository;

    public NodeButtonServiceImpl(INodeButtonRepository nodeButtonRepository) {
        this.nodeButtonRepository = nodeButtonRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNodeButton(List<NodeButtonRequest> requests) {
        logger.info("[新增流程节点按钮信息集]---实体信息为{}", JSON.toJSONString(requests));
        final List<NodeButtonPo> nodeButtonPoList = NodeButtonCvt.INSTANCE.requestToPoBatch(requests);
        nodeButtonRepository.saveBatch(nodeButtonPoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNodeButton(List<NodeButtonRequest> requests) {
        logger.info("[修改流程节点按钮信息集]---实体信息为{}", JSON.toJSONString(requests));
        // 删除历史流程节点按钮信息集
        final String nodeId = requests.stream().findFirst().get().getNodeId();
        NodeButtonPo nodeButtonPo = new NodeButtonPo();
        nodeButtonPo.setStatus(Constant.STATUS_DEL);
        NodeButtonRequest request = new NodeButtonRequest();
        request.setNodeId(nodeId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        nodeButtonRepository.updateByParams(nodeButtonPo, request);
        // 保存新流程节点按钮信息集
        final List<NodeButtonPo> nodeButtonPoList = NodeButtonCvt.INSTANCE.requestToPoBatch(requests);
        nodeButtonRepository.saveBatch(nodeButtonPoList);
    }

    @Override
    public List<NodeButtonResponse> getNodeButtonListByParams(NodeButtonRequest request) {
        final List<NodeButtonPo> nodeButtonPoList = nodeButtonRepository.getListByParams(request);
        return NodeButtonCvt.INSTANCE.poToResponseBatch(nodeButtonPoList);
    }
}
