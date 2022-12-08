package com.supply.system.service.impl;

import com.supply.system.repository.IUserThirdRepository;
import com.supply.system.service.IUserThirdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author wjd
 * @description .
 * @date 2022-12-08
 */
@Service
public class UserThirdServiceImpl implements IUserThirdService {
    private static final Logger logger = LoggerFactory.getLogger(UserThirdServiceImpl.class);

    private final IUserThirdRepository userThirdRepository;

    public UserThirdServiceImpl(IUserThirdRepository userThirdRepository) {
        this.userThirdRepository = userThirdRepository;
    }


}
