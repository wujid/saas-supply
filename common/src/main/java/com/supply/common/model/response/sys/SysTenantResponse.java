package com.supply.common.model.response.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.supply.common.annotation.Note;
import com.supply.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjd
 * @description 租户信息响应实体.
 * @date 2022-07-08
 */
@Note(description="租户信息响应实体")
@Data
@EqualsAndHashCode(callSuper=false)
public class SysTenantResponse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8044763719006926288L;

    @Note(description = "租户编号")
    private String code;

    @Note(description = "租户名称")
    private String name;

    @Note(description = "租户类型")
    private Integer type;

    @Note(description = "授权开始时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Note(description = "授权天数")
    private Integer days;

    @Note(description = "授权结束时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Note(description = "客户端ID")
    private String clientId;

    @Note(description = "业务状态")
    private Integer businessStatus;
}
