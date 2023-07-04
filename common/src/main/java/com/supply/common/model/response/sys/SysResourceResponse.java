package com.supply.common.model.response.sys;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.supply.common.annotation.Note;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wjd
 * @description 资源信息响应实体.
 * @date 2022-07-27
 */
@Note(description="资源信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class SysResourceResponse implements Serializable {
    private static final long serialVersionUID = -906481445075060108L;

    @Note(description = "ID")
    private Long id;

    @Note(description = "父ID")
    private Long parentId;

    @Note(description = "资源编码")
    private String code;

    @Note(description = "名称")
    private String name;

    @Note(description = "图标")
    private String icon;

    @Note(description = "路由")
    private String path;

    @Note(description = "排序")
    private Integer sort;

    @Note(description = "资源类型 1:菜单 2:按钮")
    private Integer type;

    @Note(description = "业务状态")
    private Integer businessStatus;

    @Note(description = "是否固定")
    private Boolean isFix;

    @Note(description = "是否选中")
    private Boolean isChecked;

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

    @Note(description = "子菜单信息")
    private List<SysResourceResponse> childrenList = new ArrayList<>();

    @Note(description = "该菜单下的按钮信息集")
    private List<SysResourceResponse> buttonResponseList = new ArrayList<>();
}
