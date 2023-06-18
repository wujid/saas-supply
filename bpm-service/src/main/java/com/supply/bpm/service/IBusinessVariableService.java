package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.BusinessVariableRequest;
import com.supply.bpm.model.response.BusinessVariableResponse;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
public interface IBusinessVariableService {

    /**
      * @description 新增流程业务参数.
      * @author wjd
      * @date 2023/1/5
      * @param request 待保存的实体信息集
      */
    void addBusinessVariable(BusinessVariableRequest request);

    /**
      * @description 修改流程业务参数.
      * @author wjd
      * @date 2023/1/5
      * @param request 待保存的实体信息集
      */
    void updateBusinessVariable(BusinessVariableRequest request);

    /**
      * @description 根据主键ID删除流程业务参数.
      * @author wjd
      * @date 2023/6/16
      * @param id 主键ID
      */
    void delBusinessVariable(Long id);

    /**
      * @description 根据自定义条件查询流程业务参数分页信息.
      * @author wjd
      * @date 2023/1/5
      * @param request 查询条件
      * @return 流程业务参数分页信息
      */
    IPage<BusinessVariableResponse> getBusinessVariablePage(BusinessVariableRequest request);
}
