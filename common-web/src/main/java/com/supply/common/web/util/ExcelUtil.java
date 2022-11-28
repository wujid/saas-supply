package com.supply.common.web.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.supply.common.exception.ApiException;
import com.supply.common.web.listener.ExcelListener;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wjd
 * @description 将数据写入EXCEL.
 * @date 2022-09-02
 */
public class ExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
      * @description 将数据写入excel.
      * @author wjd
      * @date 2022/9/2
      * @param response 响应体
      * @param fileName 文件名
      * @param data 写入的数据
      * @param sheetName sheet名称
      * @param model 指定写使用的class
      */
    public static <T> void writeExcel(HttpServletResponse response, String fileName, List<T> data,  String sheetName, Class model) {
        // 定义头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 定义内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 使用策略
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        // 写入数据
        try (OutputStream outputStream = getOutputStream(fileName, response)) {
            EasyExcel.write(outputStream, model)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet(sheetName)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .doWrite(data);
        } catch (Exception e) {
            final String message = "导出excel表格失败!";
            logger.error(message, e);
            throw new ApiException(message, e);
        }
    }

    /**
      * @description 读取Excel(多个sheet).
      * @author wjd
      * @date 2022/9/2
      * @param excel 待读取的文件流
      * @param model 转换实体
      * @return 转换的实体集
      */
    public static List<Object> readExcel(MultipartFile excel, Class model) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReaderBuilder readerBuilder = getReader(excel, excelListener);
        readerBuilder.head(model).doReadAll();
        return excelListener.getData();
    }

    /**
      * @description 读取某个sheet的Excel.
      * @author wjd
      * @date 2022/9/2
      * @param excel 待读取的文件流
      * @param model 转换实体
      * @param sheetNo 读取的sheet
      * @return 转换的实体集
      */
    public static List<Object> readExcel(MultipartFile excel, Class model, int sheetNo) {
        return readExcel(excel, model, sheetNo, 1);
    }

    /**
      * @description 读取某个sheet的Excel
      * @author wjd
      * @date 2022/9/2
      * @param excel 待读取的文件流
      * @param model 转换实体
      * @param sheetNo 读取的sheet
      * @param headLineNum 标题行号
      * @return 转换的实体集
      */
    public static List<Object> readExcel(MultipartFile excel, Class model, int sheetNo, Integer headLineNum) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReaderBuilder readerBuilder = getReader(excel, excelListener);
        ExcelReader reader = readerBuilder.headRowNumber(headLineNum).build();
        ReadSheet readSheet = EasyExcel.readSheet(sheetNo).head(model).build();
        reader.read(readSheet);
        return excelListener.getData();
    }

    /**
      * @description 文件输出.
      * @author wjd
      * @date 2022/9/2
      * @param fileName 文件名称
      * @param response 响应体
      * @return 文件输出流
      */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws IOException {
        fileName = URLEncoder.encode(fileName,  StandardCharsets.UTF_8.toString());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        return response.getOutputStream();
    }

    private static ExcelReaderBuilder getReader(MultipartFile excel, ExcelListener excelListener) {
        String filename = excel.getOriginalFilename();
        if (filename == null || (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx"))) {
            throw new ApiException("文件格式错误！");
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(excel.getInputStream());
            return EasyExcel.read(inputStream, excelListener);
        } catch (IOException e) {
            final String message = "读取excel文件异常!";
            logger.error(message, e);
            throw new ApiException(message, e);
        }
    }

}
