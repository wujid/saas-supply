package com.supply.bpm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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


    IPage<TaskResponse> getMyTask(@Param("page") Page<TaskRequest> page, @Param("request") TaskRequest request);
}
