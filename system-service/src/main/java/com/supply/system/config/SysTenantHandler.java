//package com.supply.system.config;
//
//import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.LongValue;
//import net.sf.jsqlparser.expression.NullValue;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class SysTenantHandler implements TenantLineHandler {
//    private static final Logger logger = LoggerFactory.getLogger(SysTenantHandler.class);
//
//    private final SysTenantConfigProperties tenantConfigProperties;
//
//    public SysTenantHandler(SysTenantConfigProperties tenantConfigProperties) {
//        this.tenantConfigProperties = tenantConfigProperties;
//    }
//
//
//    /**
//      * @description 获取租户ID.
//      * @author wjd
//      * @date 2022/7/8
//      * @return 租户ID
//      */
//    @Override
//    public Expression getTenantId() {
//        Long tenantId = SysTenantContextHolder.getCurrentTenantId();
//        logger.debug("当前租户为{}", tenantId);
//        if (tenantId == null || tenantId == 0L) {
//            return new NullValue();
//        }
//        return new LongValue(tenantId);
//    }
//
//    /**
//      * @description 获取租户字段名.
//      * @author wjd
//      * @date 2022/7/8
//      * @return 租户字段名
//      */
//    @Override
//    public String getTenantIdColumn() {
//        return tenantConfigProperties.getTenantIdColumn();
//    }
//
//    /**
//     * @description 需要忽略的表.
//     * @author wjd
//     * @date 2022/7/8
//     * @param tableName 当前操作的表.
//     * @return boolean 是否忽略
//     */
//    @Override
//    public boolean ignoreTable(String tableName) {
//        return tenantConfigProperties.getIgnoreTenantTables().stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
//    }
//}
