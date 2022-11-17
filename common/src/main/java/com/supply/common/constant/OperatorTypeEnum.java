package com.supply.common.constant;

/**
 * @description: 字段填充策略枚举类.
 * @Author wjd
 * @date 2022/4/27
 */
public enum OperatorTypeEnum {
    /**
     * 默认不处理
     */
    DEFAULT,
    /**
     * 插入时填充字段
     */
    INSERT,
    /**
     * 更新时填充字段
     */
    UPDATE,
    /**
     * 插入和更新时填充字段
     */
    INSERT_UPDATE
}
