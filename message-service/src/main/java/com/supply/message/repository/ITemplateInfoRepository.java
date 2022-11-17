package com.supply.message.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.message.model.po.TemplateInfoPo;
import com.supply.message.model.request.TemplateInfoRequest;

/**
 * @author wjd
 * @description
 * @date 2022-10-09
 */
public interface ITemplateInfoRepository extends IService<TemplateInfoPo> {

    /**
      * @description 根据自定义条件查询消息模板.
      * @author wjd
      * @date 2022/10/10
      * @param request 查询条件
      * @return 消息模板
      */
    TemplateInfoPo getByParams(TemplateInfoRequest request);

    /**
      * @description 根据自定义条件查询条数信息.
      * @author wjd
      * @date 2022/10/13
      * @param request 查询条件
      * @return 条数
      */
    Long getCountByParams(TemplateInfoRequest request);

    /**
      * @description 根据自定义条件查询带分页的消息模板信息集.
      * @author wjd
      * @date 2022/10/13
      * @param page 分页条件
      * @param request 查询条件
      * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.supply.message.model.po.TemplateInfoPo>
      */
    Page<TemplateInfoPo> getPageByParams(Page<TemplateInfoPo> page, TemplateInfoRequest request);
}
