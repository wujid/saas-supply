package com.supply.common.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 业务状态--每个模块准备199个序号.
 * @date 2022-07-28
 */
@Getter
@AllArgsConstructor
public enum BusinessStatusEnum {

    IN_ACTIVE(1, "激活", ModuleEnum.COMMON),

    IN_FREEZE(2, "冻结", ModuleEnum.COMMON),

    IN_EXPIRE(3, "过期", ModuleEnum.COMMON),

    WAIT_RELATION(200, "待关联", ModuleEnum.FILE),

    RELATION(201, "已关联", ModuleEnum.FILE),

    UN_READER(400, "未读", ModuleEnum.MESSAGE),

    READER(401, "已读", ModuleEnum.MESSAGE);


    @Note(description = "业务状态")
    private int status;

    @Note(description = "业务名称")
    private String name;

    @Note(description = "所属模块")
    private ModuleEnum moduleEnum;


}
