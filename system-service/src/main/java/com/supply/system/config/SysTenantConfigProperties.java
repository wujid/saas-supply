package com.supply.system.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Data
@Component
@ConfigurationProperties(prefix = "system.tenant")
public class SysTenantConfigProperties {

    @ApiModelProperty(value = "租户id列名")
    private String tenantIdColumn;

    @ApiModelProperty(value = "多租户忽略表表名")
    private List<String> ignoreTenantTables = new ArrayList<>();
}
