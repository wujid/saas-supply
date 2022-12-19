package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.ActModelPo;
import com.supply.bpm.model.request.ActModelRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-19
 */
public interface IActModelRepository extends IService<ActModelPo> {


    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/11/3
     * @param actModelPo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(ActModelPo actModelPo,  ActModelRequest request);

    /**
     * @description 根据自定义条件查询流程模型.
     * @author wjd
     * @date 2022/10/10
     * @param request 查询条件
     * @return 流程模型
     */
    ActModelPo getByParams(ActModelRequest request);

    /**
     * @description 根据自定义条件查询流程模型集.
     * @author wjd
     * @date 2022/10/10
     * @param request 查询条件
     * @return 流程模型集
     */
    List<ActModelPo> getListByParams(ActModelRequest request);

    /**
     * @description 根据自定义条件查询带分页信息.
     * @author wjd
     * @date 2022/11/1
     * @param page 分页信息
     * @param request 查询条件
     * @return 带分页的结果
     */
    Page<ActModelPo> getPageByParams(Page<ActModelPo> page, ActModelRequest request);
}
