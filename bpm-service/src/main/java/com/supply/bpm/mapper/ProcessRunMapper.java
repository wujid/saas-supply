package com.supply.bpm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.model.po.ProcessRunPo;
import com.supply.bpm.model.request.TaskRequest;
import com.supply.bpm.model.response.TaskResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wjd
 * @description .
 * @date 2023-06-20
 */
@Mapper
public interface ProcessRunMapper extends BaseMapper<ProcessRunPo> {

    /**
      * @description 我的待办.
      * @author wjd
      * @date 2023/7/4
      * @param page 分页信息
      * @param request 条件信息
      * @return 我的待办
      */
    Page<TaskResponse> getMyTask(@Param("page") Page<TaskRequest> page, @Param("request") TaskRequest request);

    /**
     * @description 我发起的流程.
     * @author wjd
     * @date 2023/7/4
     * @param page 分页信息
     * @param request 条件信息
     * @return 我发起的流程
     */
    Page<TaskResponse> getMyStart(@Param("page") Page<TaskRequest> page, @Param("request") TaskRequest request);

    /**
     * @description 我参与的流程.
     * @author wjd
     * @date 2023/7/4
     * @param page 分页信息
     * @param request 条件信息
     * @return 我参与的流程
     */
    Page<TaskResponse> getMyAttend(@Param("page") Page<TaskRequest> page, @Param("request") TaskRequest request);

    /**
     * @description 流程审批历史.
     * @author wjd
     * @date 2023/7/4
     * @param page 分页信息
     * @param request 条件信息
     * @return 流程审批历史
     */
    Page<TaskResponse> getBpmHistory(@Param("page") Page<TaskRequest> page, @Param("request") TaskRequest request);
}
