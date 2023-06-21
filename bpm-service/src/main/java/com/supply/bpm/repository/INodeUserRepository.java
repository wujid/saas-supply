package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.NodeUserPo;
import com.supply.bpm.model.request.NodeUserRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-04
 */
public interface INodeUserRepository extends IService<NodeUserPo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2023/1/4
     * @param nodePo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(NodeUserPo nodePo, NodeUserRequest request);

    /**
     * @description 根据自定义条件查询流程节点审批人信息.
     * @author wjd
     * @date 2023/1/4
     * @param request 查询条件
     * @return 流程节点审批人信息
     */
    NodeUserPo getByParams(NodeUserRequest request);

    /**
     * @description 根据自定义条件查询流程节点审批人信息集.
     * @author wjd
     * @date 2023/1/4
     * @param request 查询条件
     * @return 流程节点审批人信息集
     */
    List<NodeUserPo> getListByParams(NodeUserRequest request);

    /**
     * @description 根据自定义条件查询流程节点审批人带分页信息.
     * @author wjd
     * @date 2023/1/4
     * @param page 分页信息
     * @param request 查询条件
     * @return 带分页的结果
     */
    Page<NodeUserPo> getPageByParams(Page<NodeUserPo> page, NodeUserRequest request);

    List<NodeUserPo> getListByNodeSetId(Long nodeSetId);
}
