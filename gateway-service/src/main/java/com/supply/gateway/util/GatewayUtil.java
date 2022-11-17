package com.supply.gateway.util;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-07-14
 */
public class GatewayUtil {

    /**
      * @description 查找指定字符串是否匹配指定字符串列表中的任意一个字符串.
      * @author wjd
      * @date 2022/7/14
      * @param str 指定字符串
      * @param stringList 需要检查的字符串数组
      * @return boolean 是否匹配
      */
    public static boolean matches(String str, List<String> stringList) {
        if (CollectionUtil.isEmpty(stringList)) {
            return false;
        }
        for (String pattern : stringList) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
      * @description 判断url是否与规则配置: ?表示单个字符; *表示一层路径内的任意字符串，不可跨层级; **表示任意层路径.
      * @author wjd
      * @date 2022/7/14
      * @param pattern 匹配规则
      * @param url 需要匹配的url
      * @return boolean 是否匹配
      */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }
}
