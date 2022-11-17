package com.supply.message.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.message.model.po.TemplateTenantPo;
import com.supply.message.model.request.TemplateTenantRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-10-31
 */
public interface ITemplateTenantRepository extends IService<TemplateTenantPo> {

    /**
      * @description 根据自定义条件修改.
      * @author wjd
      * @date 2022/10/31
      * @param templateTenantPo 修改实体
      * @param request 条件值
      * @return int 受影响行数
      */
    int updateByParams(TemplateTenantPo templateTenantPo, @IgnoreFill TemplateTenantRequest request);

    /**
      * @description 根据条件查询消息模板和租户关联关系信息集.
      * @author wjd
      * @date 2022/10/31
      * @param request 查询条件
      * @return 消息模板和租户关联关系信息集
      */
    List<TemplateTenantPo> getListByParams(TemplateTenantRequest request);
}
