package com.supply.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supply.system.model.po.RolePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePo> {

    List<RolePo> getRoleByUserId(@Param("userId") Long userId);
}
