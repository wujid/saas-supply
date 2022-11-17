package com.supply.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.web.util.ContextUtil;
import com.supply.message.model.po.ContentInfoPo;
import com.supply.message.model.request.ContentInfoRequest;
import com.supply.message.model.request.SendMessageRequest;
import com.supply.message.model.response.ContentInfoResponse;
import com.supply.message.service.IContentService;
import com.supply.message.service.ISendMessageService;
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
 * @description 消息内容信息权限控制层.
 * @date 2022-11-01
 */
@Api(tags="消息内容信息权限控制层")
@RestController
@RequestMapping("/content")
public class ContentInfoController {

    private final IContentService contentService;

    private final ISendMessageService sendMessageService;

    public ContentInfoController(IContentService contentService, ISendMessageService sendMessageService) {
        this.contentService = contentService;
        this.sendMessageService = sendMessageService;
    }

    @ApiOperation(value = "修改消息内容为已读状态")
    @GetMapping("/updateReaderById")
    public Result<Object> updateReaderById(@RequestParam Long contentId) {
        contentService.updateReaderById(contentId);
        return Result.ok();
    }

    @ApiOperation(value = "修改消息全部已读")
    @GetMapping("/updateAllReader")
    public Result<Object> updateAllReader() {
        final SysUserResponse currentUser = ContextUtil.getCurrentUser();
        if (null == currentUser) {
            return Result.error("未获取到当前登录人信息!");
        }
        contentService.updateAllReader(currentUser.getId());
        return Result.ok();
    }

    @ApiOperation(value = "删除消息内容")
    @GetMapping("/delContentById")
    public Result<Object> delContentById(@RequestParam Long contentId) {
        contentService.delContentById(contentId);
        return Result.ok();
    }

    @ApiOperation(value = "消息内容分页信息")
    @GetMapping("/getContentPage")
    public Result<IPage<ContentInfoResponse>> getContentPage(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                             @RequestParam(required = false) Integer msgType, @RequestParam(required = false) Integer businessStatus,
                                                             @RequestParam(required = false) Integer notifyType) {
        final SysUserResponse currentUser = ContextUtil.getCurrentUser();
        if (null == currentUser) {
            return Result.error("未获取到当前登录人信息!");
        }
        ContentInfoRequest request = new ContentInfoRequest();
        request.setPageSize(pageSize);
        request.setPageIndex(pageIndex);
        request.setMsgType(msgType);
        request.setBusinessStatus(businessStatus);
        request.setNotifyType(notifyType);
        request.setOrderColumn(ContentInfoPo::getCreateTime);
        request.setReceiverUserId(currentUser.getId());
        request.setStatus(Constant.STATUS_NOT_DEL);
        final IPage<ContentInfoResponse> contentPage = contentService.getContentPage(request);
        return Result.ok(contentPage);
    }

    @ApiOperation(value = "发送消息")
    @PostMapping("/sendMessage")
    public Result<Object> sendMessage(@RequestBody SendMessageRequest request) {
        sendMessageService.sendMessage(request);
        return Result.ok();
    }
}
