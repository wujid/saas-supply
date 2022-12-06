package com.supply.common.model.request.file;

import com.alibaba.fastjson.annotation.JSONField;
import com.supply.common.annotation.Note;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-09-22
 */
@Note(description="附件信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class FileAttachmentRequest implements Serializable {

    @Note(description = "主键ID")
    private Long id;

    @Note(description = "附件名称")
    private String name;

    @Note(description = "附件所属组")
    private String pathGroup;

    @Note(description = "附件地址")
    private String path;

    @Note(description = "附件拓展名")
    private String extensionName;

    @Note(description = "附件大小")
    private Long size;

    @Note(description = "业务ID")
    private Long businessId;

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

    @Note(description = "ID集")
    private Set<Long> ids;

}
