package com.supply.common.web.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.supply.common.annotation.Note;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 公共基础实体信息.
 * @date 2022-05-12
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Note(description = "ID")
    private Long id;

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
}
