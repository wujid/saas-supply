package com.supply.common.util;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;

/**
 * @author wjd
 * @description
 * @date 2022-11-11
 */
public class EncryptionUtil {

    // key:DES模式下,key必须为8位
    private static final String key = "12345678";

    // iv:偏移量,ECB模式不需要,CBC模式下必须为8位
    private static final String iv = "12345678";

    /**
      * @description DES密码加密.
      * @author wjd
      * @date 2022/11/11
      * @param password 待加密的密码
      * @return 加密后的密文
      */
    public static String encryptionPassword(String password) {
        // DES des = new DES(Mode.ECB, Padding.PKCS5Padding, key.getBytes());
        DES des = new DES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        String encrypt = des.encryptBase64(password);
        return encrypt;
    }

    /**
      * @description DES密码解密
      * @author wjd
      * @date 2022/11/11
      * @param encryptKey 加密后的key
      * @return 解密后的明文密码
      */
    public static String decryptPassword(String encryptKey) {
        DES des = new DES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        String decrypt = des.decryptStr(encryptKey);
        return decrypt;
    }

    public static void main(String[] args) {
        String password = "123456";
        final String encryptionPassword = encryptionPassword(password);
        System.out.println("=====加密后的密文：" + encryptionPassword);
        final String realPassword = decryptPassword(encryptionPassword);
        System.out.println("=====解密后的密码：" + realPassword);
    }

}
