package com.supply.bpm.service;

import com.supply.bpm.model.request.TaskOpinionRequest;
import com.supply.bpm.model.response.TaskOpinionResponse;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-07-04
 */
public interface ITaskOpinionService {
    List<TaskOpinionResponse> getByParams(TaskOpinionRequest request);
}
