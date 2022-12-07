package com.supply.common.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 业务状态--每个模块准备200个序号.
 * @date 2022-07-28
 */
@Getter
@AllArgsConstructor
public enum BusinessStatusEnum {

    IN_ACTIVE(1, "激活", ModuleEnum.COMMON),

    IN_FREEZE(2, "冻结", ModuleEnum.COMMON),

    IN_EXPIRE(3, "过期", ModuleEnum.COMMON),

    WAIT_RELATION(201, "待关联", ModuleEnum.FILE),

    RELATION(202, "已关联", ModuleEnum.FILE),

    UN_READER(401, "未读", ModuleEnum.MESSAGE),

    READER(402, "已读", ModuleEnum.MESSAGE);


    @Note(description = "业务状态")
    private final int status;

    @Note(description = "业务名称")
    private final String name;

    @Note(description = "所属模块")
    private final ModuleEnum moduleEnum;


}
