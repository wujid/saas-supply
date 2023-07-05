package com.supply.bpm.service.impl;

import com.supply.bpm.repository.ITaskOpinionRepository;
import com.supply.bpm.service.ITaskOpinionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @description: .
 * @author: wjd
 * @date 2023-07-04
 */
@Service
public class TaskOpinionServiceImpl implements ITaskOpinionService {
    private static final Logger logger = LoggerFactory.getLogger(TaskOpinionServiceImpl.class);

    private final ITaskOpinionRepository taskOpinionRepository;

    public TaskOpinionServiceImpl(ITaskOpinionRepository taskOpinionRepository) {
        this.taskOpinionRepository = taskOpinionRepository;
    }
}
