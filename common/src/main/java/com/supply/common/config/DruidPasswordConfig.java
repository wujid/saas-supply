package com.supply.common.config;

import com.alibaba.druid.util.DruidPasswordCallback;
import com.supply.common.util.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author wjd
 * @description 对druid的密码进行解密.
 * @date 2022-11-11
 */
public class DruidPasswordConfig extends DruidPasswordCallback {
    private static final Logger logger = LoggerFactory.getLogger(DruidPasswordConfig.class);


    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        // 获取配置文件中的已经加密的密码
        String pwd = (String)properties.get("password");
        String password = EncryptionUtil.decryptPassword(pwd);
        logger.info("数据库解密成功");
        setPassword(password.toCharArray());
    }
}
