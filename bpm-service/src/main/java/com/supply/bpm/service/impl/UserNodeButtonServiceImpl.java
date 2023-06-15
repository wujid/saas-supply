package com.supply.bpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.supply.bpm.cvt.UserNodeButtonCvt;
import com.supply.bpm.model.po.UserNodeButtonPo;
import com.supply.bpm.model.request.UserNodeButtonRequest;
import com.supply.bpm.model.response.UserNodeButtonResponse;
import com.supply.bpm.repository.IUserNodeButtonRepository;
import com.supply.bpm.service.IUserNodeButtonService;
import com.supply.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-06-15
 */
@Service
public class UserNodeButtonServiceImpl implements IUserNodeButtonService {
    private static final Logger logger = LoggerFactory.getLogger(UserNodeButtonServiceImpl.class);

    private final IUserNodeButtonRepository userNodeButtonRepository;

    public UserNodeButtonServiceImpl(IUserNodeButtonRepository userNodeButtonRepository) {
        this.userNodeButtonRepository = userNodeButtonRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserNodeButton(List<UserNodeButtonRequest> requests) {
        logger.info("[新增流程用户节点按钮信息集]---实体信息为{}", JSON.toJSONString(requests));
        final List<UserNodeButtonPo> nodeButtonPoList = UserNodeButtonCvt.INSTANCE.requestToPoBatch(requests);
        userNodeButtonRepository.saveBatch(nodeButtonPoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserNodeButton(List<UserNodeButtonRequest> requests) {
        logger.info("[修改流程节点按钮信息集]---实体信息为{}", JSON.toJSONString(requests));
        // 删除历史流程节点按钮信息集
        final Long userNodeId = requests.stream().findFirst().get().getUserNodeId();
        UserNodeButtonPo userNodeButtonPo = new UserNodeButtonPo();
        userNodeButtonPo.setStatus(Constant.STATUS_DEL);
        UserNodeButtonRequest request = new UserNodeButtonRequest();
        request.setUserNodeId(userNodeId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        userNodeButtonRepository.updateByParams(userNodeButtonPo, request);
        // 保存新流程节点按钮信息集
        final List<UserNodeButtonPo> nodeButtonPoList = UserNodeButtonCvt.INSTANCE.requestToPoBatch(requests);
        userNodeButtonRepository.saveBatch(nodeButtonPoList);
    }

    @Override
    public List<UserNodeButtonResponse> getUserNodeButtonListByParams(UserNodeButtonRequest request) {
        final List<UserNodeButtonPo> nodeButtonPoList = userNodeButtonRepository.getListByParams(request);
        return UserNodeButtonCvt.INSTANCE.poToResponseBatch(nodeButtonPoList);
    }
}
