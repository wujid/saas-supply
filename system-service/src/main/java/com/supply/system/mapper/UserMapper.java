package com.supply.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supply.system.model.po.UserPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjd
 * @description
 * @date 2022-07-07
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPo> {

}
