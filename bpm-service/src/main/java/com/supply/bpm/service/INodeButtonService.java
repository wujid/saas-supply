package com.supply.bpm.service;

import com.supply.bpm.model.request.NodeButtonRequest;
import com.supply.bpm.model.response.NodeButtonResponse;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
public interface INodeButtonService {

    /**
      * @description 新增流程节点按钮信息.
      * @author wjd
      * @date 2023/1/5
      * @param requests 待新增的实体信息集
      */
    void addNodeButton(List<NodeButtonRequest> requests);

    /**
      * @description 修改流程节点按钮信息.
      * @author wjd
      * @date 2023/1/5
      * @param requests 待修改的实体信息集
      */
    void updateNodeButton(List<NodeButtonRequest> requests);

    /**
      * @description 根据自定义条件查询流程节点按钮信息集.
      * @author wjd
      * @date 2023/1/5
      * @param request 查询条件
      * @return 流程节点按钮信息集
      */
    List<NodeButtonResponse> getNodeButtonListByParams(NodeButtonRequest request);
}
