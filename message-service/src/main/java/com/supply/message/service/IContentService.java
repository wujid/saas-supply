package com.supply.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.message.model.request.ContentInfoRequest;
import com.supply.message.model.response.ContentInfoResponse;

/**
 * @author wjd
 * @description
 * @date 2022-11-01
 */
public interface IContentService {

    /**
      * @description 根据主键ID修改消息内容为已读状态.
      * @author wjd
      * @date 2022/11/1
      * @param contentId 消息内容ID
      */
    void updateReaderById(Long contentId);

    /**
      * @description 根据接收人将消息内容全部修改为已读状态.
      * @author wjd
      * @date 2022/11/3
      * @param userId 接收人ID
      */
    void updateAllReader(Long userId);

    /**
      * @description 根据主键ID删除消息内容.
      * @author wjd
      * @date 2022/11/1
      * @param contentId 消息内容ID
      */
    void delContentById(Long contentId);

    /**
     * @description 根据自定义条件查询带分页的消息内容信息集.
     * @author wjd
     * @date 2022/7/31
     * @param request 查询条件
     * @return 带分页的消息内容信息集
     */
    IPage<ContentInfoResponse> getContentPage(ContentInfoRequest request);
}
