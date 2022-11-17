package com.supply.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.message.model.request.TemplateInfoRequest;
import com.supply.message.model.response.TemplateInfoResponse;

/**
 * @author wjd
 * @description
 * @date 2022-10-13
 */
public interface ITemplateInfoService {

    /**
      * @description 新增消息模板.
      * @author wjd
      * @date 2022/10/13
      * @param request 新增实体
      */
    void addTemplate(TemplateInfoRequest request);

    /**
      * @description 修改消息模板.
      * @author wjd
      * @date 2022/10/13
      * @param request 修改实体
      */
    void updateTemplate(TemplateInfoRequest request);

    /**
      * @description 根据主键ID删除消息模板.
      * @author wjd
      * @date 2022/10/13
      * @param templateId 主键ID
      */
    void delTemplate(Long templateId);

    /**
      * @description 根据自定义条件查询带分页的消息模板信息集.
      * @author wjd
      * @date 2022/10/13
      * @param request 带分页的查询条件
      * @return 带分页的消息模板信息集
      */
    IPage<TemplateInfoResponse> getPageByParams(TemplateInfoRequest request);

    /**
      * @description 根据模板ID获取模板详细信息.
      * @author wjd
      * @date 2022/10/29
      * @param templateId 模板ID
      * @return 模板详细信息
      */
    TemplateInfoResponse getTemplateInfoById(Long templateId);
}
