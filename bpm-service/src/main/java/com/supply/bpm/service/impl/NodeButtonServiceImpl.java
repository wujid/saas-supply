package com.supply.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.cvt.NodeButtonCvt;
import com.supply.bpm.model.po.NodeButtonPo;
import com.supply.bpm.model.po.NodeSetPo;
import com.supply.bpm.model.request.NodeButtonRequest;
import com.supply.bpm.model.request.NodeSetRequest;
import com.supply.bpm.model.response.NodeButtonResponse;
import com.supply.bpm.repository.INodeButtonRepository;
import com.supply.bpm.repository.INodeSetRepository;
import com.supply.bpm.service.INodeButtonService;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-06-15
 */
@Service
public class NodeButtonServiceImpl implements INodeButtonService {
    private static final Logger logger = LoggerFactory.getLogger(NodeButtonServiceImpl.class);

    private final INodeButtonRepository nodeButtonRepository;

    private final INodeSetRepository nodeSetRepository;

    public NodeButtonServiceImpl(INodeButtonRepository nodeButtonRepository, INodeSetRepository nodeSetRepository) {
        this.nodeButtonRepository = nodeButtonRepository;
        this.nodeSetRepository = nodeSetRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNodeButton(NodeButtonRequest request) {
        logger.info("[新增流程节点按钮]---实体信息为{}", JSON.toJSONString(request));
        // 验证按钮唯一性
        this.saveValidate(request);
        final NodeButtonPo nodeButtonPo = NodeButtonCvt.INSTANCE.requestToPo(request);
        nodeButtonPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        nodeButtonRepository.save(nodeButtonPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNodeButton(NodeButtonRequest request) {
        // 验证按钮唯一性
        this.saveValidate(request);
        logger.info("[修改流程节点按钮]---实体信息为{}", JSON.toJSONString(request));
        final NodeButtonPo nodeButtonPo = NodeButtonCvt.INSTANCE.requestToPo(request);
        nodeButtonRepository.updateById(nodeButtonPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delNodeButton(Long id) {
        logger.info("[删除流程节点按钮]---待删除的ID为{}", id);
        final NodeButtonPo nodeButtonPo = new NodeButtonPo();
        nodeButtonPo.setId(id);
        nodeButtonPo.setStatus(Constant.STATUS_DEL);
        nodeButtonRepository.updateById(nodeButtonPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeNodeButton(Long id) {
        logger.info("[冻结流程节点按钮]---待冻结的ID为{}", id);
        final NodeButtonPo nodeButtonPo = new NodeButtonPo();
        nodeButtonPo.setId(id);
        nodeButtonPo.setBusinessStatus(BusinessStatusEnum.IN_FREEZE.getStatus());
        nodeButtonRepository.updateById(nodeButtonPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeNodeButton(Long id) {
        logger.info("[解冻流程节点按钮]---待解冻的ID为{}", id);
        final NodeButtonPo nodeButtonPo = new NodeButtonPo();
        nodeButtonPo.setId(id);
        nodeButtonPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        nodeButtonRepository.updateById(nodeButtonPo);
    }

    @Override
    public IPage<NodeButtonResponse> getNodeButtonPage(NodeButtonRequest request) {
        Page<NodeButtonPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<NodeButtonPo> poPage = nodeButtonRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<NodeButtonPo> poList = page.getRecords();
        final List<NodeButtonResponse> responseList = NodeButtonCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    @Override
    public List<NodeButtonResponse> getListByParams(NodeButtonRequest request) {
        final List<NodeButtonPo> list = nodeButtonRepository.getListByParams(request);
        return NodeButtonCvt.INSTANCE.poToResponseBatch(list);
    }

    @Override
    public List<NodeButtonResponse> getByDefinitionIdAndNodeId(String definitionId, String nodeId) {
        NodeSetRequest nodeSetRequest = new NodeSetRequest();
        nodeSetRequest.setDefinitionId(definitionId);
        nodeSetRequest.setNodeId(nodeId);
        nodeSetRequest.setStatus(Constant.STATUS_NOT_DEL);
        final NodeSetPo nodeSet = nodeSetRepository.getByParams(nodeSetRequest);
        if (null == nodeSet) {
            logger.error("根据流程定义ID{}和节点ID{}未找到流程节点设置信息", definitionId, nodeId);
            throw new ApiException();
        }
        NodeButtonRequest request = new NodeButtonRequest();
        request.setNodeSetId(nodeSet.getId());
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        final List<NodeButtonResponse> list = this.getListByParams(request);
        if (CollectionUtil.isEmpty(list)) {
            throw new ApiException("当前审批人未配置流程操作按钮");
        }
        return list;
    }

    /**
      * @description 新增&修改验证按钮唯一性.
      * @author wjd
      * @date 2023/6/18
      * @param request 待验证的实体
      */
    private void saveValidate(NodeButtonRequest request) {
        NodeButtonRequest nodeButtonRequest = new NodeButtonRequest();
        nodeButtonRequest.setNodeSetId(request.getNodeSetId());
        nodeButtonRequest.setButtonType(request.getButtonType());
        nodeButtonRequest.setTenantId(request.getTenantId());
        nodeButtonRequest.setStatus(Constant.STATUS_NOT_DEL);
        nodeButtonRequest.setNeId(request.getId());
        final Long count = nodeButtonRepository.getCountByParams(nodeButtonRequest);
        if (count > 0) {
            throw new ApiException("该按钮类型已存在!");
        }
    }
}
