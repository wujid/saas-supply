package com.supply.common.handle;

import com.alibaba.druid.util.DruidPasswordCallback;
import com.supply.common.util.EncryptionUtil;

import java.util.Properties;

/**
 * @author wjd
 * @description 对druid的密码进行解密.
 * @date 2022-11-11
 */
public class DruidPasswordHandle extends DruidPasswordCallback {

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        // 获取配置文件中的已经加密的密码
        String pwd = (String)properties.get("password");
        String password = EncryptionUtil.decryptPassword(pwd);
        setPassword(password.toCharArray());
    }
}
