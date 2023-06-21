package com.supply.common.web.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.supply.common.constant.Constant;
import com.supply.common.constant.OperatorTypeEnum;
import com.supply.common.web.annotation.BaseData;
import com.supply.common.web.annotation.IgnoreFill;
import com.supply.common.web.util.ContextUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description 填补基础数据规则
 * 当参数上存在注解@IgnoreFill时则忽略填充
 * 新增时填补创建人&创建时间&状态
 * 新增修改时填补创建人&创建时间&修改人&修改时间&状态
 * 修改时填补修改人&修改时间
 * @Author wjd
 * @Date 2021/12/25
 */
@Aspect
@Component
public class BaseDataAspect {
    private static final Logger logger = LoggerFactory.getLogger(BaseDataAspect.class);

    @Pointcut("@annotation(com.supply.common.web.annotation.BaseData)")
    public void pointcut() {
        logger.info("开启jdbc数据填补切面");
    }


    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        BaseData baseData = ( (MethodSignature)joinPoint.getSignature() ).getMethod().getAnnotation(BaseData.class);
        final OperatorTypeEnum operatorType = baseData.fill();

        final Object[] params = joinPoint.getArgs();
        final Annotation[][] parameterAnnotations = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            // 判断是否存在需要忽略的注解,存在则跳过
            Annotation[] annotations = parameterAnnotations[i];
            final boolean isIgnore = this.isIgnore(annotations);
            if (isIgnore){
                continue;
            }
            // 自动填充属性
            final Object param = params[i];
            if (param instanceof Collection) {
                for (Object item : (List)param) {
                    this.setFile(item, operatorType);
                }
            } else {
                this.setFile(param, operatorType);
            }
        }
        return joinPoint.proceed(params);
    }

    /**
      * @description 判断该参数是否需要忽略被自动填充:当存在注解@IgnoreFill时则忽略
      * @author wjd
      * @date 2022/7/31
      * @param annotations 参数上的注解
      * @return boolean 是否需要被忽略
      */
    private boolean isIgnore(Annotation[] annotations) {
        boolean isTrue = false;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(IgnoreFill.class)) {
                isTrue = true;
                break;
            }
        }
        return isTrue;
    }

    private void setFile(Object obj, OperatorTypeEnum operatorType) {
        if (null == obj) {
            return;
        }
        // 当前类
        final Field[] fields = obj.getClass().getDeclaredFields();
        // 当前用户ID
        final String userId = this.getCurrentUserId();
        final Long tenantId = ContextUtil.getCurrentTenantId();
        for (Field field : fields) {
            final Object fieldValue = this.getFieldValue(obj, field);
            // 当操作类型为新增/新增修改时为创建时间&创建人赋值
            if (operatorType.equals(OperatorTypeEnum.INSERT) || operatorType.equals(OperatorTypeEnum.INSERT_UPDATE)) {
                // 创建人
                final boolean createUserIdIsBlank = StrUtil.equals(field.getName(), "createUserId") && null == fieldValue;
                if (StrUtil.isNotBlank(userId)) {
                    // 为空并且类型为String
                    if (createUserIdIsBlank && field.getType().equals(String.class)) {
                        setFieldValue(obj, field, userId);
                    }
                    // 为空并且类型为Integer&int
                    if (createUserIdIsBlank && (field.getType().equals(Integer.class) || field.getType().equals(int.class))) {
                        if (StrUtil.isNotBlank(userId)) {
                            setFieldValue(obj, field, Integer.parseInt(userId));
                        }
                    }
                    // 为空并且类型为Long&long
                    if (createUserIdIsBlank && (field.getType().equals(Long.class) || field.getType().equals(long.class))) {
                        if (StrUtil.isNotBlank(userId)) {
                            setFieldValue(obj, field, Long.valueOf(userId));
                        }
                    }
                }

                // 创建时间
                final boolean createTimeIsBlank = StrUtil.equals(field.getName(), "createTime")
                        && field.getType().equals(Date.class) && null == fieldValue;
                if (createTimeIsBlank) {
                    setFieldValue(obj, field, DateUtil.date());
                }

                // 状态
                final boolean statusIsBlank = StrUtil.equals(field.getName(), "status")
                        && field.getType().equals(Integer.class) && null == fieldValue;
                if (statusIsBlank) {
                    setFieldValue(obj, field, Constant.STATUS_NOT_DEL);
                }

                // 租户
                if (null != tenantId) {
                    final boolean tenantIdIsBlank = StrUtil.equals(field.getName(), "tenantId")
                            && field.getType().equals(Long.class) && null == fieldValue;
                    if (tenantIdIsBlank) {
                        setFieldValue(obj, field, tenantId);
                    }
                }
            }
            // 当操作类型为修改/新增修改时为修改时间&修改人赋值
            if (operatorType.equals(OperatorTypeEnum.UPDATE) || operatorType.equals(OperatorTypeEnum.INSERT_UPDATE)) {
                // 修改人
                final boolean updateUserIdIsBlank = StrUtil.equals(field.getName(), "updateUserId") && null == fieldValue;
                if (StrUtil.isNotBlank(userId)) {
                    // 为空并且类型为String
                    if (updateUserIdIsBlank && field.getType().equals(String.class)) {
                        setFieldValue(obj, field, userId);
                    }
                    // 为空并且类型为Integer&int
                    if (updateUserIdIsBlank && (field.getType().equals(Integer.class) || field.getType().equals(int.class))) {
                        if (StrUtil.isNotBlank(userId)) {
                            setFieldValue(obj, field, Integer.parseInt(userId));
                        }
                    }
                    // 为空并且类型为Integer&int
                    if (updateUserIdIsBlank && (field.getType().equals(Long.class) || field.getType().equals(long.class))) {
                        if (StrUtil.isNotBlank(userId)) {
                            setFieldValue(obj, field, Long.valueOf(userId));
                        }
                    }
                }
                // 修改时间
                final boolean updateTimeIsBlank = StrUtil.equals(field.getName(), "updateTime")
                        && field.getType().equals(Date.class) && null == fieldValue;
                if (updateTimeIsBlank) {
                    setFieldValue(obj, field, DateUtil.date());
                }
            }
        }
    }

    private void setFieldValue(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
            final char[] tmp = field.getName().toCharArray();
            tmp[0] = Character.toUpperCase(tmp[0]);
            String setter = "set" + new String(tmp);
            obj.getClass().getMethod(setter).invoke(obj, value).toString();
        } catch (Exception e) {
            final String message = "执行参数切面赋值";
            logger.info(message);
        }
    }

    private Object getFieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            final String message = "参数切面值获取失败";
            logger.error(message, e);
            throw new RuntimeException(e);
        }
    }

    private String getCurrentUserId() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
        final String userId = request.getHeader("userId");
        if (StrUtil.isNotBlank(userId)) {
            return userId;
        }
        return null;
    }
}
