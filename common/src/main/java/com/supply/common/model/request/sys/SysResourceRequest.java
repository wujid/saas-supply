package com.supply.common.model.request.sys;

import com.supply.common.annotation.Note;
import com.supply.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wjd
 * @description 资源信息请求实体.
 * @date 2022-07-27
 */
@Note(description="资源信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class SysResourceRequest extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -4068719063954705701L;


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

    @Note(description = "不等于ID")
    private Long neId;
}
