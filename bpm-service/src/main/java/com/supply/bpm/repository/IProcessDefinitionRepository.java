package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.ProcessDefinitionPo;
import com.supply.bpm.model.request.ProcessDefinitionRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
public interface IProcessDefinitionRepository extends IService<ProcessDefinitionPo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/11/3
     * @param processDefinitionPo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(ProcessDefinitionPo processDefinitionPo, ProcessDefinitionRequest request);

    /**
     * @description 根据自定义条件查询流程定义信息.
     * @author wjd
     * @date 2022/10/10
     * @param request 查询条件
     * @return 流程定义信息
     */
    ProcessDefinitionPo getByParams(ProcessDefinitionRequest request);

    /**
     * @description 根据自定义条件查询流程定义信息集.
     * @author wjd
     * @date 2022/10/10
     * @param request 查询条件
     * @return 流程定义信息集
     */
    List<ProcessDefinitionPo> getListByParams(ProcessDefinitionRequest request);

    /**
     * @description 根据自定义条件查询带分页信息.
     * @author wjd
     * @date 2022/11/1
     * @param page 分页信息
     * @param request 查询条件
     * @return 带分页的结果
     */
    Page<ProcessDefinitionPo> getPageByParams(Page<ProcessDefinitionPo> page, ProcessDefinitionRequest request);

    /**
      * @description 根据自定义条件查询条数信息.
      * @author wjd
      * @date 2023/6/7
      * @param request 查询条件
      * @return 条数信息
      */
    Long getCountByParams(ProcessDefinitionRequest request);
}
