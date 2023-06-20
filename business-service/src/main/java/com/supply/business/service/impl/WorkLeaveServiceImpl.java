package com.supply.business.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.business.constant.BusinessConstant;
import com.supply.business.cvt.WorkLeaveCvt;
import com.supply.business.model.po.WorkLeavePo;
import com.supply.business.model.request.WorkLeaveRequest;
import com.supply.business.model.response.WorkLeaveResponse;
import com.supply.business.repository.IWorkLeaveRepository;
import com.supply.business.service.IWorkLeaveService;
import com.supply.common.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2023-06-19
 */
@Service
public class WorkLeaveServiceImpl implements IWorkLeaveService {

    private final IWorkLeaveRepository workLeaveRepository;

    private final Snowflake snowflake;

    public WorkLeaveServiceImpl(IWorkLeaveRepository workLeaveRepository, Snowflake snowflake) {
        this.workLeaveRepository = workLeaveRepository;
        this.snowflake = snowflake;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWorkLeave(WorkLeaveRequest request) {
        final WorkLeavePo workLeavePo = WorkLeaveCvt.INSTANCE.requestToPo(request);
        final String businessId = snowflake.nextIdStr();
        workLeavePo.setBpmBusinessId(businessId);
        workLeavePo.setBusinessStatus(BusinessConstant.IN_PROCESS);
        workLeaveRepository.save(workLeavePo);
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
