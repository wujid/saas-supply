package com.supply.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.supply.common.constant.Constant;
import com.supply.system.constant.ThirdTypeEnum;
import com.supply.system.cvt.UserThirdCvt;
import com.supply.system.model.po.UserThirdPo;
import com.supply.system.model.request.UserThirdRequest;
import com.supply.system.model.response.UserThirdResponse;
import com.supply.system.repository.IUserThirdRepository;
import com.supply.system.service.IUserThirdService;
import com.supply.system.util.WeCatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-08
 */
@Service
public class UserThirdServiceImpl implements IUserThirdService {
    private static final Logger logger = LoggerFactory.getLogger(UserThirdServiceImpl.class);

    private final IUserThirdRepository userThirdRepository;

    private final WeCatUtil weCatUtil;

    public UserThirdServiceImpl(IUserThirdRepository userThirdRepository, WeCatUtil weCatUtil) {
        this.userThirdRepository = userThirdRepository;
        this.weCatUtil = weCatUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addThird(UserThirdRequest request) {
        logger.info("[第三方平台信息绑定用户]---待新增的实体信息为{}", JSON.toJSONString(request));
        String openId = null;
        String nickName = null;
        // 绑定微信
        if (request.getThirdType() == ThirdTypeEnum.WE_CAT.getType()) {
            // 通过授权码获取第三方用户信息
            final UserThirdResponse userThird = weCatUtil.getOpenInfoByCode(request.getCode());
            openId = userThird.getOpenId();
            nickName = userThird.getNickName();
        }
        final Long userId = request.getUserId();
        UserThirdPo userThirdPo = new UserThirdPo();
        userThirdPo.setUserId(userId);
        userThirdPo.setOpenId(openId);
        userThirdPo.setNickName(nickName);
        userThirdPo.setThirdType(request.getThirdType());
        userThirdPo.setTenantId(request.getTenantId());
        userThirdRepository.save(userThirdPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delThird(Long thirdId) {
        logger.info("[解绑第三方平台信息绑定用户]---待解绑的ID为{}", thirdId);
        UserThirdPo userThirdPo = new UserThirdPo();
        userThirdPo.setId(thirdId);
        userThirdPo.setStatus(Constant.STATUS_DEL);
        userThirdRepository.updateById(userThirdPo);
    }

    @Override
    public List<UserThirdResponse> getThirdListByParams(UserThirdRequest request) {
        final List<UserThirdPo> poList = userThirdRepository.getListByParams(request);
        return UserThirdCvt.INSTANCE.poToResponseBatch(poList);
    }


}
