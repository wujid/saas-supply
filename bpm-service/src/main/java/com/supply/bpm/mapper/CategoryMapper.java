package com.supply.bpm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supply.bpm.model.po.CategoryPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjd
 * @description .
 * @date 2022-12-20
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryPo> {
}
