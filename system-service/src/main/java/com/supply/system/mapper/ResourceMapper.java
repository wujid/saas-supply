package com.supply.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.po.TenantPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
@Mapper
public interface ResourceMapper extends BaseMapper<ResourcePo> {


    List<ResourcePo> getByRoleIdList(List<Long> roleIdList, Integer type);


    List<ResourcePo> getByTenantId(Long tenantId);
}
