package com.supply.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.business.api.BpmClient;
import com.supply.business.constant.BusinessConstant;
import com.supply.business.cvt.WorkLeaveCvt;
import com.supply.business.model.po.WorkLeavePo;
import com.supply.business.model.request.WorkLeaveRequest;
import com.supply.business.model.response.WorkLeaveResponse;
import com.supply.business.repository.IWorkLeaveRepository;
import com.supply.business.service.IWorkLeaveService;
import com.supply.common.util.CommonUtil;
import com.supply.common.web.model.BpmRequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author wjd
 * @description .
 * @date 2023-06-19
 */
@Service
public class WorkLeaveServiceImpl implements IWorkLeaveService {

    private final IWorkLeaveRepository workLeaveRepository;

    private final Snowflake snowflake;

    private final BpmClient bpmClient;

    public WorkLeaveServiceImpl(IWorkLeaveRepository workLeaveRepository, Snowflake snowflake, BpmClient bpmClient) {
        this.workLeaveRepository = workLeaveRepository;
        this.snowflake = snowflake;
        this.bpmClient = bpmClient;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWorkLeave(WorkLeaveRequest request) {
        final WorkLeavePo workLeavePo = WorkLeaveCvt.INSTANCE.requestToPo(request);
        final String businessId = snowflake.nextIdStr();
        workLeavePo.setBusinessId(businessId);
        workLeavePo.setBusinessStatus(BusinessConstant.IN_PROCESS);
        workLeaveRepository.save(workLeavePo);
        request.setBpmBusinessId(businessId);

        // 转换并设置流程发起人及流程所需的业务参数
        BpmRequestEntity bpmRequest = request;
        // 可直接转换后由流程自行过滤也可准确给值(只能多不能少防止流程因缺失参数异常)
        final Map<String, Object> map = BeanUtil.beanToMap(request);
        bpmRequest.setBpmStartUserId(request.getApplyUserId());
        bpmRequest.setBpmBusinessVariableMap(map);
        bpmClient.startProcess(bpmRequest);
    }

    @Override
    public IPage<WorkLeaveResponse> getPageByParams(WorkLeaveRequest request) {
        Page<WorkLeavePo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<WorkLeavePo> poPage = workLeaveRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<WorkLeavePo> poList = page.getRecords();
        final List<WorkLeaveResponse> responseList = WorkLeaveCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    @Override
    public WorkLeaveResponse getInfoById(Long id) {
        final WorkLeavePo workLeavePo = workLeaveRepository.getById(id);
        return WorkLeaveCvt.INSTANCE.poToResponse(workLeavePo);
    }
}
