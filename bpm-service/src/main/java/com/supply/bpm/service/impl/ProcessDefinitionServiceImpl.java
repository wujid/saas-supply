package com.supply.bpm.service.impl;

import com.supply.bpm.repository.IProcessDefinitionRepository;
import com.supply.bpm.service.IProcessDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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


}
