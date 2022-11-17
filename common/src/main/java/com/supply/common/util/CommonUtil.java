package com.supply.common.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wjd
 * @description
 * @date 2022-07-31
 */
public class CommonUtil {

    /**
      * @description 将一种类型的page转换成另一种类型.
      * @author wjd
      * @date 2022/9/8
      * @param dataList 分页数据信息集
      * @param page 待转换的分页
      * @return 转换泛型后的分页
      */
    public static <T> Page<T> pageCvt(List<T> dataList, Page page) {
        Page<T> resultPage = new Page<>();
        resultPage.setRecords(dataList);
        resultPage.setSize(page.getSize());
        resultPage.setTotal(page.getTotal());
        resultPage.setCurrent(page.getCurrent());
        return resultPage;
    }

    /**
     * @description 获取标题.
     * @author wjd
     * @date 2022/10/29
     * @param titleRule 标题内容规则
     * @param map 参数
     * @return 组装后的标题
     */
    public static String getContentByRule(String titleRule, Map<String, Object> map) {
        if (StrUtil.isBlank(titleRule)) {
            return null;
        }
        Pattern regex = Pattern.compile("\\#\\{(.*?)\\}", Pattern.DOTALL
                | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = regex.matcher(titleRule);
        while (matcher.find()) {
            String tag = matcher.group(0);
            String rule = matcher.group(1);
            String[] aryRule = rule.split(":");
            String name;
            if (aryRule.length == 1) {
                name = rule;
            } else {
                name = aryRule[1];
            }
            if(map.containsKey(name)){
                Object obj=map.get(name);
                titleRule = titleRule.replace(tag, obj!=null ? obj.toString() : "");
            }
            else{
                titleRule = titleRule.replace(tag, "");
            }
        }
        return titleRule;
    }
}
