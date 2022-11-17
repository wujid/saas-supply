package com.supply.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.system.model.po.OrgPo;
import com.supply.system.model.request.OrgRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-08-09
 */
public interface IOrgRepository extends IService<OrgPo> {

    /**
     * @description 根据自定义条件修改.
     * @author wjd
     * @date 2022/8/5
     * @param roleResourcePo 修改实体
     * @param request 条件值
     * @return int 受影响行数
     */
    int updateByParams(OrgPo roleResourcePo, OrgRequest request);

    /**
     * @author wjd
     * @description 根据条件查询唯一的组织信息.
     * @date 2022/7/13
     * @param request 待查询的条件
     * @return 组织信息
     **/
    OrgPo getByParams(OrgRequest request);

    /**
     * @description 根据自定义条件查询信息集.
     * @author wjd
     * @date 2022/8/3
     * @param request 查询条件
     * @return 组织信息集
     */
    List<OrgPo> getListByParams(OrgRequest request);

    /**
      * @description 根据主键ID集查询映射信息.
      * @author wjd
      * @date 2022/11/7
      * @param ids 主键id集
      * @return 主键ID > 组织信息
      */
    Map<Long, OrgPo> getMapByIds(Set<Long> ids);
}
