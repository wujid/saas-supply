package com.supply.bpm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.cvt.NodeCvt;
import com.supply.bpm.model.po.NodePo;
import com.supply.bpm.model.request.NodeRequest;
import com.supply.bpm.model.response.NodeResponse;
import com.supply.bpm.repository.INodeRepository;
import com.supply.bpm.service.INodeService;
import com.supply.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
@Service
public class NodeServiceImpl implements INodeService {
    private static final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

    private final INodeRepository nodeRepository;

    public NodeServiceImpl(INodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @Override
    public IPage<NodeResponse> getNodePage(NodeRequest request) {
        Page<NodePo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<NodePo> poPage = nodeRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<NodePo> poList = poPage.getRecords();
        final List<NodeResponse> responseList = NodeCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }


}
