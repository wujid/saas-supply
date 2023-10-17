package com.supply.job.system;

import com.supply.job.api.SystemClient;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wjd
 * @description
 * @date 2022-08-31
 */
@Component
public class TenantJob {
    private static final Logger logger = LoggerFactory.getLogger(TenantJob.class);

    private final SystemClient systemClient;

    public TenantJob(SystemClient systemClient) {
        this.systemClient = systemClient;
    }


    @XxlJob("expireTenantJobHandler")
    public void expireTenantJobHandler() {
        String message = "开始执行租户过期";
        XxlJobHelper.log(message);
        logger.info(message);

        try {
            systemClient.expireTenant();
        } catch (Exception e) {
            logger.error("租户过期调度执行异常", e);
            throw new RuntimeException(e);
        }

        message = "租户过期调度完成";
        XxlJobHelper.log(message);
        logger.info(message);
    }
}
