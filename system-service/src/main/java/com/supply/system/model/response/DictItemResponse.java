package com.supply.system.model.response;

import com.supply.common.web.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wjd
 * @description 数据字典集信息响应实体.
 * @date 2022-08-29
 */
@ApiModel(value="数据字典集信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DictItemResponse extends BaseEntity {
    private static final long serialVersionUID = -9001165489057204880L;


    @ApiModelProperty(value = "字典类型ID")
    private Long dictId;

    @ApiModelProperty(value = "字典名称")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
