package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.UserThirdPo;
import com.supply.system.model.request.UserThirdRequest;

import java.util.List;

/**
 * @author wjd
 * @description .
 * @date 2022-12-08
 */
public interface IUserThirdRepository extends IService<UserThirdPo> {

    /**
     * @author wjd
     * @description 根据条件查询唯一信息.
     * @date 2022/12/8
     * @param request 待查询的条件
     * @return 用户和第三方平台关联关系信息
     **/
    UserThirdPo getByParams(UserThirdRequest request);

    /**
     * @author wjd
     * @description 根据条件查询信息集.
     * @date 2022/12/8
     * @param request 待查询的条件
     * @return 用户和第三方平台关联关系信息集
     **/
    List<UserThirdPo> getListByParams(UserThirdRequest request);
}
