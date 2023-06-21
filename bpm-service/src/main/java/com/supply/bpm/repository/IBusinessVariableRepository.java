package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.BusinessVariablePo;
import com.supply.bpm.model.request.BusinessVariableRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-04
 */
public interface IBusinessVariableRepository extends IService<BusinessVariablePo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2023/1/4
     * @param businessVariablePo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(BusinessVariablePo businessVariablePo, BusinessVariableRequest request);

    /**
     * @description 根据自定义条件查询流程业务参数信息.
     * @author wjd
     * @date 2023/1/4
     * @param request 查询条件
     * @return 流程业务参数信息
     */
    BusinessVariablePo getByParams(BusinessVariableRequest request);

    /**
     * @description 根据自定义条件查询流程业务参数信息集.
     * @author wjd
     * @date 2023/1/4
     * @param request 查询条件
     * @return 流程业务参数信息集
     */
    List<BusinessVariablePo> getListByParams(BusinessVariableRequest request);

    /**
     * @description 根据自定义条件查询流程业务参数带分页信息.
     * @author wjd
     * @date 2023/1/4
     * @param page 分页信息
     * @param request 查询条件
     * @return 带分页的结果
     */
    Page<BusinessVariablePo> getPageByParams(Page<BusinessVariablePo> page, BusinessVariableRequest request);

    List<BusinessVariablePo> getListByDefinitionId(String definitionId);
}
