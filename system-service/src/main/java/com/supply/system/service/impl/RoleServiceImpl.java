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
        logger.info("[????????????]---???????????????????????????{}", JSON.toJSONString(request));
        // ??????????????????????????????
        this.validateRole(request);
        // ???????????????
        final RolePo rolePo = RoleCvt.INSTANCE.requestToPo(request);
        // ?????????????????????????????????
        if (null == rolePo.getBusinessStatus()) {
            rolePo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        }
        // ?????????????????????
        if (null == rolePo.getIsMain()) {
            rolePo.setIsMain(false);
        }
        // ???????????????????????????????????????
        rolePo.setIsSystem(false);
        roleRepository.save(rolePo);
        // ?????????????????????
        resourceUtil.initRoleMenu(rolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleRequest request) {
        logger.info("[????????????]---?????????????????????{}", JSON.toJSONString(request));
        // ??????????????????????????????
        this.validateRole(request);
        // ???????????????
        final RolePo rolePo = RoleCvt.INSTANCE.requestToPo(request);
        roleRepository.updateById(rolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delRole(Long roleId) {
        logger.info("[????????????]---????????????ID???{}", roleId);
        // ??????????????????
        RolePo rolePo = new RolePo();
        rolePo.setId(roleId);
        rolePo.setStatus(Constant.STATUS_DEL);
        roleRepository.updateById(rolePo);
        // ??????????????????????????????
        UserRolePo userRolePo = new UserRolePo();
        userRolePo.setStatus(Constant.STATUS_DEL);
        UserRoleRequest userRoleRequest = new UserRoleRequest();
        userRoleRequest.setRoleId(roleId);
        userRoleRequest.setStatus(Constant.STATUS_NOT_DEL);
        userRoleRepository.updateByParams(userRolePo, userRoleRequest);
        // ??????????????????????????????
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
        logger.info("[????????????]---????????????ID???{}", roleId);
        RolePo rolePo = new RolePo();
        rolePo.setId(roleId);
        rolePo.setBusinessStatus(BusinessStatusEnum.IN_FREEZE.getStatus());
        roleRepository.updateById(rolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeUser(Long roleId) {
        logger.info("[????????????]---????????????????????????ID???{}", roleId);
        RolePo rolePo = new RolePo();
        rolePo.setId(roleId);
        rolePo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        roleRepository.updateById(rolePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setResourceAuth(RoleRequest request) {
        logger.info("[????????????????????????]---????????????ID???{}, ??????ID???{}", request.getId(), request.getResourceIdList());
        final Long roleId = request.getId();
        // ????????????????????????????????????
        RoleResourcePo roleResourcePo = new RoleResourcePo();
        roleResourcePo.setStatus(Constant.STATUS_DEL);
        RoleResourceRequest roleResourceRequest = new RoleResourceRequest();
        roleResourceRequest.setRoleId(roleId);
        roleResourceRepository.updateByParams(roleResourcePo, roleResourceRequest);

        // ??????????????????????????????
        final List<Long> resourceIdList = request.getResourceIdList();
        if (CollectionUtil.isEmpty(resourceIdList)) {
            logger.info("[????????????????????????]---??????ID???{}????????????????????????", roleId);
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
        // ??????????????????
        final RolePo rolePo = roleRepository.getById(roleId);
        if (null == rolePo) {
            logger.error("[????????????ID??????????????????]---????????????????????????,??????ID???{}", roleId);
            throw new ApiException("????????????");
        }
        RoleResponse response = RoleCvt.INSTANCE.poToResponse(rolePo);

        // ????????????????????????????????????
        final List<ResourcePo> roleResourceList = resourceRepository.getByRoleIdList(List.of(roleId), null);
        // ??????????????????????????????????????????????????????????????????
        final List<ResourceResponse> menuResponseList = resourceUtil.resourceTreeRemark(roleResourceList, tenantId);
        response.setMenuResponseList(menuResponseList);

        return response;
    }

    /**
      * @description ??????/????????????????????????.
      * @author wjd
      * @date 2022/8/3
      * @param request ????????????????????????
      */
    private void validateRole(RoleRequest request) {
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setNeId(request.getId());
        roleRequest.setName(request.getName());
        roleRequest.setTenantId(request.getTenantId());
        roleRequest.setStatus(Constant.STATUS_NOT_DEL);
        // ????????????????????????
        final Long count = roleRepository.getCountByParams(roleRequest);
        if (count > 0) {
            logger.warn("???????????????{}?????????", request.getName());
            throw new ApiException("????????????????????????!");
        }
    }
}
