package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.NodeRequest;
import com.supply.bpm.model.response.NodeResponse;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
public interface INodeService {

    /**
      * @description 根据自定义条件查询流程节点信息带分页.
      * @author wjd
      * @date 2023/1/5
      * @param request 查询条件
      * @return 流程节点分页信息
      */
    IPage<NodeResponse> getNodePage(NodeRequest request);
}
