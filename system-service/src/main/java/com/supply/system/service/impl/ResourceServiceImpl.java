package com.supply.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.system.constant.ResourceTypeEnum;
import com.supply.system.cvt.ResourceCvt;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.po.RoleResourcePo;
import com.supply.system.model.po.TenantResourcePo;
import com.supply.system.model.request.ResourceRequest;
import com.supply.system.model.request.RoleResourceRequest;
import com.supply.system.model.request.TenantResourceRequest;
import com.supply.system.model.response.ResourceResponse;
import com.supply.system.repository.IResourceRepository;
import com.supply.system.repository.IRoleResourceRepository;
import com.supply.system.repository.ITenantResourceRepository;
import com.supply.system.service.IResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description 资源信息服务实现层.
 * @date 2022-08-05
 */
@Service
public class ResourceServiceImpl implements IResourceService {
    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    private final IResourceRepository resourceRepository;

    private final ITenantResourceRepository tenantResourceRepository;

    private final IRoleResourceRepository roleResourceRepository;

    public ResourceServiceImpl(IResourceRepository resourceRepository, ITenantResourceRepository tenantResourceRepository,
                               IRoleResourceRepository roleResourceRepository) {
        this.resourceRepository = resourceRepository;
        this.tenantResourceRepository = tenantResourceRepository;
        this.roleResourceRepository = roleResourceRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addResource(ResourceRequest request) {
        logger.info("[新增资源]---待新增的实体信息为{}", JSON.toJSONString(request));
        // 验证
        this.validateResource(request);
        final ResourcePo resourcePo = ResourceCvt.INSTANCE.requestToPo(request);
        // 默认业务状态为激活状态
        if (null == resourcePo.getBusinessStatus()) {
            resourcePo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        }
        // 默认非必须资源
        if (null == resourcePo.getIsFix()) {
            resourcePo.setIsFix(false);
        }
        resourceRepository.save(resourcePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResource(ResourceRequest request) {
        logger.info("[修改资源]---待修改的实体信息为{}", JSON.toJSONString(request));
        // 验证
        this.validateResource(request);
        final ResourcePo resourcePo = ResourceCvt.INSTANCE.requestToPo(request);
        resourceRepository.updateById(resourcePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delResource(Long resourceId) {
        logger.info("[删除资源]---其中资源ID为{}", resourceId);
        // 验证资源是否存在子级
        this.validateDelResource(resourceId);
        // 修改状态为删除状态
        ResourcePo resourcePo = new ResourcePo();
        resourcePo.setId(resourceId);
        resourcePo.setStatus(Constant.STATUS_DEL);
        resourceRepository.updateById(resourcePo);
        // 删除对应的租户资源关联关系
        TenantResourcePo tenantResourcePo = new TenantResourcePo();
        tenantResourcePo.setStatus(Constant.STATUS_DEL);
        TenantResourceRequest tenantResourceRequest = new TenantResourceRequest();
        tenantResourceRequest.setResourceId(resourceId);
        tenantResourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        tenantResourceRepository.updateByParams(tenantResourcePo, tenantResourceRequest);

        // 删除对应的角色资源关联关系
        RoleResourcePo roleResourcePo = new RoleResourcePo();
        resourcePo.setStatus(Constant.STATUS_DEL);
        RoleResourceRequest roleResourceRequest = new RoleResourceRequest();
        roleResourceRequest.setResourceId(resourceId);
        roleResourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        roleResourceRepository.updateByParams(roleResourcePo, roleResourceRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeResource(Long resourceId) {
        logger.info("[资源冻结]---其中资源ID为{}", resourceId);
        ResourcePo resourcePo = new ResourcePo();
        resourcePo.setId(resourceId);
        resourcePo.setBusinessStatus(BusinessStatusEnum.IN_FREEZE.getStatus());
        resourceRepository.updateById(resourcePo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeResource(Long resourceId) {
        logger.info("[资源解冻]---其中资源ID为{}", resourceId);
        ResourcePo resourcePo = new ResourcePo();
        resourcePo.setId(resourceId);
        resourcePo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        resourceRepository.updateById(resourcePo);
    }


    @Override
    public List<ResourceResponse> getResourceTree(ResourceRequest request) {
        List<ResourceResponse> responseList = new ArrayList<>();
        final List<ResourcePo> resourcePoList = resourceRepository.getListByParams(request);
        if (CollectionUtil.isEmpty(resourcePoList)) {
            return responseList;
        }
        // 实体转换
        final List<ResourceResponse> resourceResponseList = ResourceCvt.INSTANCE.poToResponseBatch(resourcePoList);
        // 固定菜单默认选中
        resourceResponseList.forEach(resource -> resource.setIsChecked(resource.getIsFix()));

        // 资源菜单集
        final List<ResourceResponse> menuList = resourceResponseList.stream()
                .filter(resourceResponse -> resourceResponse.getType() == ResourceTypeEnum.MENU.getType())
                .collect(Collectors.toList());
        // 资源按钮并根据菜单ID进行分组
        Map<Long, List<ResourceResponse>> buttonGroupByMenuIdMap = resourceResponseList.stream()
                .filter(resourceResponse -> resourceResponse.getType() == ResourceTypeEnum.BUTTON.getType())
                .collect(Collectors.groupingBy(ResourceResponse::getParentId));
        if (CollectionUtil.isEmpty(buttonGroupByMenuIdMap)) {
            buttonGroupByMenuIdMap = new HashMap<>();
        }

        // 组装树结构中菜单根节点
        final List<ResourceResponse> rootMenuList = menuList.stream()
                .filter(menu -> menu.getParentId() == 0L)
                .collect(Collectors.toList());
        responseList.addAll(rootMenuList);
        this.getLeafMenu(rootMenuList, menuList, buttonGroupByMenuIdMap);

        return responseList;
    }

    @Override
    public ResourceResponse getResourceByParams(ResourceRequest request) {
        final ResourcePo resourcePo = resourceRepository.getByParams(request);
        return ResourceCvt.INSTANCE.poToResponse(resourcePo);
    }

    /**
      * @description 新增/修改验证同类型下资源编码是否唯一.
      * @author wjd
      * @date 2022/11/5
      * @param request 待验证的实体信息
      */
    private void validateResource(ResourceRequest request) {
        // 验证同类型资源编码是否唯一
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setNeId(request.getId());
        resourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        resourceRequest.setType(request.getType());
        resourceRequest.setCode(request.getCode());
        final Long count = resourceRepository.getCountByParams(resourceRequest);
        if (count > 0) {
            logger.warn("资源类型{}编码{}已存在", request.getType(), request.getCode());
            throw new ApiException("编码已存在!");
        }
    }

    /**
      * @description 删除资源验证.
      * @author wjd
      * @date 2022/11/5
      * @param resourceId 资源ID
      */
    private void validateDelResource(Long resourceId) {
        // 验证规则: 存在子级不能删
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setParentId(resourceId);
        resourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        final Long count = resourceRepository.getCountByParams(resourceRequest);
        if (count > 0) {
            final String message = "存在子级,无法删除";
            logger.warn(message);
            throw new ApiException(message);
        }
    }

    /**
     * @description 组装菜单树结构.
     * @author wjd
     * @date 2022/5/20
     * @param rootMenuList 根节点菜单集
     * @param menuList 菜单集
     * @param buttonGroupByMenuIdMap 根据菜单ID分组的按钮集
     */
    private void getLeafMenu(List<ResourceResponse> rootMenuList, List<ResourceResponse> menuList, Map<Long, List<ResourceResponse>> buttonGroupByMenuIdMap) {
        for (ResourceResponse menu : rootMenuList) {
            final Long menuId = menu.getId();
            // 获取该菜单下是否存在按钮
            if (buttonGroupByMenuIdMap.containsKey(menuId)) {
                final List<ResourceResponse> buttonList = buttonGroupByMenuIdMap.get(menuId);
                menu.setButtonResponseList(buttonList);
            }

            // 根据id查询出子菜单集
            final List<ResourceResponse> childrenMenuList = menuList.stream()
                    .filter(childrenMenu -> Objects.equals(childrenMenu.getParentId(), menuId))
                    .collect(Collectors.toList());
            menu.setChildrenList(childrenMenuList);
            this.getLeafMenu(menu.getChildrenList(), menuList, buttonGroupByMenuIdMap);
        }
    }
}
