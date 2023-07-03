package com.supply.business.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.business.model.po.WorkLeavePo;
import com.supply.business.model.request.WorkLeaveRequest;
import com.supply.business.model.response.WorkLeaveResponse;
import com.supply.business.service.IWorkLeaveService;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.web.util.ContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description .
 * @date 2023-06-19
 */
@Api(tags="请假申请")
@RestController
@RequestMapping("/workLeave")
public class WorkLeaveController {

    private final IWorkLeaveService workLeaveService;

    public WorkLeaveController(IWorkLeaveService workLeaveService) {
        this.workLeaveService = workLeaveService;
    }

    @ApiOperation(value = "新增请假申请")
    @PostMapping("/addWorkLeave")
    public Result<?> addWorkLeave(@RequestBody WorkLeaveRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        final SysUserResponse user = ContextUtil.getCurrentUser();
        if (null != user) {
            request.setApplyUserId(user.getId());
            request.setApplyUserName(user.getName());
        }
        workLeaveService.addWorkLeave(request);
        return Result.ok();
    }

    @ApiOperation(value = "获取请假申请分页信息")
    @GetMapping("/getWorkLeavePage")
    public Result<?> getWorkLeavePage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        WorkLeaveRequest request = new WorkLeaveRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(WorkLeavePo::getCreateTime);
        final IPage<WorkLeaveResponse> data = workLeaveService.getPageByParams(request);
        return Result.ok(data);
    }

    @ApiOperation(value = "获取请假申请信息")
    @GetMapping("/getWorkLeaveByBusinessId")
    public Result<?> getWorkLeaveByBusinessId(@RequestParam String businessId) {
        final WorkLeaveResponse data = workLeaveService.getInfoByBusinessId(businessId);
        return Result.ok(data);
    }
}
