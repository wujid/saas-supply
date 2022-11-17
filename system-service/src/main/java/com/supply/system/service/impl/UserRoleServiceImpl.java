package com.supply.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.system.cvt.UserRoleCvt;
import com.supply.system.model.po.UserRolePo;
import com.supply.system.model.request.UserRoleRequest;
import com.supply.system.model.response.UserRoleResponse;
import com.supply.system.repository.IUserRoleRepository;
import com.supply.system.service.IUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-28
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {
    private static final Logger logger = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    private final IUserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(IUserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserRole(UserRoleRequest request) {
        logger.info("[新增用户角色关联关系]---待新增的实体为{}", JSON.toJSONString(request));
        // 验证是否已经关联
        UserRoleRequest userRoleRequest = new UserRoleRequest();
        userRoleRequest.setRoleId(request.getRoleId());
        userRoleRequest.setUserId(request.getUserId());
        userRoleRequest.setStatus(Constant.STATUS_NOT_DEL);
        final Long count = userRoleRepository.getCountByParams(userRoleRequest);
        if (count > 0) {
            final String message = "该角色下已存在该用户!";
            logger.info(message);
            throw new ApiException(message);
        }
        final UserRolePo userRolePo = UserRoleCvt.INSTANCE.requestToPo(request);
        userRoleRepository.save(userRolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUserRoleByParams(UserRoleRequest request) {
        logger.info("[删除用户角色关联关系]---条件为{}", JSON.toJSONString(request));
        UserRolePo userRolePo = new UserRolePo();
        userRolePo.setStatus(Constant.STATUS_DEL);
        userRoleRepository.updateByParams(userRolePo, request);
    }

    @Override
    public List<UserRoleResponse> getUserRoleListByParams(UserRoleRequest request) {
        final List<UserRolePo> poList = userRoleRepository.getListByParams(request);
        return UserRoleCvt.INSTANCE.poToResponseBatch(poList);
    }
}
