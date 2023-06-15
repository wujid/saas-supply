package com.supply.bpm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.po.UserNodePo;
import com.supply.bpm.model.request.UserNodeRequest;
import com.supply.bpm.model.response.UserNodeResponse;
import com.supply.bpm.service.IUserNodeService;
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
 * @description .
 * @date 2023-06-15
 */
@Api(tags="用户节点信息控制层")
@RestController
@RequestMapping("/userNode")
public class UserNodeController {

    private final IUserNodeService userNodeService;

    public UserNodeController(IUserNodeService userNodeService) {
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
        UserNodeRequest request = new UserNodeRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setNodeId(nodeId);
        request.setNodeName(nodeName);
        request.setSort(sort);
        request.setTenantId(tenantId);
        request.setOrderColumn(UserNodePo::getSort);
        request.setIsAsc(true);
        final IPage<UserNodeResponse> data = userNodeService.getUserNodePage(request);
        return Result.ok(data);
    }
}
