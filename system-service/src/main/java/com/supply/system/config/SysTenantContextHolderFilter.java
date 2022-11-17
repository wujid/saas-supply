//package com.supply.system.config;
//
//import cn.hutool.core.util.StrUtil;
//import com.supply.system.constant.SystemConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//@Slf4j
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class SysTenantContextHolderFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String tenantId = request.getHeader(SystemConstant.TENANT_KEY);
//        // 在没有提供tenantId的情况下返回默认的
//        if (StrUtil.isNotBlank(tenantId)) {
//            SysTenantContextHolder.setCurrentTenantId(Long.valueOf((tenantId)));
//        } else {
//            SysTenantContextHolder.setCurrentTenantId(0L);
//        }
//        filterChain.doFilter(request, response);
//    }
//}
