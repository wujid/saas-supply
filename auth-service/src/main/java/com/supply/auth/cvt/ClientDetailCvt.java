package com.supply.auth.cvt;

import com.supply.auth.model.request.ClientDetailRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * @author wjd
 * @description
 * @date 2022-07-27
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientDetailCvt {
    ClientDetailCvt INSTANCE = Mappers.getMapper(ClientDetailCvt.class);


    @Mappings({
            @Mapping(target = "registeredRedirectUri", expression = "java(com.supply.auth.util.AuthUtil.addToSet(request.getRedirectUri()))"),
            @Mapping(target = "autoApproveScopes", expression = "java(com.supply.auth.util.AuthUtil.booleanToSet(request.getAutoApprove()))")
    })
    BaseClientDetails requestToPo(ClientDetailRequest request);

}
