package com.supply.bpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.supply.bpm.model.response.ProcessRunResponse;
import com.supply.bpm.repository.IProcessRunRepository;
import com.supply.bpm.service.IProcessRunService;
import com.supply.common.web.model.BpmRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author wjd
 * @description .
 * @date 2023-06-20
 */
@Service
public class ProcessRunServiceImpl implements IProcessRunService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessRunServiceImpl.class);

    private final IProcessRunRepository processRunRepository;

    public ProcessRunServiceImpl(IProcessRunRepository processRunRepository) {
        this.processRunRepository = processRunRepository;
    }

    public ProcessRunResponse startProcess(BpmRequestEntity request) {
        logger.info("[流程发起]---待发起的实体信息为{}", JSON.toJSONString(request));
        return null;
    }
}
