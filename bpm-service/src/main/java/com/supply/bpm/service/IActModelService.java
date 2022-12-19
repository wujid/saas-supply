package com.supply.bpm.service;

import com.supply.bpm.model.request.ActModelRequest;

/**
 * @author wjd
 * @description .
 * @date 2022-12-19
 */
public interface IActModelService {

    /**
      * @description 新增流程模型.
      * @author wjd
      * @date 2022/12/19
      * @param request 待保存的流程实体
      */
    void addModel(ActModelRequest request);
}
