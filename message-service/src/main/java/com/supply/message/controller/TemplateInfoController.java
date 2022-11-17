package com.supply.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.message.model.request.TemplateInfoRequest;
import com.supply.message.model.response.TemplateInfoResponse;
import com.supply.message.service.ITemplateInfoService;
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
 * @description 消息模板信息权限控制层.
 * @date 2022-10-13
 */
@Api(tags="消息模板信息权限控制层")
@RestController
@RequestMapping("/template")
public class TemplateInfoController {

    private final ITemplateInfoService templateInfoService;

    public TemplateInfoController(ITemplateInfoService templateInfoService) {
        this.templateInfoService = templateInfoService;
    }

    @ApiOperation(value = "新增消息模板")
    @PostMapping("/addTemplate")
    public Result<Object> addTemplate(@RequestBody TemplateInfoRequest request) {
        templateInfoService.addTemplate(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改消息模板")
    @PostMapping("/updateTemplate")
    public Result<Object> updateTemplate(@RequestBody TemplateInfoRequest request) {
        templateInfoService.updateTemplate(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除消息模板")
    @GetMapping("/delTemplate")
    public Result<Object> delTemplate(@RequestParam Long templateId) {
        templateInfoService.delTemplate(templateId);
        return Result.ok();
    }

    @ApiOperation(value = "根据自定义条件查询分页信息")
    @GetMapping("/getTemplatePageByParams")
    public Result<IPage<TemplateInfoResponse>> getTemplatePageByParams(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                                        @RequestParam(required = false) String code, @RequestParam(required = false) Integer msgType) {
        TemplateInfoRequest request = new TemplateInfoRequest();
        request.setCode(code);
        request.setMsgType(msgType);
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final IPage<TemplateInfoResponse> page = templateInfoService.getPageByParams(request);
        return Result.ok(page);
    }

    @ApiOperation(value = "根据模板ID获取模板详细信息")
    @GetMapping("/getTemplateInfoById")
    public Result<TemplateInfoResponse> getTemplateInfoById(@RequestParam Long templateId) {
        final TemplateInfoResponse info = templateInfoService.getTemplateInfoById(templateId);
        return Result.ok(info);
    }
}
