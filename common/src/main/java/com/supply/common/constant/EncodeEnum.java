package com.supply.common.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 字符编码枚举.
 * @date 2022-09-02
 */
@Getter
@AllArgsConstructor
public enum EncodeEnum {
    UTF8("UTF-8");

    @Note(description = "编码类型")
    private String type;
}
