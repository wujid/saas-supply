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

    WAIT_RELATION(301, "待关联", ModuleEnum.FILE),

    RELATION(302, "已关联", ModuleEnum.FILE),

    UN_READER(501, "未读", ModuleEnum.MESSAGE),

    READER(502, "已读", ModuleEnum.MESSAGE),

    UN_DEPLOY(701, "未部署", ModuleEnum.BPM),

    DEPLOYED(702, "已部署", ModuleEnum.BPM),

    PROCESS_STATUS_ACTIVE(703, "流程激活", ModuleEnum.BPM),

    PROCESS_STATUS_SUSPEND(704, "流程挂起", ModuleEnum.BPM);




    @Note(description = "业务状态")
    private final int status;

    @Note(description = "业务名称")
    private final String name;

    @Note(description = "所属模块")
    private final ModuleEnum moduleEnum;


}
