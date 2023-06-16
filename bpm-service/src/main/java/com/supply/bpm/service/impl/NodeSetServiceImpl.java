package com.supply.bpm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.cvt.NodeSetCvt;
import com.supply.bpm.model.po.NodeSetPo;
import com.supply.bpm.model.request.NodeSetRequest;
import com.supply.bpm.model.response.NodeSetResponse;
import com.supply.bpm.repository.INodeSetRepository;
import com.supply.bpm.service.INodeSetService;
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
public class NodeSetServiceImpl implements INodeSetService {
    private static final Logger logger = LoggerFactory.getLogger(NodeSetServiceImpl.class);

    private final INodeSetRepository nodeSetRepository;

    public NodeSetServiceImpl(INodeSetRepository nodeSetRepository) {
        this.nodeSetRepository = nodeSetRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNodeSetFormUrl(Long id, String formUrl) {
        logger.info("[根据用户节点ID修改详情表单url]---待修改的用户节点id{}对应的表单url为{}", id, formUrl);
        NodeSetPo nodeSetPo = new NodeSetPo();
        nodeSetPo.setId(id);
        nodeSetPo.setFormUrl(formUrl);
        nodeSetRepository.updateById(nodeSetPo);
    }

    @Override
    public IPage<NodeSetResponse> getNodeSetPage(NodeSetRequest request) {
        Page<NodeSetPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<NodeSetPo> poPage = nodeSetRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<NodeSetPo> poList = poPage.getRecords();
        final List<NodeSetResponse> responseList = NodeSetCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }
}
