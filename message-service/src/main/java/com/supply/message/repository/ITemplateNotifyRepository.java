package com.supply.message.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.message.model.po.TemplateNotifyPo;
import com.supply.message.model.request.TemplateNotifyRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-10-29
 */
public interface ITemplateNotifyRepository extends IService<TemplateNotifyPo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/10/29
     * @param templateNotifyPo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(TemplateNotifyPo templateNotifyPo, @IgnoreFill TemplateNotifyRequest request);


    /**
     * @author wjd
     * @description 根据条件查询消息通知方式信息集.
     * @date 2022/10/29
     * @param request 待查询的条件
     * @return 消息通知方式信息集
     **/
    List<TemplateNotifyPo> getListByParams(TemplateNotifyRequest request);
}
