package com.supply.common.model;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.supply.common.annotation.Note;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author wjd
 * @description 公共基础请求实体信息.
 * @date 2022-07-27
 */
@Data
public class BaseRequestEntity<T> extends BaseEntity implements Serializable {

    @Note(description = "当前页")
    private int pageIndex = 1;

    @Note(description = "每页条数")
    private int pageSize = 10;

    @Note(description = "排序字段")
    private SFunction<T, ?> orderColumn;

    @Note(description = "排序字段集")
    private List<SFunction<T, ?>> orderColumnList;

    @Note(description = "是否递增")
    private Boolean isAsc = false;

    @Note(description = "自定义sql")
    private String applySql;

    @Note(description = "附件ID")
    private Long attachmentId;

    @Note(description = "附件ID集")
    private Set<Long> attachmentIdList;
}
