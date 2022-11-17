package com.supply.system.controller;

import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import com.supply.system.model.po.OrgPo;
import com.supply.system.model.request.OrgRequest;
import com.supply.system.model.response.OrgResponse;
import com.supply.system.service.IOrgService;
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
 * @description 组织机构控制层.
 * @date 2022-08-12
 */
@Api(tags="组织机构控制层")
@RestController
@RequestMapping("/org")
public class OrgController {

    private final IOrgService orgService;

    public OrgController(IOrgService orgService) {
        this.orgService = orgService;
    }

    @ApiOperation(value = "新增组织机构")
    @PostMapping("/addOrg")
    public Result<Object> addOrg(@RequestBody OrgRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        orgService.addOrg(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改组织机构")
    @PostMapping("/updateOrg")
    public Result<Object> updateOrg(@RequestBody OrgRequest orgId) {
        orgService.updateOrg(orgId);
        return Result.ok();
    }

    @ApiOperation(value = "删除组织机构")
    @GetMapping("/delOrg")
    public Result<Object> delOrg(@RequestParam Long orgId) {
        orgService.delOrg(orgId);
        return Result.ok();
    }

    @ApiOperation(value = "组织机构冻结")
    @GetMapping("/freezeOrg")
    public Result<Object> freezeOrg(@RequestParam Long orgId) {
        orgService.freezeOrg(orgId);
        return Result.ok();
    }

    @ApiOperation(value = "组织机构解冻")
    @GetMapping("/activeOrg")
    public Result<Object> activeOrg(@RequestParam Long orgId) {
        orgService.activeOrg(orgId);
        return Result.ok();
    }

    @ApiOperation(value = "根据父级查询对应的子集")
    @GetMapping("/getOrgListByParentId")
    public Result<List<OrgResponse>> getOrgListByParentId(@RequestParam Long parentId) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        OrgRequest request = new OrgRequest();
        request.setTenantId(tenantId);
        request.setParentId(parentId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(OrgPo::getSort);
        request.setIsAsc(true);
        final List<OrgResponse> list = orgService.getOrgListByParams(request);
        return Result.ok(list);
    }

    @ApiOperation(value = "组织机构树结构")
    @GetMapping("/getOrgTreeByParams")
    public Result<List<OrgResponse>> getOrgTreeByParams(@RequestParam(required = false) Long parentId, @RequestParam(required = false) String name) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        final List<OrgResponse> list = orgService.getOrgTree(tenantId, parentId, name);
        return Result.ok(list);
    }
}
