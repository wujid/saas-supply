package com.supply.common.model.response.sys;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.common.annotation.Note;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wjd
 * @description 用户资源数据权限类型信息响应实体.
 * @date 2022-09-05
 */
@Note(description="用户资源数据权限类型信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class SysDataScopeTypeResponse implements Serializable {
    private static final long serialVersionUID = 3708568033242296368L;

    @Note(description = "ID")
    private Long id;

    @Note(description = "用户ID")
    private Long userId;

    @Note(description = "资源ID")
    private Long resourceId;

    @Note(description = "数据权限类型")
    private Integer dataScopeType;

    @Note(description = "租户ID")
    private Long tenantId;

    @Note(description = "创建人")
    private Long createUserId;

    @Note(description = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Note(description = "修改人")
    private Long updateUserId;

    @Note(description = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Note(description = "状态")
    private Integer status;

    @Note(description = "用户资源数据权限范围集")
    List<SysDataScopeRangeResponse> dataScopeRangeList;

}
