package com.supply.common.web.model;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author wjd
 * @description 公共基础请求实体信息.
 * @date 2022-07-27
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class BaseRequestEntity<T> extends BpmRequestEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页")
    private int pageIndex = 1;

    @ApiModelProperty(value = "每页条数")
    private int pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private SFunction<T, ?> orderColumn;

    @ApiModelProperty(value = "排序字段集")
    private List<SFunction<T, ?>> orderColumnList;

    @ApiModelProperty(value = "是否递增")
    private Boolean isAsc = false;

    @ApiModelProperty(value = "自定义权限sql")
    private String authSql;

    @ApiModelProperty(value = "自定义sql")
    private String applySql;

    @ApiModelProperty(value = "附件ID")
    private Long attachmentId;

    @ApiModelProperty(value = "附件ID集")
    private Set<Long> attachmentIdList;
}
