package com.supply.common.model.response.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.common.annotation.Note;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 系统角色信息.
 * @date 2023-06-21
 */
@Note(description="系统角色信息")
@Data
@EqualsAndHashCode(callSuper=false)
public class SysRoleResponse implements Serializable {
    private static final long serialVersionUID = 287616195226575988L;

    @Note(description = "主键ID")
    private Long id;

    @Note(description = "角色编码")
    private String code;

    @Note(description = "角色名称")
    private String name;

    @Note(description = "角色描述")
    private String description;

    @Note(description = "业务状态")
    private Integer businessStatus;

    @Note(description = "是否系统管理角色")
    private Boolean isSystem;

    @Note(description = "是否主要角色")
    private Boolean isMain;

    @Note(description = "创建人")
    private Long createUserId;

    @Note(description = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @Note(description = "修改人")
    private Long updateUserId;

    @Note(description = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

    @Note(description = "状态")
    private Integer status;

    @Note(description = "租户ID")
    private Long tenantId;

}
