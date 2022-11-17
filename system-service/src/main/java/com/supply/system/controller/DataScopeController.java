package com.supply.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import com.supply.system.model.po.DataScopeTypePo;
import com.supply.system.model.request.DataScopeRangeRequest;
import com.supply.system.model.request.DataScopeTypeRequest;
import com.supply.system.model.response.DataScopeRangeResponse;
import com.supply.system.model.response.DataScopeTypeResponse;
import com.supply.system.service.IDataScopeService;
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
 * @description 用户资源数据权限控制层.
 * @date 2022-09-08
 */
@Api(tags="用户资源数据权限控制层")
@RestController
@RequestMapping("/dataScope")
public class DataScopeController {

    private final IDataScopeService dataScopeService;

    public DataScopeController(IDataScopeService dataScopeService) {
        this.dataScopeService = dataScopeService;
    }

    @ApiOperation(value = "新增用户资源数据权限")
    @PostMapping("/addDataScope")
    public Result<Object> addDataScope(@RequestBody DataScopeTypeRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        dataScopeService.addDataScope(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改用户资源数据权限")
    @PostMapping("/updateDataScope")
    public Result<Object> updateDataScope(@RequestBody DataScopeTypeRequest request) {
        dataScopeService.updateDataScope(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户资源数据权限")
    @GetMapping("/delDataScopeById")
    public Result<Object> delDataScopeById(@RequestParam Long dataScopeTypeId) {
        dataScopeService.delDataScopeById(dataScopeTypeId);
        return Result.ok();
    }

    @ApiOperation(value = "用户资源数据权限分页信息")
    @GetMapping("/getDataScopePageByParams")
    public Result<IPage<DataScopeTypeResponse>> getDataScopePageByParams(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                                         @RequestParam(required = false) Long resourceId, @RequestParam(required = false) Integer dataScopeType,
                                                                         @RequestParam(required = false) Long userId) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        DataScopeTypeRequest request = new DataScopeTypeRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setResourceId(resourceId);
        request.setUserId(userId);
        request.setDataScopeType(dataScopeType);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setTenantId(tenantId);
        request.setOrderColumn(DataScopeTypePo::getCreateTime);
        final IPage<DataScopeTypeResponse> page = dataScopeService.getPageByParams(request);
        return Result.ok(page);
    }

    @ApiOperation(value = "根据自定义条件查询数据权限范围信息集")
    @GetMapping("/getDataScopeRangeListByParams")
    public Result<List<DataScopeRangeResponse>> getDataScopeRangeListByParams(@RequestParam(required = false) Long dataScopeTypeId) {
        DataScopeRangeRequest request = new DataScopeRangeRequest();
        request.setDataScopeTypeId(dataScopeTypeId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<DataScopeRangeResponse> list = dataScopeService.getDataScopeRangeListByParams(request);
        return Result.ok(list);
    }
}
