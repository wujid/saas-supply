package com.supply.bpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.bpm.cvt.BusinessVariableCvt;
import com.supply.bpm.model.po.BusinessVariablePo;
import com.supply.bpm.model.request.BusinessVariableRequest;
import com.supply.bpm.model.response.BusinessVariableResponse;
import com.supply.bpm.repository.IBusinessVariableRepository;
import com.supply.bpm.service.IBusinessVariableService;
import com.supply.common.constant.Constant;
import com.supply.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
@Service
public class BusinessVariableServiceImpl implements IBusinessVariableService {
    private static final Logger logger = LoggerFactory.getLogger(BusinessVariableServiceImpl.class);

    private final IBusinessVariableRepository businessVariableRepository;

    public BusinessVariableServiceImpl(IBusinessVariableRepository businessVariableRepository) {
        this.businessVariableRepository = businessVariableRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBusinessVariable(BusinessVariableRequest request) {
        logger.info("[新增流程业务参数信息]---实体信息为{}", JSON.toJSONString(request));
        final BusinessVariablePo businessVariable = BusinessVariableCvt.INSTANCE.requestToPo(request);
        businessVariableRepository.save(businessVariable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBusinessVariable(BusinessVariableRequest request) {
        logger.info("[修改流程业务参数信息]---实体信息为{}", JSON.toJSONString(request));
        // 保存流程业务参数信息集
        final BusinessVariablePo businessVariable = BusinessVariableCvt.INSTANCE.requestToPo(request);
        businessVariableRepository.updateById(businessVariable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBusinessVariable(Long id) {
        logger.info("[删除流程业务参数]---待删除的主键ID为{}", id);
        final BusinessVariablePo businessVariable = new BusinessVariablePo();
        businessVariable.setId(id);
        businessVariable.setStatus(Constant.STATUS_DEL);
        businessVariableRepository.updateById(businessVariable);
    }

    @Override
    public IPage<BusinessVariableResponse> getBusinessVariablePage(BusinessVariableRequest request) {
        Page<BusinessVariablePo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<BusinessVariablePo> poPage = businessVariableRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<BusinessVariablePo> poList = poPage.getRecords();
        final List<BusinessVariableResponse> responseList = BusinessVariableCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }


}
