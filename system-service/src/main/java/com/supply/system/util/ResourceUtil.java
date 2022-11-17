package com.supply.system.util;

import cn.hutool.core.collection.CollectionUtil;
import com.supply.common.constant.Constant;
import com.supply.system.constant.ResourceTypeEnum;
import com.supply.system.cvt.ResourceCvt;
import com.supply.system.model.po.ResourcePo;
import com.supply.system.model.po.RolePo;
import com.supply.system.model.po.RoleResourcePo;
import com.supply.system.model.po.TenantResourcePo;
import com.supply.system.model.request.ResourceRequest;
import com.supply.system.model.request.TenantResourceRequest;
import com.supply.system.model.response.ResourceResponse;
import com.supply.system.repository.IResourceRepository;
import com.supply.system.repository.IRoleResourceRepository;
import com.supply.system.repository.ITenantResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-08-10
 */
@Component
public class ResourceUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResourceUtil.class);

    private final IResourceRepository resourceRepository;

    private final ITenantResourceRepository tenantResourceRepository;

    private final IRoleResourceRepository roleResourceRepository;

    public ResourceUtil(IResourceRepository resourceRepository, ITenantResourceRepository tenantResourceRepository,
                        IRoleResourceRepository roleResourceRepository) {
        this.resourceRepository = resourceRepository;
        this.tenantResourceRepository = tenantResourceRepository;
        this.roleResourceRepository = roleResourceRepository;
    }

    /**
      * @description 资源树结构并标记指定的资源.
      * @author wjd
      * @date 2022/8/10
      * @param markResourceList 待标记的资源
      * @param tenantId 租户ID: 如存在则查询该租户下的资源否则查询所有资源
      * @return 资源树结构
      */
    public List<ResourceResponse> resourceTreeRemark(List<ResourcePo> markResourceList, Long tenantId) {
        List<ResourceResponse> response = new ArrayList<>();
        // 获取当前所有未删除的且该租户所拥有的资源信息集
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        if (null != tenantId) {
            final Set<Long> resourceIds = this.getResourceIdListByTenantId(tenantId);
            if (CollectionUtil.isEmpty(resourceIds)) {
                return response;
            }
            resourceRequest.setResourceIds(resourceIds);
        }
        final List<ResourcePo> resourcePoList = resourceRepository.getListByParams(resourceRequest);

        // 查询所有按钮集并标记该角色存在的按钮
        final List<ResourceResponse> buttonResponseList = this.getButtonAndRemark(markResourceList, resourcePoList);
        // 根据菜单ID进行分组
        final Map<Long, List<ResourceResponse>> menuToButtonGroup = buttonResponseList.stream().collect(Collectors.groupingBy(ResourceResponse::getParentId));

        // 查询所有菜单组装树结构并标记该角色存在的菜单
        response = this.getMenuTreeAndRemark(markResourceList, resourcePoList, menuToButtonGroup);
        return response;
    }

    /**
     * @description 初始化创建角色时绑定固定资源.
     * @author wjd
     * @date 2022/8/3
     * @param rolePo 角色信息
     */
    public void initRoleMenu(RolePo rolePo) {
        final Long roleId = rolePo.getId();
        // 查询出固定的资源菜单
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setIsFix(true);
        resourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<ResourcePo> resourcePoList = resourceRepository.getListByParams(resourceRequest);
        if (CollectionUtil.isEmpty(resourcePoList)) {
            logger.info("未查询到创建角色时需要绑定的菜单!");
            return;
        }
        List<RoleResourcePo> list = new ArrayList<>();
        for (ResourcePo resourcePo : resourcePoList) {
            final Long resourceId = resourcePo.getId();
            RoleResourcePo roleResourcePo = new RoleResourcePo();
            roleResourcePo.setRoleId(roleId);
            roleResourcePo.setResourceId(resourceId);
            list.add(roleResourcePo);
        }
        roleResourceRepository.saveBatch(list);
    }

    /**
     * @description 初始化创建角色时批量绑定固定资源.
     * @author wjd
     * @date 2022/8/3
     * @param rolePos 角色信息集
     */
    public void initRoleMenus(List<RolePo> rolePos) {
        if (CollectionUtil.isEmpty(rolePos) ) {
            logger.info("批量初始化角色绑定固定资源无角色集!");
            return;
        }
        // 查询出固定的资源菜单
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setIsFix(true);
        resourceRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<ResourcePo> resourcePoList = resourceRepository.getListByParams(resourceRequest);
        if (CollectionUtil.isEmpty(resourcePoList)) {
            logger.info("批量初始化角色绑定时未查询到创建角色时需要绑定的菜单!");
            return;
        }
        List<RoleResourcePo> list = new ArrayList<>();
        for (RolePo rolePo : rolePos) {
            final Long roleId = rolePo.getId();
            for (ResourcePo resourcePo : resourcePoList) {
                final Long resourceId = resourcePo.getId();
                RoleResourcePo roleResourcePo = new RoleResourcePo();
                roleResourcePo.setRoleId(roleId);
                roleResourcePo.setResourceId(resourceId);
                list.add(roleResourcePo);
            }
        }
        roleResourceRepository.saveBatch(list);
    }

    /**
     * @description 根据租户ID查询该租户所拥有的资源信息集.
     * @author wjd
     * @date 2022/8/9
     * @param tenantId 租户ID
     * @return 租户所拥有的资源信息集
     */
    private Set<Long> getResourceIdListByTenantId(Long tenantId) {
        Set<Long> set = new HashSet<>();
        if (null == tenantId) {
            return set;
        }
        TenantResourceRequest request = new TenantResourceRequest();
        request.setTenantId(tenantId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<TenantResourcePo> tenantResourcePoList = tenantResourceRepository.getListByParams(request);
        if (CollectionUtil.isEmpty(tenantResourcePoList)) {
            return set;
        }
        set = tenantResourcePoList.stream().map(TenantResourcePo::getResourceId).collect(Collectors.toSet());
        return set;
    }

    /**
     * @description 获取所有按钮集并标记该角色对应的按钮.
     * @author wjd
     * @date 2022/8/3
     * @param roleResourceList 角色下的资源信息集
     * @param resourcePoList 所有未删除的资源信息集
     * @return 所有按钮集
     */
    private List<ResourceResponse> getButtonAndRemark(List<ResourcePo> roleResourceList, List<ResourcePo> resourcePoList) {
        List<ResourceResponse> buttonResponseList = new ArrayList<>();
        // 获取该角色下的按钮ID集
        Set<Long> buttonIds = new HashSet<>();
        if (CollectionUtil.isNotEmpty(roleResourceList)) {
            buttonIds = roleResourceList.stream()
                    .filter(resourcePo -> resourcePo.getType() == ResourceTypeEnum.BUTTON.getType())
                    .map(ResourcePo::getId).collect(Collectors.toSet());
        }
        // 获取所有未删除的按钮信息集
        final List<ResourcePo> buttonList = resourcePoList.stream()
                .filter(resourcePo -> resourcePo.getType() == ResourceTypeEnum.BUTTON.getType())
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(buttonList)) {
            return buttonResponseList;
        }
        // 标记所有按钮中该角色关联的按钮
        buttonResponseList = ResourceCvt.INSTANCE.poToResponseBatch(buttonList);
        for (ResourceResponse resource : buttonResponseList) {
            final Long buttonId = resource.getId();
            // 判断按钮是否属于该角色
            if (buttonIds.contains(buttonId)) {
                resource.setIsChecked(true);
            }
        }
        return buttonResponseList;
    }

    private List<ResourceResponse> getMenuTreeAndRemark(List<ResourcePo> markResourceList, List<ResourcePo> resourcePoList, Map<Long, List<ResourceResponse>> menuToButtonGroup) {
        List<ResourceResponse> responseList = new ArrayList<>();
        // 获取该角色下的菜单ID集
        Set<Long> roleMenuIds = new HashSet<>();
        if (CollectionUtil.isNotEmpty(markResourceList)) {
            roleMenuIds = markResourceList.stream()
                    .filter(resourcePo -> resourcePo.getType() == ResourceTypeEnum.MENU.getType())
                    .map(ResourcePo::getId).collect(Collectors.toSet());
        }
        // 获取所有未删除的菜单信息集
        final List<ResourcePo> menuList = resourcePoList.stream()
                .filter(resourcePo -> resourcePo.getType() == ResourceTypeEnum.MENU.getType())
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(menuList)) {
            return responseList;
        }
        final List<ResourceResponse> menuResponseList = ResourceCvt.INSTANCE.poToResponseBatch(menuList);

        // 组装树结构中菜单根节点
        final List<ResourceResponse> rootMenuList = menuResponseList.stream()
                .filter(menuResponse -> menuResponse.getParentId() == 0L)
                .collect(Collectors.toList());
        responseList.addAll(rootMenuList);
        // 组装树结构中叶子菜单按钮
        this.getLeafMenu(rootMenuList, menuResponseList, roleMenuIds, menuToButtonGroup);

        return responseList;
    }

    private void getLeafMenu(List<ResourceResponse> rootMenuList, List<ResourceResponse> menuResponseList,
                             Set<Long> roleMenuIds, Map<Long, List<ResourceResponse>> menuToButtonGroup) {
        if (CollectionUtil.isEmpty(rootMenuList)) {
            return;
        }
        for (ResourceResponse menuResponse : rootMenuList) {
            final Long menuId = menuResponse.getId();
            // 判断菜单是否属于该角色,属于则进行标记
            if (roleMenuIds.contains(menuId)) {
                menuResponse.setIsChecked(true);
            }
            // 获取该菜单下是否存在按钮
            if (menuToButtonGroup.containsKey(menuId)) {
                final List<ResourceResponse> buttonResponses = menuToButtonGroup.get(menuId);
                menuResponse.setButtonResponseList(buttonResponses);
            }

            // 根据id查询出子菜单集
            final List<ResourceResponse> childrenMenuList = menuResponseList.stream()
                    .filter(menu -> Objects.equals(menu.getParentId(), menuId))
                    .collect(Collectors.toList());
            menuResponse.setChildrenList(childrenMenuList);
            this.getLeafMenu(menuResponse.getChildrenList(), menuResponseList, roleMenuIds, menuToButtonGroup);
        }
    }
}
