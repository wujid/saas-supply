package com.supply.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.system.model.request.DictRequest;
import com.supply.system.model.response.DictResponse;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
public interface IDictService {

    /**
     * @description 新增字典.
     * @author wjd
     * @date 2022/8/29
     * @param request 实体信息
     */
    void addDict(DictRequest request);

    /**
     * @description 修改字典.
     * @author wjd
     * @date 2022/8/29
     * @param request 实体信息
     */
    void updateDict(DictRequest request);

    /**
      * @description 删除字典.
      * @author wjd
      * @date 2022/8/29
      * @param dictId 待删除的业务字典ID
      */
    void delDict(Long dictId);

    /**
      * @description 根据自定义条件查询带分页的字典信息集.
      * @author wjd
      * @date 2022/8/29
      * @param request 查询条件
      * @return 带分页的字典信息集
      */
    IPage<DictResponse> getPageByParams(DictRequest request);
}
