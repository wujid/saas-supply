package com.supply.bpm.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.bpm.model.po.ProcessRunPo;
import com.supply.bpm.model.request.ProcessRunRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-06-20
 */
public interface IProcessRunRepository extends IService<ProcessRunPo> {
    ProcessRunPo getByParams(ProcessRunRequest request);

    List<ProcessRunPo> getListByParams(ProcessRunRequest request);

    Page<ProcessRunPo> getPageByParams(Page<ProcessRunPo> page, ProcessRunRequest request);
}
