package com.supply.bpm.service;

import com.supply.bpm.model.response.TaskOpinionResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-07-04
 */
public interface ITaskOpinionService {

    List<TaskOpinionResponse> getTaskOpinions(String instanceId, String businessId);

    /**
     * @description 流程运行图.
     * @author wjd
     * @date 2023/6/29
     * @param instanceId 流程运行实例
     */
    void getActImage(String instanceId, String businessId, HttpServletResponse response);
}
