package com.supply.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.util.CommonUtil;
import com.supply.system.cvt.RoleCvt;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.po.RolePo;
import com.supply.system.model.po.RoleResourcePo;
import com.supply.system.model.po.UserRolePo;
import com.supply.system.model.request.RoleRequest;
import com.supply.system.model.request.RoleResourceRequest;
import com.supply.system.model.request.UserRoleRequest;
import com.supply.system.model.response.ResourceResponse;
import com.supply.system.model.response.RoleResponse;
import com.supply.system.repository.IResourceRepository;
import com.supply.system.repository.IRoleRepository;
import com.supply.system.repository.IRoleResourceRepository;
import com.supply.system.repository.IUserRoleRepository;
import com.supply.system.service.IRoleService;
import com.supply.system.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-03
 */
@Service
public class RoleServiceImpl implements IRoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final IRoleRepository roleRepository;

    private final IResourceRepository resourceRepository;

    private final IRoleResourceRepository roleResourceRepository;

    private final IUserRoleRepository userRoleRepository;

    private final ResourceUtil resourceUtil;

    public RoleServiceImpl(IRoleRepository roleRepository, IResourceRepository resourceRepository,
                           IRoleResourceRepository roleResourceRepository, IUserRoleRepository userRoleRepository,
                           ResourceUtil resourceUtil) {
        this.roleRepository = roleRepository;
        this.resourceRepository = resourceRepository;
        this.roleResourceRepository = roleResourceRepository;
        this.userRoleRepository = userRoleRepository;
        this.resourceUtil = resourceUtil;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(RoleRequest request) {
        logger.info("[新增角色]---待新增的实体信息为{}", JSON.toJSONString(request));
        // 验证该角色是否已存在
        this.validateRole(request);
        // 转换并保存
        final RolePo rolePo = RoleCvt.INSTANCE.requestToPo(request);
        // 默认业务状态为激活状态
        if (null == rolePo.getBusinessStatus()) {
            rolePo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        }
        // 默认非主要角色
        if (null == rolePo.getIsMain()) {
            rolePo.setIsMain(false);
        }
        // 新增角色均为非系统管理角色
        rolePo.setIsSystem(false);
        roleRepository.save(rolePo);
        // 保存固定的菜单
        resourceUtil.initRoleMenu(rolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleRequest request) {
        logger.info("[修改角色]---其中实体信息为{}", JSON.toJSONString(request));
        // 验证该角色是否已存在
        this.validateRole(request);
        // 转换并修改
        final RolePo rolePo = RoleCvt.INSTANCE.requestToPo(request);
        roleRepository.updateById(rolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delRole(Long roleId) {
        logger.info("[删除角色]---其中角色ID为{}", roleId);
        // 删除角色信息
        RolePo rolePo = new RolePo();
        rolePo.setId(roleId);
        rolePo.setStatus(Constant.STATUS_DEL);
        roleRepository.updateById(rolePo);
        // 删除用户角色关联关系
        UserRolePo userRolePo = new UserRolePo();
        userRolePo.setStatus(Constant.STATUS_DEL);
        UserRoleRequest userRoleRequest = new UserRoleRequest();
        userRoleRequest.setRoleId(roleId);
        userRoleRequest.setStatus(Constant.STATUS_NOT_DEL);
        userRoleRepository.updateByParams(userRolePo, userRoleRequest);
        // 删除角色资源关联关系
        RoleResourcePo roleResourcePo = new RoleResourcePo();
        roleResourcePo.setStatus(Constant.STATUS_DEL);
        RoleResourceRequest roleResourceRequest = new RoleResourceRequest();
        roleResourceRequest.setRoleId(roleId);
        roleResourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        roleResourceRepository.updateByParams(roleResourcePo, roleResourceRequest);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeRole(Long roleId) {
        logger.info("[角色冻结]---其中角色ID为{}", roleId);
        RolePo rolePo = new RolePo();
        rolePo.setId(roleId);
        rolePo.setBusinessStatus(BusinessStatusEnum.IN_FREEZE.getStatus());
        roleRepository.updateById(rolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeUser(Long roleId) {
        logger.info("[角色激活]---其中待激活的角色ID为{}", roleId);
        RolePo rolePo = new RolePo();
        rolePo.setId(roleId);
        rolePo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        roleRepository.updateById(rolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setResourceAuth(RoleRequest request) {
        logger.info("[设置角色资源权限]---其中角色ID为{}, 资源ID为{}", request.getId(), request.getResourceIdList());
        final Long roleId = request.getId();
        // 删除历史角色资源关联关系
        RoleResourcePo roleResourcePo = new RoleResourcePo();
        roleResourcePo.setStatus(Constant.STATUS_DEL);
        RoleResourceRequest roleResourceRequest = new RoleResourceRequest();
        roleResourceRequest.setRoleId(roleId);
        roleResourceRepository.updateByParams(roleResourcePo, roleResourceRequest);

        // 新建角色资源关联关系
        final List<Long> resourceIdList = request.getResourceIdList();
        if (CollectionUtil.isEmpty(resourceIdList)) {
            logger.info("[设置角色资源权限]---角色ID为{}无对应的资源权限", roleId);
            return;
        }
        List<RoleResourcePo> roleResourcePoList = new ArrayList<>();
        for (Long resourceId : resourceIdList) {
            RoleResourcePo roleResource = new RoleResourcePo();
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(resourceId);
            roleResourcePoList.add(roleResource);
        }
        roleResourceRepository.saveBatch(roleResourcePoList);
    }

    @Override
    public List<RoleResponse> getListByParams(RoleRequest request) {
        final List<RolePo> rolePoList = roleRepository.getListByParams(request);
        return RoleCvt.INSTANCE.poToResponseBatch(rolePoList);
    }

    @Override
    public IPage<RoleResponse> getPageByParams(RoleRequest request) {
        Page<RolePo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<RolePo> poPage = roleRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<RolePo> poList = page.getRecords();
        final List<RoleResponse> responseList = RoleCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    @Override
    public RoleResponse getAuthByRoleId(Long roleId, Long tenantId) {
        // 获取角色信息
        final RolePo rolePo = roleRepository.getById(roleId);
        if (null == rolePo) {
            logger.error("[根据角色ID获取资源信息]---未获取到角色信息,角色ID为{}", roleId);
            throw new ApiException("请求异常");
        }
        RoleResponse response = RoleCvt.INSTANCE.poToResponse(rolePo);

        // 获取当前角色的资源信息集
        final List<ResourcePo> roleResourceList = resourceRepository.getByRoleIdList(List.of(roleId), null);
        // 查询所有菜单组装树结构并标记该角色存在的菜单
        final List<ResourceResponse> menuResponseList = resourceUtil.resourceTreeRemark(roleResourceList, tenantId);
        response.setMenuResponseList(menuResponseList);

        return response;
    }

    /**
      * @description 新增/修改角色信息验证.
      * @author wjd
      * @date 2022/8/3
      * @param request 待验证的角色信息
      */
    private void validateRole(RoleRequest request) {
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setNeId(request.getId());
        roleRequest.setName(request.getName());
        roleRequest.setTenantId(request.getTenantId());
        roleRequest.setStatus(Constant.STATUS_NOT_DEL);
        // 验证名称是否存在
        final Long count = roleRepository.getCountByParams(roleRequest);
        if (count > 0) {
            logger.warn("该角色名称{}已存在", request.getName());
            throw new ApiException("该角色名称已存在!");
        }
    }
}
