package com.supply.common.model.request.file;

import com.supply.common.annotation.Note;
import com.supply.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-09-22
 */
@Note(description="附件信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class FileAttachmentRequest extends BaseEntity implements Serializable {

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

    @Note(description = "ID集")
    private Set<Long> ids;

}
