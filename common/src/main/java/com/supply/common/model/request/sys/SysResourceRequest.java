package com.supply.common.model.request.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.supply.common.annotation.Note;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 资源信息请求实体.
 * @date 2022-07-27
 */
@Note(description="资源信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class SysResourceRequest implements Serializable {
    private static final long serialVersionUID = -4068719063954705701L;

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

    @Note(description = "创建人")
    private Long createUserId;

    @Note(description = "创建时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Note(description = "修改人")
    private Long updateUserId;

    @Note(description = "修改时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Note(description = "状态")
    private Integer status;

    @Note(description = "是否固定")
    private Boolean isFix;

    @Note(description = "不等于ID")
    private Long neId;
}
