package com.supply.system.model.request;

import com.supply.common.web.model.BaseRequestEntity;
import com.supply.system.model.po.DictItemPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author wjd
 * @description 数据字典集信息请求实体.
 * @date 2022-08-29
 */
@ApiModel(value="字典数据信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DictItemRequest extends BaseRequestEntity<DictItemPo> {
    private static final long serialVersionUID = 1942254915491660373L;

    @ApiModelProperty(value = "字典类型ID")
    private Long dictId;

    @ApiModelProperty(value = "字典名称")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "字典类型ID集")
    private Set<Long> dictIdList;
}
