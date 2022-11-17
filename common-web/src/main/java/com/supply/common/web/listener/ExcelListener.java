package com.supply.common.web.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-09-02
 */
public class ExcelListener extends AnalysisEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ExcelListener.class);

    /**
     * 每隔5条存储数据库,实际使用中可以100条,然后清理list,方便内存回收
     */
    private static final int BATCH_COUNT = 1000;
    private List<Object> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
      * @description 这个每一条数据解析都会来调用.
      * @author wjd
      * @date 2022/9/2
      * @param object 数据
      * @param context 解析内容
      */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        logger.info("解析到一条数据:{}", JSON.toJSONString(object));
        cachedDataList.add(object);
    }

    /**
      * @description 所有数据解析完成了 都会来调用
      * @author wjd
      * @date 2022/9/2
      * @param context 解析内容
      */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        logger.info("所有数据解析完成！");
    }

    /**
     * 返回list
     */
    public List<Object> getData() {
        return this.cachedDataList;
    }

}
