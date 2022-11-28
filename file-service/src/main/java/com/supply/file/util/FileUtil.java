package com.supply.file.util;

import cn.hutool.core.util.StrUtil;
import com.supply.file.constant.FileExtensionEnum;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author wjd
 * @description
 * @date 2022-09-22
 */
public class FileUtil {

    /**
      * @description 根据不同的文件类型进行不同的操作.
      * @author wjd
      * @date 2022/9/22
      * @param fileName 文件名
      * @param fileExtName 文件拓展名
      * @param isDownload 是否下载
      * @param response 响应
      */
    public static void fileOperator(String fileName, String fileExtName, boolean isDownload, HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        // 非指定下载的图片类型的显示在浏览器上
        if (!isDownload && StrUtil.equals(FileExtensionEnum.valueOf(fileExtName).getType(), "image")) {
            response.setHeader("Content-disposition", "inline; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            return;
        }
        // 其它类型的均下载
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
}
