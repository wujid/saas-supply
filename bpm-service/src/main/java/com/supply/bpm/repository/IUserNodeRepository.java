package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.UserNodePo;
import com.supply.bpm.model.request.UserNodeRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-04
 */
public interface IUserNodeRepository extends IService<UserNodePo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2023/1/4
     * @param userNodePo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(UserNodePo userNodePo, UserNodeRequest request);

    /**
     * @description 根据自定义条件查询流程节点信息.
     * @author wjd
     * @date 2023/1/4
     * @param request 查询条件
     * @return 流程节点信息
     */
    UserNodePo getByParams(UserNodeRequest request);

    /**
     * @description 根据自定义条件查询流程节点信息集.
     * @author wjd
     * @date 2023/1/4
     * @param request 查询条件
     * @return 流程节点信息集
     */
    List<UserNodePo> getListByParams(UserNodeRequest request);

    /**
     * @description 根据自定义条件查询流程节点带分页信息.
     * @author wjd
     * @date 2023/1/4
     * @param page 分页信息
     * @param request 查询条件
     * @return 带分页的结果
     */
    Page<UserNodePo> getPageByParams(Page<UserNodePo> page, UserNodeRequest request);
}
