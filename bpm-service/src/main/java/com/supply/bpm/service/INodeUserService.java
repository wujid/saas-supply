package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
    void addNodeUsers(List<NodeUserRequest> requests);

    /**
     * @description 修改流程节点审批人信息.
     * @author wjd
     * @date 2023/1/5
     * @param requests 新审批人信息集
     */
    void updateNodeUsers(List<NodeUserRequest> requests);

    /**
      * @description 获取流程审批人分页信息.
      * @author wjd
      * @date 2023/6/15
      * @param request 查询条件
      * @return 流程审批人分页信息
      */
    IPage<NodeUserResponse> getNodeUserPage(NodeUserRequest request);
}
