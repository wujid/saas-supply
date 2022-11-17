package com.supply.system.cvt;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


/**
 * @author wjd
 * @description
 * @date 2022-07-28
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleResourceCvt {
    RoleResourceCvt INSTANCE = Mappers.getMapper(RoleResourceCvt.class);
}
