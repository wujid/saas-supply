package com.supply.system.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.supply.common.model.BaseRequestEntity;
import com.supply.system.model.po.DictPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author wjd
 * @description 字典类型信息请求实体.
 * @date 2022-08-29
 */
@ApiModel(value="字典类型信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class DictRequest extends BaseRequestEntity<DictPo> implements Serializable {
    private static final long serialVersionUID = -9094975178403318272L;


    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "字典备注")
    private String description;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "修改时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "不包含的主键ID")
    private Long neId;

    @ApiModelProperty(value = "字典编码集")
    private Set<String> codeList;
}
