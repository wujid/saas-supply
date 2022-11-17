package com.supply.common.web.util;

import cn.hutool.core.util.StrUtil;
import com.supply.common.exception.ApiException;
import com.supply.common.model.response.sys.SysDataScopeRangeResponse;
import com.supply.common.model.response.sys.SysDataScopeTypeResponse;
import com.supply.common.model.response.sys.SysResourceResponse;
import com.supply.common.model.response.sys.SysUserResponse;
import com.supply.common.util.SpringContextUtil;
import com.supply.common.util.SystemUserUtil;
import com.supply.common.web.constant.DataScopeRangeEnum;
import com.supply.common.web.constant.DataScopeTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-09-29
 */
public class DataScopeUtil {
    private static final Logger logger = LoggerFactory.getLogger(DataScopeUtil.class);

    /**
      * @description 获取资源权限sql.
      * @author wjd
      * @date 2022/9/29
      * @param resourceCode 资源编码
      * @param orgAlias 组织机构列别名
      * @param deptAlias 部门列别名
      * @param userAlias 用户列别名
      * @return 资源权限sql
      */
    public static String getDataScopeSql(String resourceCode, String orgAlias, String deptAlias, String userAlias) {
        // 获取sql
        String sql = dataScopeFilter(resourceCode, orgAlias, deptAlias, userAlias);
        if (StrUtil.isNotBlank(sql)) {
            sql = " (" + sql.substring(4) + ")";
        }
        logger.info("资源{}权限sql--{}", resourceCode, sql);
        return sql;
    }

    /**
     * @description 数据权限过滤.
     * @author wjd
     * @date 2022/9/5
     * @param resourceCode 资源编码
     * @param orgAlias 组织机构列别名
     * @param deptAlias 部门列别名
     * @param userAlias 用户列别名
     */
    public static String dataScopeFilter(String resourceCode, String orgAlias, String deptAlias, String userAlias) {
        final SysUserResponse user = ContextUtil.getCurrentUser();
        // 如果是管理员则不做任何处理
        if (user.getIsManage()) {
            return null;
        }
        final SystemUserUtil systemUserUtil = SpringContextUtil.getBean(SystemUserUtil.class);
        final SysResourceResponse resource = systemUserUtil.getResourceByCode(resourceCode);
        if (null == resource) {
            final String message = StrUtil.format("根据资源编码{}未查询资源信息!", resourceCode);
            logger.error(message);
            throw new ApiException(message);
        }
        final Long resourceId = resource.getId();
        final SysDataScopeTypeResponse dataScopeResponse = systemUserUtil.getDataScope(user.getId(), resourceId);
        StringBuilder sqlString = new StringBuilder();
        // 未配置则默认为个人权限
        if (null == dataScopeResponse) {
            sqlString.append(StrUtil.format(" OR {} = {} ", userAlias, user.getId()));
            return sqlString.toString();
        }
        // 所有权限
        if (dataScopeResponse.getDataScopeType() == DataScopeTypeEnum.DATA_SCOPE_ALL.getType()) {
            sqlString = new StringBuilder();
            return sqlString.toString();
        }
        // 自定义数据权限
        if (dataScopeResponse.getDataScopeType() == DataScopeTypeEnum.DATA_SCOPE_CUSTOM.getType()) {
            commonDataScope(dataScopeResponse, sqlString, orgAlias, deptAlias, userAlias);
        }
        // 组织机构数据权限
        if (dataScopeResponse.getDataScopeType() == DataScopeTypeEnum.DATA_SCOPE_ORG.getType()) {
            commonDataScope(dataScopeResponse, sqlString, orgAlias, deptAlias, userAlias);
        }
        // 角色数据权限
        if (dataScopeResponse.getDataScopeType() == DataScopeTypeEnum.DATA_SCOPE_ROLE.getType()) {
            commonDataScope(dataScopeResponse, sqlString, orgAlias, deptAlias, userAlias);
        }
        // 用户数据权限
        if (dataScopeResponse.getDataScopeType() == DataScopeTypeEnum.DATA_SCOPE_USER.getType()) {
            final List<SysDataScopeRangeResponse> dataScopeRangeList = dataScopeResponse.getDataScopeRangeList();
            final List<Long> dataScopeIdList = dataScopeRangeList.stream().map(SysDataScopeRangeResponse::getDataScopeId).collect(Collectors.toList());
            final String joins = StrUtil.join(",", dataScopeIdList);
            sqlString.append(StrUtil.format(" OR {} IN () ", deptAlias, joins));
        }
        // 个人数据权限
        if (dataScopeResponse.getDataScopeType() == DataScopeTypeEnum.DATA_SCOPE_USER.getType()) {
            sqlString.append(StrUtil.format(" OR {} = {} ", userAlias, user.getId()));
        }
        return sqlString.toString();
    }

    /**
      * @description 公共数据权限拼接.
      * @author wjd
      * @date 2022/9/29
      * @param dataScopeResponse 数据权限信息
      * @param sqlString 拼接sql
      * @param orgAlias 组织机构列别名
      * @param deptAlias 部门列别名
      * @param userAlias 用户列别名
      */
    private static void commonDataScope(SysDataScopeTypeResponse dataScopeResponse, StringBuilder sqlString, String orgAlias, String deptAlias, String userAlias) {
        final List<SysDataScopeRangeResponse> dataScopeRangeList = dataScopeResponse.getDataScopeRangeList();
        // 根据数据权限范围进行分组
        final Map<Integer, List<SysDataScopeRangeResponse>> scopeRangeGroup = dataScopeRangeList.stream().collect(Collectors.groupingBy(SysDataScopeRangeResponse::getDataScopeRange));
        for (Integer dataScopeRange : scopeRangeGroup.keySet()) {
            // 获取数据权限ID集并转换成字符串
            final List<SysDataScopeRangeResponse> customerScopeList = scopeRangeGroup.get(dataScopeRange);
            final Set<Long> dataScopeIds = customerScopeList.stream().map(SysDataScopeRangeResponse::getDataScopeId).collect(Collectors.toSet());
            final String joins = StrUtil.join(",", dataScopeIds);
            // 公司
            if (dataScopeRange == DataScopeRangeEnum.DATA_SCOPE_RANGE_ORG.getType()) {
                sqlString.append(StrUtil.format(" OR {} IN ({}) ", orgAlias, joins));
            }
            // 部门
            if (dataScopeRange == DataScopeRangeEnum.DATA_SCOPE_RANGE_DEPT.getType()) {
                sqlString.append(StrUtil.format(" OR {} IN ({}) ", deptAlias, joins));
            }
            // 用户
            if (dataScopeRange == DataScopeRangeEnum.DATA_SCOPE_RANGE_USER.getType()) {
                sqlString.append(StrUtil.format(" OR {} IN ({}) ", userAlias, joins));
            }
            // 角色
            if (dataScopeRange == DataScopeRangeEnum.DATA_SCOPE_RANGE_ROLE.getType()) {
                final SystemUserUtil systemUserUtil = SpringContextUtil.getBean(SystemUserUtil.class);
                final Set<Long> userIds = systemUserUtil.getUserIdsByRoleIds(dataScopeIds);
                final String userIdString = StrUtil.join(",", userIds);
                sqlString.append(StrUtil.format(" OR {} IN ({}) ", userAlias, userIdString));
            }
        }
    }
}
