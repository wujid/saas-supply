package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.NodeButtonPo;
import com.supply.bpm.model.request.NodeButtonRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-04
 */
public interface INodeButtonRepository extends IService<NodeButtonPo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2023/1/4
     * @param nodeButtonPo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(NodeButtonPo nodeButtonPo, NodeButtonRequest request);

    /**
     * @description 根据自定义条件查询流程节点按钮信息.
     * @author wjd
     * @date 2023/1/4
     * @param request 查询条件
     * @return 流程节点按钮信息
     */
    NodeButtonPo getByParams(NodeButtonRequest request);

    /**
     * @description 根据自定义条件查询流程节点按钮信息集.
     * @author wjd
     * @date 2023/1/4
     * @param request 查询条件
     * @return 流程节点按钮信息集
     */
    List<NodeButtonPo> getListByParams(NodeButtonRequest request);

    /**
     * @description 根据自定义条件查询流程节点按钮带分页信息.
     * @author wjd
     * @date 2023/1/4
     * @param page 分页信息
     * @param request 查询条件
     * @return 带分页的结果
     */
    Page<NodeButtonPo> getPageByParams(Page<NodeButtonPo> page, NodeButtonRequest request);

    Long getCountByParams(NodeButtonRequest request);
}
