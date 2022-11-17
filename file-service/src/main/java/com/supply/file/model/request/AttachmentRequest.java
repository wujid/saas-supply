package com.supply.file.model.request;

import com.supply.common.model.BaseRequestEntity;
import com.supply.file.model.po.AttachmentPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author wjd
 * @description 附件信息请求实体.
 * @date 2022-09-19
 */
@ApiModel(value="附件信息请求实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class AttachmentRequest extends BaseRequestEntity<AttachmentPo> implements Serializable {
    private static final long serialVersionUID = -7647307021669743495L;


    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "附件名称")
    private String name;

    @ApiModelProperty(value = "附件所属组")
    private String pathGroup;

    @ApiModelProperty(value = "附件地址")
    private String path;

    @ApiModelProperty(value = "附件拓展名")
    private String extensionName;

    @ApiModelProperty(value = "附件大小")
    private Long size;

    @ApiModelProperty(value = "业务ID")
    private Long businessId;

    @ApiModelProperty(value = "业务状态")
    private Integer businessStatus;

    @ApiModelProperty(value = "ID集")
    private Set<Long> ids;
}
