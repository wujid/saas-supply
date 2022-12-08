package com.supply.system.service;

import com.supply.system.model.request.UserThirdRequest;
import com.supply.system.model.response.UserThirdResponse;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-08
 */
public interface IUserThirdService {

    /**
      * @description 用户和第三方平台关联关系绑定.
      * @author wjd
      * @date 2022/12/8
      * @param request 待绑定的请求信息
      */
    void addThird(UserThirdRequest request);

    /**
      * @description 解绑和第三方平台关联关系.
      * @author wjd
      * @date 2022/12/8
      * @param thirdId
      */
    void delThird(Long thirdId);

    /**
      * @description 根据自定义条件查询关联关系集.
      * @author wjd
      * @date 2022/12/8
      * @param request 查询条件
      * @return 关联关系集
      */
    List<UserThirdResponse> getThirdListByParams(UserThirdRequest request);
}
