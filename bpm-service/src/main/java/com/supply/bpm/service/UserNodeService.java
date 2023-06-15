package com.supply.bpm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.bpm.model.request.UserNodeRequest;
import com.supply.bpm.model.response.UserNodeResponse;

/**
 * @author wjd
 * @description .
 * @date 2023-06-15
 */
public interface UserNodeService {


    /**
      * @description 根据主键ID修改详情表单url.
      * @author wjd
      * @date 2023/6/15
      * @param id 主键ID
      * @param formUrl 详情表单url
      */
    void updateUserNodeFormUrl(Long id, String formUrl);

    /**
      * @description 根据自定义条件查询流程节点信息带分页.
      * @author wjd
      * @date 2023/6/15
      * @param request 查询条件
      * @return 流程节点分页信息
      */
    IPage<UserNodeResponse> getUserNodePage(UserNodeRequest request);
}
