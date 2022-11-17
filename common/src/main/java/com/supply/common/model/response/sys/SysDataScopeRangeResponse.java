package com.supply.common.model.response.sys;

import com.supply.common.annotation.Note;
import com.supply.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 用户数据权限范围信息响应实体.
 * @date 2022-09-05
 */
@Note(description="用户数据权限范围信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class SysDataScopeRangeResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2880231603706732270L;


    @Note(description = "用户资源数据权限类型ID")
    private Long dataScopeTypeId;

    @Note(description = "数据权限范围")
    private Integer dataScopeRange;

    @Note(description = "数据权限ID")
    private Long dataScopeId;
}
