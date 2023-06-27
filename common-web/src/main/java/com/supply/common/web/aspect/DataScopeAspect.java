package com.supply.common.web.aspect;

import cn.hutool.core.util.StrUtil;
import com.supply.common.web.model.BaseRequestEntity;
import com.supply.common.web.annotation.DataScope;
import com.supply.common.web.util.DataScopeUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wjd
 * @description 用户数据权限过滤处理.
 * @date 2022-09-05
 */
@Aspect
@Component
public class DataScopeAspect {
    private static final Logger logger = LoggerFactory.getLogger(DataScopeAspect.class);


    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint point, DataScope dataScope) {
        this.clearDataScope(point);
        // 获取sql
        String sql = DataScopeUtil.dataScopeFilter(dataScope.resourceCode(), dataScope.orgAlias(), dataScope.deptAlias(), dataScope.userAlias());
        if (StrUtil.isNotBlank(sql)) {
            sql = " (" + sql.substring(4) + ")";
            Object params = point.getArgs()[0];
            if (params instanceof BaseRequestEntity) {
                BaseRequestEntity baseEntity = (BaseRequestEntity) params;
                baseEntity.setAuthSql(sql);
            }
        }
        logger.info("资源{}权限sql--{}", dataScope.resourceCode(), sql);
    }

    /**
     * @description 清除数据权限.
     * @author wjd
     * @date 2022/9/5
     * @param joinPoint 切点
     */
    private void clearDataScope(JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (params instanceof BaseRequestEntity) {
            BaseRequestEntity baseEntity = (BaseRequestEntity) params;
            baseEntity.setAuthSql(null);
        }
    }

}
