package com.supply.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.util.CommonUtil;
import com.supply.system.api.AuthClient;
import com.supply.system.constant.TenantTypeEnum;
import com.supply.system.cvt.OrgCvt;
import com.supply.system.cvt.RoleCvt;
import com.supply.system.cvt.TenantCvt;
import com.supply.system.cvt.UserCvt;
import com.supply.system.model.po.OrgPo;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.po.RolePo;
import com.supply.system.model.po.RoleResourcePo;
import com.supply.system.model.po.TenantPo;
import com.supply.system.model.po.TenantResourcePo;
import com.supply.system.model.po.UserPo;
import com.supply.system.model.po.UserRolePo;
import com.supply.system.model.request.ClientDetailRequest;
import com.supply.system.model.request.OrgRequest;
import com.supply.system.model.request.RoleRequest;
import com.supply.system.model.request.RoleResourceRequest;
import com.supply.system.model.request.TenantRequest;
import com.supply.system.model.request.TenantResourceRequest;
import com.supply.system.model.request.TenantSaveRequest;
import com.supply.system.model.request.UserRequest;
import com.supply.system.model.request.UserRoleRequest;
import com.supply.system.model.response.OrgResponse;
import com.supply.system.model.response.ResourceResponse;
import com.supply.system.model.response.RoleResponse;
import com.supply.system.model.response.TenantDetailResponse;
import com.supply.system.model.response.TenantResponse;
import com.supply.system.model.response.UserResponse;
import com.supply.system.repository.IOrgRepository;
import com.supply.system.repository.IResourceRepository;
import com.supply.system.repository.IRoleRepository;
import com.supply.system.repository.IRoleResourceRepository;
import com.supply.system.repository.ITenantRepository;
import com.supply.system.repository.ITenantResourceRepository;
import com.supply.system.repository.IUserRepository;
import com.supply.system.repository.IUserRoleRepository;
import com.supply.system.service.ITenantService;
import com.supply.system.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-07-08
 */
@Service
public class TenantServiceImpl implements ITenantService {
    private static final Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final ITenantRepository tenantRepository;

    private final AuthClient authClient;

    private final IRoleRepository roleRepository;

    private final IRoleResourceRepository roleResourceRepository;

    private final IUserRepository userRepository;

    private final IUserRoleRepository userRoleRepository;

    private final ITenantResourceRepository tenantResourceRepository;

    private final IOrgRepository orgRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ResourceUtil resourceUtil;

    private final IResourceRepository resourceRepository;

    public TenantServiceImpl(ITenantRepository tenantRepository, AuthClient authClient,
                             IRoleRepository roleRepository, IRoleResourceRepository roleResourceRepository,
                             IUserRepository userRepository, IUserRoleRepository userRoleRepository,
                             ITenantResourceRepository tenantResourceRepository, IOrgRepository orgRepository,
                             BCryptPasswordEncoder passwordEncoder, ResourceUtil resourceUtil,
                             IResourceRepository resourceRepository) {
        this.tenantRepository = tenantRepository;
        this.authClient = authClient;
        this.roleRepository = roleRepository;
        this.roleResourceRepository = roleResourceRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.tenantResourceRepository = tenantResourceRepository;
        this.orgRepository = orgRepository;
        this.passwordEncoder = passwordEncoder;
        this.resourceUtil = resourceUtil;
        this.resourceRepository = resourceRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTenant(TenantSaveRequest request) {
        logger.info("[新增租户]---请求实体为{}", JSON.toJSONString(request));
        // 租户信息
        final TenantRequest tenantRequest = request.getTenant();
        // 验证租户是否存在
        this.validateTenant(tenantRequest);
        // 生成唯一客户端ID
        final String clientId = IdUtil.simpleUUID();
        // 新增权限验证客户端详情信息
        ClientDetailRequest clientDetailRequest = new ClientDetailRequest();
        clientDetailRequest.setClientId(clientId);
        // 租户唯一编码为客户端密钥
        clientDetailRequest.setClientSecret(tenantRequest.getCode());
        authClient.saveClientDetails(clientDetailRequest);
        // 保存
        final TenantPo tenantPo = TenantCvt.INSTANCE.requestToPo(tenantRequest);
        // 开始时间为当前时间
        final DateTime stareTime = DateUtil.parse(DateUtil.today());
        tenantPo.setStartTime(stareTime);
        // 计算结束时间
        final DateTime endTime = DateUtil.offsetDay(stareTime, tenantPo.getDays());
        tenantPo.setEndTime(endTime);
        tenantPo.setClientId(clientId);
        tenantPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        tenantRepository.save(tenantPo);
        final Long tenantId = tenantPo.getId();

        // 租户资源关联关系信息
        final List<Long> resourceIdList = request.getResourceIdList();
        final List<TenantResourcePo> tenantResourcePoList = new ArrayList<>();
        for (Long resourceId : resourceIdList) {
            TenantResourcePo tenantResourcePo = new TenantResourcePo();
            tenantResourcePo.setTenantId(tenantId);
            tenantResourcePo.setResourceId(resourceId);
            tenantResourcePo.setStatus(Constant.STATUS_NOT_DEL);
            tenantResourcePoList.add(tenantResourcePo);
        }
        tenantResourceRepository.saveBatch(tenantResourcePoList);

        // 角色信息
        final Long roleId = this.addRole(request.getRoleList(), tenantId);

        // 角色资源关联关系信息
        List<RoleResourcePo> roleResourcePoList = new ArrayList<>();
        for (Long resourceId : resourceIdList) {
            RoleResourcePo roleResourcePo = new RoleResourcePo();
            roleResourcePo.setRoleId(roleId);
            roleResourcePo.setResourceId(resourceId);
            roleResourcePo.setStatus(Constant.STATUS_NOT_DEL);
            roleResourcePoList.add(roleResourcePo);
        }
        roleResourceRepository.saveBatch(roleResourcePoList);

        // 组织信息
        final Integer tenantType = tenantPo.getType();
        Long orgId = null;
        if (tenantType == TenantTypeEnum.COMPANY.getType()) {
            final OrgRequest orgRequest = request.getOrg();
            final OrgPo orgPo = OrgCvt.INSTANCE.requestToPo(orgRequest);
            orgPo.setTenantId(tenantId);
            orgPo.setIsMain(true);
            orgPo.setParentId(0L);
            orgPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
            orgPo.setSort(1);
            orgRepository.save(orgPo);
            orgId = orgPo.getId();
        }

        // 管理员用户信息
        final UserRequest userRequest = request.getUser();
        final UserPo userPo = UserCvt.INSTANCE.requestToPo(userRequest);
        userPo.setIsManage(true);
        userPo.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userPo.setTenantId(tenantId);
        userPo.setOrgId(orgId);
        userPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        userRepository.save(userPo);
        final Long userId = userPo.getId();

        // 用户角色关联关系
        UserRolePo userRolePo = new UserRolePo();
        userRolePo.setUserId(userId);
        userRolePo.setRoleId(roleId);
        userRoleRepository.save(userRolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTenant(TenantSaveRequest request) {
        logger.info("[修改租户]---请求实体为{}", JSON.toJSONString(request));
        // 租户信息
        final TenantRequest tenantRequest = request.getTenant();
        final Long tenantId = tenantRequest.getId();
        final TenantPo tenantPo = TenantCvt.INSTANCE.requestToPo(tenantRequest);
        tenantRepository.updateById(tenantPo);

        // 租户资源关联关系信息
        // 删除历史关联关系
        TenantResourcePo tenantResourcePo = new TenantResourcePo();
        tenantResourcePo.setStatus(Constant.STATUS_DEL);
        TenantResourceRequest tenantResourceRequest = new TenantResourceRequest();
        tenantResourceRequest.setTenantId(tenantId);
        tenantResourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        tenantResourceRepository.updateByParams(tenantResourcePo, tenantResourceRequest);
        // 保存关联关系
        final List<Long> resourceIdList = request.getResourceIdList();
        final List<TenantResourcePo> tenantResourcePoList = new ArrayList<>();
        for (Long resourceId : resourceIdList) {
            TenantResourcePo tenantResource = new TenantResourcePo();
            tenantResource.setTenantId(tenantId);
            tenantResource.setResourceId(resourceId);
            tenantResource.setStatus(Constant.STATUS_NOT_DEL);
            tenantResourcePoList.add(tenantResource);
        }
        tenantResourceRepository.saveBatch(tenantResourcePoList);

        // 管理员角色信息
        final Long roleId = this.updateRole(request.getRoleList(), tenantId);

        // 角色资源关联关系信息
        // 删除历史关联关系
        RoleResourcePo roleResourcePo = new RoleResourcePo();
        roleResourcePo.setStatus(Constant.STATUS_DEL);
        RoleResourceRequest roleResourceRequest = new RoleResourceRequest();
        roleResourceRequest.setRoleId(roleId);
        roleResourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        roleResourceRepository.updateByParams(roleResourcePo, roleResourceRequest);
        // 保存关联关系
        List<RoleResourcePo> roleResourcePoList = new ArrayList<>();
        for (Long resourceId : resourceIdList) {
            RoleResourcePo roleResource = new RoleResourcePo();
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(resourceId);
            roleResource.setStatus(Constant.STATUS_NOT_DEL);
            roleResourcePoList.add(roleResource);
        }
        roleResourceRepository.saveBatch(roleResourcePoList);

        // 组织信息
        final Integer tenantType = tenantPo.getType();
        final OrgRequest orgRequest = request.getOrg();
        final Long orgId = orgRequest.getId();
        // 如果是个人并且组织信息存在,那么删除该组织信息
        if (tenantType == TenantTypeEnum.PERSONAL.getType() && null != orgRequest.getId()) {
            final OrgPo orgPo = OrgCvt.INSTANCE.requestToPo(orgRequest);
            orgRepository.updateById(orgPo);
        }
        // 如果是组织则判断是否存在组织ID,存在则修改否则新增
        if (tenantType == TenantTypeEnum.COMPANY.getType()) {
            final OrgPo orgPo = OrgCvt.INSTANCE.requestToPo(orgRequest);
           if (null == orgId) {
               orgPo.setTenantId(tenantId);
               orgPo.setParentId(0L);
               orgPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
               orgRepository.save(orgPo);
               // 设置管理员的组织ID
               request.getUser().setOrgId(orgPo.getId());
           } else {
               orgRepository.updateById(orgPo);
           }
        }

        // 管理员用户信息
        final UserRequest userRequest = request.getUser();
        final UserPo userPo = UserCvt.INSTANCE.requestToPo(userRequest);
        if (null != userPo.getPassword()) {
            userPo.setPassword(passwordEncoder.encode(userPo.getPassword()));
        }
        userRepository.updateById(userPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delTenant(Long tenantId) {
        logger.info("[删除租户]---其中租户ID为{}", tenantId);
        TenantPo tenantPo = new TenantPo();
        tenantPo.setId(tenantId);
        tenantPo.setStatus(Constant.STATUS_DEL);
        tenantRepository.updateById(tenantPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeTenant(Long tenantId) {
        logger.info("[冻结租户]---其中租户ID为{}", tenantId);
        TenantPo tenantPo = new TenantPo();
        tenantPo.setId(tenantId);
        tenantPo.setBusinessStatus(BusinessStatusEnum.IN_FREEZE.getStatus());
        tenantRepository.updateById(tenantPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeTenant(Long tenantId) {
        logger.info("[激活租户]---其中租户ID为{}", tenantId);
        TenantPo tenantPo = new TenantPo();
        tenantPo.setId(tenantId);
        tenantPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        tenantRepository.updateById(tenantPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void expireTenant() {
        logger.info("[租户过期]---开始执行");
        // 查询出所有正在运行的租户
        TenantRequest request = new TenantRequest();
        request.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<TenantPo> tenantPoList = tenantRepository.getListByParams(request);
        if (CollectionUtil.isEmpty(tenantPoList)) {
            logger.info("当前不存在正在运行的租户,本次调度结束");
            return;
        }
        List<TenantPo> updateTenantList = new ArrayList<>();
        for (TenantPo tenantPo : tenantPoList) {
            final Long id = tenantPo.getId();
            final DateTime date = DateUtil.date();
            final Date startTime = tenantPo.getStartTime();
            final Integer days = tenantPo.getDays();
            final DateTime endTime = DateUtil.offsetDay(startTime, days);
            final int compare = DateUtil.compare(endTime, date);
            if (compare <= 0) {
                TenantPo tenant = new TenantPo();
                tenant.setId(id);
                tenant.setBusinessStatus(BusinessStatusEnum.IN_EXPIRE.getStatus());
                updateTenantList.add(tenant);
            }
        }
        tenantRepository.updateBatchById(updateTenantList);
        logger.info("[租户过期]---执行完成");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void renewalDays(Long tenantId, Integer days) {
        logger.info("[租户续期]---租户{}续期{}天", tenantId, days);
        final TenantPo tenantPo = tenantRepository.getById(tenantId);
        TenantPo updateTenant = new TenantPo();
        // 如果已到期则覆盖开始时间为当前时间,天数为当前续费天数
        if (tenantPo.getBusinessStatus() == BusinessStatusEnum.IN_EXPIRE.getStatus()) {
            final DateTime startTime = DateUtil.parse(DateUtil.today());
            updateTenant.setStartTime(startTime);
            updateTenant.setDays(days);
            final DateTime endTime = DateUtil.offsetDay(startTime, days);
            updateTenant.setEndTime(endTime);
            updateTenant.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        } else {
            // 如果在期限内则增加天数
            int finalDays = tenantPo.getDays() + days;
            final DateTime endTime = DateUtil.offsetDay(tenantPo.getStartTime(), finalDays);
            updateTenant.setDays(finalDays);
            updateTenant.setEndTime(endTime);
        }
        updateTenant.setId(tenantId);
        tenantRepository.updateById(updateTenant);
    }

    @Override
    public TenantResponse getTenantByParams(TenantRequest request) {
        final TenantPo tenantPo = tenantRepository.getByParams(request);
        return TenantCvt.INSTANCE.poToResponse(tenantPo);
    }

    public List<TenantResponse> getTenantListByParams(TenantRequest request) {
        final List<TenantPo> tenantPoList = tenantRepository.getListByParams(request);
        return TenantCvt.INSTANCE.poToResponseBatch(tenantPoList);
    }

    @Override
    public IPage<TenantResponse> getTenantPage(TenantRequest request) {
        Page<TenantPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<TenantPo> poPage = tenantRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<TenantPo> tenantPoList = poPage.getRecords();
        final List<TenantResponse> responseList = TenantCvt.INSTANCE.poToResponseBatch(tenantPoList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    @Override
    public TenantDetailResponse getDetailById(Long tenantId) {
        TenantDetailResponse tenantDetail = new TenantDetailResponse();
        // 查询租户信息
        final TenantPo tenantPo = tenantRepository.getById(tenantId);
        final TenantResponse tenantResponse = TenantCvt.INSTANCE.poToResponse(tenantPo);
        tenantDetail.setTenant(tenantResponse);

        // 管理员信息
        UserRequest userRequest = new UserRequest();
        userRequest.setTenantId(tenantId);
        userRequest.setIsManage(true);
        userRequest.setStatus(Constant.STATUS_NOT_DEL);
        final UserPo userPo = userRepository.getByParams(userRequest);
        final UserResponse userResponse = UserCvt.INSTANCE.poToResponse(userPo);
        tenantDetail.setUser(userResponse);

        // 系统角色信息
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setTenantId(tenantId);
        roleRequest.setIsMain(true);
        roleRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<RolePo> rolePos = roleRepository.getListByParams(roleRequest);
        final List<RoleResponse> roleResponses = RoleCvt.INSTANCE.poToResponseBatch(rolePos);
        tenantDetail.setRoleList(roleResponses);

        // 组织信息
        OrgRequest orgRequest = new OrgRequest();
        orgRequest.setTenantId(tenantId);
        orgRequest.setParentId(0L);
        orgRequest.setIsMain(true);
        orgRequest.setStatus(Constant.STATUS_NOT_DEL);
        final OrgPo orgPo = orgRepository.getByParams(orgRequest);
        final OrgResponse orgResponse = OrgCvt.INSTANCE.poToResponse(orgPo);
        tenantDetail.setOrg(orgResponse);

        // 资源信息
        final List<ResourcePo> resourcePoList = resourceRepository.getByTenantId(tenantId);
        final List<ResourceResponse> menuResponseList = resourceUtil.resourceTreeRemark(resourcePoList, null);
        tenantDetail.setMenuResponseList(menuResponseList);
        return tenantDetail;
    }

    /**
      * @description 根据code + name验证租户是否存在.
      * @author wjd
      * @date 2022/9/30
      * @param tenantRequest 验证信息
      */
    private void validateTenant(TenantRequest tenantRequest) {
        TenantRequest request = new TenantRequest();
        request.setCode(tenantRequest.getCode());
        request.setName(tenantRequest.getName());
        request.setStatus(Constant.STATUS_NOT_DEL);
        final Long count = tenantRepository.getCountByParams(request);
        if (count > 0) {
            final String message = "该租户已存在!";
            logger.error(message);
            throw new ApiException(message);
        }
    }

    /**
     * @description 新增租户---角色保存
     * @author wjd
     * @date 2022/11/11
     * @param roleList 待保存的角色信息集
     * @param tenantId 租户ID
     * @return 系统角色ID
     */
    private Long addRole(List<RoleRequest> roleList, Long tenantId) {
        final List<RolePo> rolePoList = RoleCvt.INSTANCE.requestToPoBatch(roleList);
        for (RolePo rolePo : rolePoList) {
            rolePo.setTenantId(tenantId);
            rolePo.setIsMain(true);
            rolePo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        }
        roleRepository.saveBatch(rolePoList);
        Long systemRoleId = null;
        for (RolePo role : rolePoList) {
            if (role.getIsSystem()) {
                systemRoleId = role.getId();
                continue;
            }
            // 非系统角色绑定固定资源
            resourceUtil.initRoleMenu(role);
        }
        return systemRoleId;
    }

    /**
     * @description 修改租户--修改角色.
     * @author wjd
     * @date 2022/11/11
     * @param roleList 角色ID集
     * @param tenantId 租户ID
     * @return 系统角色ID
     */
    private Long updateRole(List<RoleRequest> roleList, Long tenantId) {
        final List<RolePo> rolePoList = RoleCvt.INSTANCE.requestToPoBatch(roleList);
        // 获取存在ID的数据对其进行修改
        final List<RolePo> updateRoles = rolePoList.stream().filter(role -> null != role.getId()).collect(Collectors.toList());
        roleRepository.updateBatchById(updateRoles);
        // 查询删除的角色解除用户角色关联关系&角色资源关联关系
        final Set<Long> roleIds = updateRoles.stream().map(RolePo::getId).collect(Collectors.toSet());
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setTenantId(tenantId);
        roleRequest.setNotInRoleIds(roleIds);
        roleRequest.setIsMain(true);
        roleRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<RolePo> delRoles = roleRepository.getListByParams(roleRequest);
        if (CollectionUtil.isNotEmpty(delRoles)) {
            final List<RolePo> updateDelRole = delRoles.stream().map(role -> {
                RolePo delRol = new RolePo();
                delRol.setId(role.getId());
                delRol.setStatus(Constant.STATUS_DEL);
                return delRol;
            }).collect(Collectors.toList());
            roleRepository.updateBatchById(updateDelRole);

            final Set<Long> delRoleIds = delRoles.stream().map(RolePo::getId).collect(Collectors.toSet());
            // 删除用户角色关联关系
            UserRolePo userRolePo = new UserRolePo();
            userRolePo.setStatus(Constant.STATUS_DEL);
            UserRoleRequest userRoleRequest = new UserRoleRequest();
            userRoleRequest.setRoleIds(delRoleIds);
            userRoleRequest.setStatus(Constant.STATUS_NOT_DEL);
            userRoleRepository.updateByParams(userRolePo, userRoleRequest);
            // 删除角色资源关联关系
            RoleResourcePo roleResourcePo = new RoleResourcePo();
            roleResourcePo.setStatus(Constant.STATUS_DEL);
            RoleResourceRequest roleResourceRequest = new RoleResourceRequest();
            roleResourceRequest.setRoleIds(delRoleIds);
            roleResourceRequest.setStatus(Constant.STATUS_NOT_DEL);
            roleResourceRepository.updateByParams(roleResourcePo, roleResourceRequest);
        }
        // 过滤出新增且未删除的角色
        final List<RolePo> addRole = rolePoList.stream().filter(role -> null == role.getId()).collect(Collectors.toList());
        for (RolePo role : addRole) {
            role.setTenantId(tenantId);
            role.setIsMain(true);
            role.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        }
        roleRepository.saveBatch(addRole);
        // 绑定默认菜单
        resourceUtil.initRoleMenus(addRole);
        // 查询出系统角色
        final Optional<RolePo> optionalRolePo = updateRoles.stream().filter(RolePo::getIsSystem).findFirst();
        if (optionalRolePo.isEmpty()) {
            logger.error("请求体中未包含系统角色信息,其中角色信息体为{}", JSON.toJSONString(roleList));
            throw new ApiException();
        }
        final RolePo rolePo = optionalRolePo.get();
        return rolePo.getId();
    }

}
