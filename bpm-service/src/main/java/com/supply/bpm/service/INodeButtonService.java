package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.NodeButtonRequest;
import com.supply.bpm.model.response.NodeButtonResponse;

import java.util.List;

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

    /**
      * @description 根据自定义条件查询流程节点按钮信息集.
      * @author wjd
      * @date 2023/6/27
      * @param request 查询条件
      * @return 流程节点按钮信息集
      */
    List<NodeButtonResponse> getListByParams(NodeButtonRequest request);

    /**
      * @description 根据流程定义ID及节点ID查询对应的节点按钮信息集.
      * @author wjd
      * @date 2023/7/3
      * @param definitionId 流程定义ID
      * @param nodeId 节点ID
      * @return 节点按钮信息集
      */
    List<NodeButtonResponse> getByDefinitionIdAndNodeId(String definitionId, String nodeId);
}
