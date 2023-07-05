package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.TaskOpinionPo;
import com.supply.bpm.model.request.TaskOpinionRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-07-04
 */
public interface ITaskOpinionRepository extends IService<TaskOpinionPo> {

    int updateByParams(TaskOpinionPo taskOpinionPo, TaskOpinionRequest request);

    TaskOpinionPo getByParams(TaskOpinionRequest request);

    List<TaskOpinionPo> getListByParams(TaskOpinionRequest request);

    Page<TaskOpinionPo> getPageByParams(Page<TaskOpinionPo> page, TaskOpinionRequest request);

    TaskOpinionPo getByTaskId(String taskId);
}
