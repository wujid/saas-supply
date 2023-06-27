package com.supply.bpm.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.po.NodeButtonPo;
import com.supply.bpm.model.request.NodeButtonRequest;
import com.supply.bpm.model.response.NodeButtonResponse;
import com.supply.bpm.service.INodeButtonService;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
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
 * @description 用户节点操作按钮信息控制层.
 * @date 2023-01-05
 */
@Api(tags="用户节点操作按钮信息控制层")
@RestController
@RequestMapping("/nodeButton")
public class NodeButtonController {

    private final INodeButtonService nodeButtonService;

    public NodeButtonController(INodeButtonService nodeButtonService) {
        this.nodeButtonService = nodeButtonService;
    }

    @ApiOperation(value = "新增流程节点按钮")
    @PostMapping("/addNodeButton")
    public Result<?> addNodeButton(@RequestBody NodeButtonRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        nodeButtonService.addNodeButton(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改流程节点按钮")
    @PostMapping("/updateNodeButton")
    public Result<?> updateNodeButton(@RequestBody NodeButtonRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        nodeButtonService.updateNodeButton(request);
        return Result.ok();
    }

    @ApiOperation(value = "流程节点按钮删除")
    @GetMapping("/delNodeButton")
    public Result<?> delNodeButton(@RequestParam Long id) {
        nodeButtonService.delNodeButton(id);
        return Result.ok();
    }

    @ApiOperation(value = "流程节点按钮冻结")
    @GetMapping("/freezeNodeButton")
    public Result<?> freezeNodeButton(@RequestParam Long id) {
        nodeButtonService.freezeNodeButton(id);
        return Result.ok();
    }

    @ApiOperation(value = "流程节点按钮解冻")
    @GetMapping("/activeNodeButton")
    public Result<?> activeNodeButton(@RequestParam Long id) {
        nodeButtonService.activeNodeButton(id);
        return Result.ok();
    }

    @ApiOperation(value = "流程节点按钮分页信息")
    @GetMapping("/getNodeButtonPage")
    public Result<?> getNodeButtonPage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                       @RequestParam(required = false) Long nodeSetId, @RequestParam(required = false) Integer businessStatus) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        NodeButtonRequest request = new NodeButtonRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setNodeSetId(nodeSetId);
        request.setBusinessStatus(businessStatus);
        request.setTenantId(tenantId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(NodeButtonPo::getSort);
        request.setIsAsc(true);
        final IPage<NodeButtonResponse> data = nodeButtonService.getNodeButtonPage(request);
        return Result.ok(data);
    }

    @ApiOperation(value = "根据流程节点设置ID查询按钮信息")
    @GetMapping("/getNodeButtonByNodeSetId")
    public Result<?> getNodeButtonByNodeSetId(@RequestParam Long nodeSetId) {
        NodeButtonRequest request = new NodeButtonRequest();
        request.setNodeSetId(nodeSetId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        final List<NodeButtonResponse> data = nodeButtonService.getListByParams(request);
        if (CollectionUtil.isEmpty(data)) {
            return Result.error("当前审批人未配置流程操作按钮");
        }
        return Result.ok(data);
    }

}
