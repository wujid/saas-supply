package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.NodeSetRequest;
import com.supply.bpm.model.response.NodeSetResponse;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-06-15
 */
public interface INodeSetService {


    /**
      * @description 根据主键ID修改详情表单url.
      * @author wjd
      * @date 2023/6/15
      * @param id 主键ID
      * @param formUrl 详情表单url
      */
    void updateNodeSetFormUrl(Long id, String formUrl);

    /**
      * @description 根据自定义条件查询流程节点分页信息.
      * @author wjd
      * @date 2023/6/15
      * @param request 查询条件
      * @return 流程节点分页信息
      */
    IPage<NodeSetResponse> getNodeSetPage(NodeSetRequest request);

    /**
      * @description 发起流程获取下一个审批节点信息.
      * @author wjd
      * @date 2023/6/19
      * @param request 查询条件
      * @return 下一个节点审批人信息
      */
    List<NodeSetResponse> startBpmNextNodeInfo(NodeSetRequest request);

    /**
      * @description 根据任务ID获取下一个审批节点信息.
      * @author wjd
      * @date 2023/6/27
      * @param taskId 任务ID
      * @return 下一个审批节点信息
      */
    List<NodeSetResponse> getNextNodeInfoByTaskId(String taskId);
}
