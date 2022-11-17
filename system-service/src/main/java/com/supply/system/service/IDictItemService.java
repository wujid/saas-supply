package com.supply.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.system.model.request.DictItemRequest;
import com.supply.system.model.response.DictItemResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
public interface IDictItemService {

    /**
     * @description 新增字典数据集.
     * @author wjd
     * @date 2022/8/29
     * @param request 实体信息
     */
    void addDictItem(DictItemRequest request);

    /**
     * @description 修改字典数据集.
     * @author wjd
     * @date 2022/8/29
     * @param request 实体信息
     */
    void updateDictItem(DictItemRequest request);

    /**
     * @description 删除字典数据集.
     * @author wjd
     * @date 2022/8/29
     * @param dictItemId 待删除的数据字典集
     */
    void delDictItem(Long dictItemId);

    /**
      * @description 根据自定义条件查询带分页的字典数据集信息集.
      * @author wjd
      * @date 2022/8/29
      * @param request 查询条件
      * @return 带分页的字典数据信息集
      */
    IPage<DictItemResponse> getPageByParams(DictItemRequest request);

    /**
      * @description 根据自定义条件查询数据字典集信息集.
      * @author wjd
      * @date 2022/8/29
      * @param request 查询条件
      * @return 字典数据信息集
      */
    List<DictItemResponse> getListByParams(DictItemRequest request);

    /**
      * @description 根据字典类型编码获取对应的数据字典集信息集.
      * @author wjd
      * @date 2022/8/29
      * @param code 字典类型编码
      * @return 数据字典集信息集
      */
    List<DictItemResponse> getListByCode(String code);

    /**
      * @description 根据字典类型编码集查询对应的数据字典集映射信息.
      * @author wjd
      * @date 2022/8/29
      * @param codes 字典类型编码集
      * @return 字典类型编码集对应的数据字典集映射信息
      */
    Map<String, List<DictItemResponse>> getMapByCodes(Set<String> codes);

    /**
      * @description 获取所有数据字典code映射字典信息集.
      * @author wjd
      * @date 2022/11/8
      * @return 字典code映射字典信息集
      */
    Map<String, List<DictItemResponse>> getAllMap();
}
