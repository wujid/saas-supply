package com.supply.file.constant;

import com.supply.common.annotation.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wjd
 * @description 文件拓展名枚举.
 * @date 2022-09-22
 */
@Getter
@AllArgsConstructor
public enum FileExtensionEnum {

    jpg("image"),

    jpeg("image"),

    png("image"),

    gif("image"),

    text("text"),

    mp4("media"),

    doc("document"),

    docx("document"),

    xlsx("excel");

    @Note(description = "文件类型")
    private String type;
}
