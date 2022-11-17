package com.supply.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supply.system.model.po.TenantPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
@Mapper
public interface TenantMapper extends BaseMapper<TenantPo> {
}
