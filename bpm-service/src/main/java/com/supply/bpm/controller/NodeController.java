package com.supply.bpm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.NodeRequest;
import com.supply.bpm.model.response.NodeResponse;
import com.supply.bpm.service.INodeService;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description .
 * @date 2023-01-05
 */
@Api(tags="流程节点信息权限控制层")
@RestController
@RequestMapping("/node")
public class NodeController {

    private final INodeService nodeService;

    public NodeController(INodeService nodeService) {
        this.nodeService = nodeService;
    }

    @ApiOperation(value = "流程节点分页信息")
    @GetMapping("/getNodePage")
    public Result<IPage<NodeResponse>> getNodePage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                   @RequestParam(required = false) String definitionId, @RequestParam(required = false) String nodeName) {
        NodeRequest request = new NodeRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setDefinitionId(definitionId);
        request.setLikeNodeName(nodeName);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final IPage<NodeResponse> data = nodeService.getNodePage(request);
        return Result.ok(data);
    }
}
