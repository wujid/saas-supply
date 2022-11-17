package com.supply.auth.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-07-27
 */
public class AuthUtil {

    public static Set<String> addToSet(String value) {
        Set<String> stringSet = new HashSet<>();
        if (StrUtil.isNotBlank(value)) {
            stringSet.add(value);
        }
        return stringSet;
    }

    public static Set<String> booleanToSet(Boolean value) {
        Set<String> stringSet = new HashSet<>();
        if (null == value) {
            return stringSet;
        }
        final String stringValue = value.toString();
        return addToSet(stringValue);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        final String system = bCryptPasswordEncoder.encode("123456");
        System.out.println("========" + system);
    }
}
