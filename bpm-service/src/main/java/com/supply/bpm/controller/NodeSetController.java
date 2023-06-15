package com.supply.bpm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.po.NodeSetPo;
import com.supply.bpm.model.request.NodeSetRequest;
import com.supply.bpm.model.response.NodeSetResponse;
import com.supply.bpm.service.INodeSetService;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description 流程节点设置信息控制层.
 * @date 2023-06-15
 */
@Api(tags="流程节点设置信息控制层")
@RestController
@RequestMapping("/nodeSet")
public class NodeSetController {

    private final INodeSetService userNodeService;

    public NodeSetController(INodeSetService userNodeService) {
        this.userNodeService = userNodeService;
    }


    @ApiOperation(value = "根据主键ID修改详情表单url")
    @GetMapping("/updateUserNodeFormUrl")
    public Result<?> updateUserNodeFormUrl(@RequestParam Long id, @RequestParam String formUrl) {
        userNodeService.updateUserNodeFormUrl(id, formUrl);
        return Result.ok();
    }

    @ApiOperation(value = "根据主键ID修改详情表单url")
    @GetMapping("/getUserNodePage")
    public Result<?> getUserNodePage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                     @RequestParam(required = false) String nodeId, @RequestParam(required = false) String nodeName,
                                     @RequestParam(required = false) Integer sort) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        NodeSetRequest request = new NodeSetRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setNodeId(nodeId);
        request.setNodeName(nodeName);
        request.setSort(sort);
        request.setTenantId(tenantId);
        request.setOrderColumn(NodeSetPo::getSort);
        request.setIsAsc(true);
        final IPage<NodeSetResponse> data = userNodeService.getUserNodePage(request);
        return Result.ok(data);
    }
}
