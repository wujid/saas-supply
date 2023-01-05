package com.supply.bpm.service;

import com.supply.bpm.model.request.NodeUserRequest;
import com.supply.bpm.model.response.NodeUserResponse;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
public interface INodeUserService {

    /**
      * @description 新增流程节点审批人信息.
      * @author wjd
      * @date 2023/1/5
      * @param requests 待新增的实体信息集
      */
    void addNodeUser(List<NodeUserRequest> requests);

    /**
      * @description 修改流程节点审批人信息.
      * @author wjd
      * @date 2023/1/5
      * @param requests 新审批人信息集
      */
    void updateNodeUser(List<NodeUserRequest> requests);

    /**
      * @description 根据自定义条件查询流程节点审批人信息集.
      * @author wjd
      * @date 2023/1/5
      * @param request 查询条件
      * @return 流程节点审批人信息集
      */
    List<NodeUserResponse> getNodeUserListByParams(NodeUserRequest request);
}
