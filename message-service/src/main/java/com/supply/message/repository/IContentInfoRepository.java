package com.supply.message.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.message.model.po.ContentInfoPo;
import com.supply.message.model.request.ContentInfoRequest;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-10-09
 */
public interface IContentInfoRepository extends IService<ContentInfoPo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/11/3
     * @param contentInfoPo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(ContentInfoPo contentInfoPo, ContentInfoRequest request);

    /**
      * @description 根据自定义条件查询消息内容.
      * @author wjd
      * @date 2022/10/10
      * @param request 查询条件
      * @return 消息内容
      */
    ContentInfoPo getByParams(ContentInfoRequest request);

    /**
      * @description 根据自定义条件查询消息内容集.
      * @author wjd
      * @date 2022/10/10
      * @param request 查询条件
      * @return 消息内容集
      */
    List<ContentInfoPo> getListByParams(ContentInfoRequest request);

    /**
      * @description 根据自定义条件查询带分页信息.
      * @author wjd
      * @date 2022/11/1
      * @param page 分页信息
      * @param request 查询条件
      * @return 带分页的结果
      */
    Page<ContentInfoPo> getPageByParams(Page<ContentInfoPo> page, ContentInfoRequest request);
}
