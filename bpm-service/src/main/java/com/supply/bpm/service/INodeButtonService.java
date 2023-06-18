package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.NodeButtonRequest;
import com.supply.bpm.model.response.NodeButtonResponse;

/**
 * @author wjd
 * @description .
 * @date 2023-06-15
 */
public interface INodeButtonService {

    void addNodeButton(NodeButtonRequest request);

    void updateNodeButton(NodeButtonRequest request);

    void delNodeButton(Long id);

    void freezeNodeButton(Long id);

    void activeNodeButton(Long id);

    IPage<NodeButtonResponse> getNodeButtonPage(NodeButtonRequest request);
}
