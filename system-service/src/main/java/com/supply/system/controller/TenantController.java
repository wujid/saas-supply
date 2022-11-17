package com.supply.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.system.model.po.TenantPo;
import com.supply.system.model.request.TenantRequest;
import com.supply.system.model.request.TenantSaveRequest;
import com.supply.system.model.response.TenantDetailResponse;
import com.supply.system.model.response.TenantResponse;
import com.supply.system.service.ITenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wjd
 * @description 租户信息控制层.
 * @date 2022-07-08
 */
@Api(tags="租户信息控制层")
@RestController
@RequestMapping("/tenant")
public class TenantController {

    private final ITenantService tenantService;

    public TenantController(ITenantService tenantService) {
        this.tenantService = tenantService;
    }

    @ApiOperation(value = "保存租户信息")
    @PostMapping("/addTenant")
    public Result<Object> saveTenant(@RequestBody TenantSaveRequest request) {
        tenantService.addTenant(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改租户信息")
    @PostMapping("/updateTenant")
    public Result<Object> updateTenant(@RequestBody TenantSaveRequest request) {
        tenantService.updateTenant(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除租户")
    @GetMapping("/delTenant")
    public Result<Object> delTenant(@RequestParam Long tenantId) {
        tenantService.delTenant(tenantId);
        return Result.ok();
    }

    @ApiOperation(value = "冻结租户")
    @GetMapping("/freezeTenant")
    public Result<Object> freezeRole(@RequestParam Long tenantId) {
        tenantService.freezeTenant(tenantId);
        return Result.ok();
    }

    @ApiOperation(value = "激活租户")
    @GetMapping("/activeTenant")
    public Result<Object> activeTenant(@RequestParam Long tenantId) {
        tenantService.activeTenant(tenantId);
        return Result.ok();
    }

    @ApiOperation(value = "租户续期")
    @GetMapping("/renewalDays")
    public Result<Object> renewalDays(@RequestParam Long tenantId, @RequestParam Integer days) {
        tenantService.renewalDays(tenantId, days);
        return Result.ok();
    }


    @ApiOperation(value = "可用租户列表")
    @GetMapping("/getTenantList")
    public Result<List<TenantResponse>> getTenantList() {
        TenantRequest request = new TenantRequest();
        request.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<TenantResponse> list = tenantService.getTenantListByParams(request);
        return Result.ok(list);
    }

    @ApiOperation(value = "租户分页")
    @GetMapping("/getTenantPage")
    public Result<Object> getTenantPage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                        @RequestParam(required = false) String code, @RequestParam(required = false) String name,
                                        @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer businessStatus,
                                        @RequestParam(required = false) String endTimeStart, @RequestParam(required = false) String endTimeEnd) {
        TenantRequest request = new TenantRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setCode(code);
        request.setLikeName(name);
        request.setType(type);
        request.setBusinessStatus(businessStatus);
        request.setEndTimeStart(endTimeStart);
        request.setEndTimeEnd(endTimeEnd);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(TenantPo::getCreateTime);
        final IPage<TenantResponse> userPage = tenantService.getTenantPage(request);
        return Result.ok(userPage);
    }

    @ApiOperation(value = "根据租户ID获取详细信息")
    @GetMapping("/getDetailById")
    public Result<TenantDetailResponse> getDetailById(@RequestParam Long tenantId) {
        final TenantDetailResponse detail = tenantService.getDetailById(tenantId);
        return Result.ok(detail);
    }

}
