package com.supply.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.supply.common.constant.BusinessStatusEnum;
import com.supply.common.constant.Constant;
import com.supply.system.cvt.OrgCvt;
import com.supply.system.model.po.OrgPo;
import com.supply.system.model.request.OrgRequest;
import com.supply.system.model.response.OrgResponse;
import com.supply.system.repository.IOrgRepository;
import com.supply.system.service.IOrgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-08-10
 */
@Service
public class OrgServiceImpl implements IOrgService {
    private static final Logger logger = LoggerFactory.getLogger(OrgServiceImpl.class);

    private final IOrgRepository orgRepository;

    public OrgServiceImpl(IOrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrg(OrgRequest request) {
        logger.info("[新增组织机构]---待新增的实体信息为{}", JSON.toJSONString(request));
        final OrgPo orgPo = OrgCvt.INSTANCE.requestToPo(request);
        orgPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        orgPo.setIsMain(false);
        orgRepository.save(orgPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrg(OrgRequest request) {
        logger.info("[修改组织机构]---待修改的实体信息为{}", JSON.toJSONString(request));
        final OrgPo orgPo = OrgCvt.INSTANCE.requestToPo(request);
        orgRepository.updateById(orgPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delOrg(Long orgId) {
        logger.info("[删除组织机构]---其中组织机构ID为{}", orgId);
        // 存在子级或者存在用户则不能删除
        OrgPo orgPo = new OrgPo();
        orgPo.setStatus(Constant.STATUS_DEL);
        orgPo.setId(orgId);
        orgRepository.updateById(orgPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeOrg(Long orgId) {
        logger.info("[冻结组织机构]---其中组织机构ID为{}", orgId);
        // 存在子级或者存在用户则不能删除
        OrgPo orgPo = new OrgPo();
        orgPo.setBusinessStatus(BusinessStatusEnum.IN_FREEZE.getStatus());
        orgPo.setId(orgId);
        orgRepository.updateById(orgPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeOrg(Long orgId) {
        logger.info("[解冻组织机构]---其中组织机构ID为{}", orgId);
        // 存在子级或者存在用户则不能删除
        OrgPo orgPo = new OrgPo();
        orgPo.setBusinessStatus(BusinessStatusEnum.IN_ACTIVE.getStatus());
        orgPo.setId(orgId);
        orgRepository.updateById(orgPo);
    }

    @Override
    public List<OrgResponse> getOrgListByParams(OrgRequest request) {
        final List<OrgPo> poList = orgRepository.getListByParams(request);
        return OrgCvt.INSTANCE.poToResponseBatch(poList);
    }

    @Override
    public List<OrgResponse> getOrgTree(Long tenantId, Long parentId, String name) {
        List<OrgResponse> result = new ArrayList<>();

        OrgRequest request = new OrgRequest();
        request.setTenantId(tenantId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(OrgPo::getSort);
        request.setIsAsc(true);
        // 根据条件查询结果集
        final List<OrgPo> orgPoList = orgRepository.getListByParams(request);
        if (CollectionUtil.isEmpty(orgPoList)) {
            return result;
        }
        // 实体转换
        final List<OrgResponse> orgResponseList = OrgCvt.INSTANCE.poToResponseBatch(orgPoList);

        // 获取根节点集
        if (null == parentId) {
            parentId = 0L;
        }
        final Long finalParentId = parentId;
        List<OrgResponse> rootList = orgResponseList.stream().
                filter(orgResponse -> Objects.equals(orgResponse.getParentId(), finalParentId)).collect(Collectors.toList());
        // 叶子节点
        result.addAll(rootList);
        this.getLeafNode(rootList, orgResponseList);
        // 存在名称模糊查询条件则进行过滤
        if (StrUtil.isNotBlank(name)) {
            this.treeMatch(result, name);
        }
        return result;
    }

    /**
      * @description 获取叶子节点.
      * @author wjd
      * @date 2022/8/12
      * @param rootList 根节点集
      * @param orgResponseList 组织机构信息集
      */
    private void getLeafNode(List<OrgResponse> rootList, List<OrgResponse> orgResponseList) {
        for (OrgResponse orgResponse : rootList) {
            final Long parentId = orgResponse.getId();
            final String parentName = orgResponse.getName();
            // 获取该叶子节点的子集
            final List<OrgResponse> childrenList = orgResponseList.stream()
                    .filter(org -> Objects.equals(org.getParentId(), parentId))
                    .collect(Collectors.toList());
            childrenList.forEach(org -> org.setParentName(parentName));
            orgResponse.setChildrenList(childrenList);
            this.getLeafNode(childrenList, orgResponseList);
        }
    }

    /**
      * @description 过滤树形结构.
      * @author wjd
      * @date 2022/8/16
      * @param orgList 待过滤的组织机构树结构
     *  @param name 匹配条件
      */
    private  void treeMatch(List<OrgResponse> orgList, String name) {
        Iterator<OrgResponse> oneIter = orgList.iterator();
        while (oneIter.hasNext()) {
            OrgResponse oneOrg = oneIter.next();
            final List<OrgResponse> childrenList = oneOrg.getChildrenList();
            // 如果包含则什么也不做(不移除),否则就看子级目录
            if(!oneOrg.getName().contains(name)){
                if(CollectionUtil.isNotEmpty(childrenList)){
                    this.treeMatch(childrenList, name);
                }
                // 如果子级目录全部被移除,则移除父级目录
                if(CollectionUtil.isEmpty(childrenList)){
                    oneIter.remove();
                }
            }
        }
    }


}
