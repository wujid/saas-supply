package com.supply.bpm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.cvt.ProcessDefinitionCvt;
import com.supply.bpm.model.po.ProcessDefinitionPo;
import com.supply.bpm.model.request.ProcessDefinitionRequest;
import com.supply.bpm.model.response.ProcessDefinitionResponse;
import com.supply.bpm.repository.IProcessDefinitionRepository;
import com.supply.bpm.service.IProcessDefinitionService;
import com.supply.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
@Service
public class ProcessDefinitionServiceImpl implements IProcessDefinitionService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessDefinitionServiceImpl.class);

    private final IProcessDefinitionRepository processDefinitionRepository;

    public ProcessDefinitionServiceImpl(IProcessDefinitionRepository processDefinitionRepository) {
        this.processDefinitionRepository = processDefinitionRepository;
    }


    @Override
    public IPage<ProcessDefinitionResponse> getProcessDefinitionPage(ProcessDefinitionRequest request) {
        Page<ProcessDefinitionPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<ProcessDefinitionPo> poPage = processDefinitionRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<ProcessDefinitionPo> poList = poPage.getRecords();
        final List<ProcessDefinitionResponse> responseList = ProcessDefinitionCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }
}
