package com.supply.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.util.CommonUtil;
import com.supply.system.cvt.DictCategoryCvt;
import com.supply.system.model.po.DictCategoryPo;
import com.supply.system.model.request.DictCategoryRequest;
import com.supply.system.model.response.DictCategoryResponse;
import com.supply.system.repository.IDictCategoryRepository;
import com.supply.system.service.IDictCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-11-08
 */
@Service
public class DictCategoryServiceImpl implements IDictCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(DictCategoryServiceImpl.class);

    private final IDictCategoryRepository dictCategoryRepository;

    public DictCategoryServiceImpl(IDictCategoryRepository dictCategoryRepository) {
        this.dictCategoryRepository = dictCategoryRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDictCategory(DictCategoryRequest request) {
        logger.info("[新增字典分类]---待新增的实体信息为{}", JSON.toJSONString(request));
        // 数据验证
        final DictCategoryPo parent = this.validateDict(request);
        // 存在父级则将父级修改为存在子级
        if (null != parent) {
            DictCategoryPo parentCategory = new DictCategoryPo();
            parentCategory.setId(parent.getId());
            parentCategory.setHasChildren(true);
            dictCategoryRepository.updateById(parentCategory);
        }
        // 转换保存
        final DictCategoryPo dictCategoryPo = DictCategoryCvt.INSTANCE.requestToPo(request);
        dictCategoryRepository.save(dictCategoryPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictCategory(DictCategoryRequest request) {
        logger.info("[修改字典分类]---待修改的实体信息为{}", JSON.toJSONString(request));
        // 数据验证
        this.validateDict(request);
        if (request.getParentId() == null) {
            request.setParentId(0L);
        }
        // 转换保存
        final DictCategoryPo dictCategoryPo = DictCategoryCvt.INSTANCE.requestToPo(request);
        dictCategoryRepository.updateById(dictCategoryPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delDictCategory(Long dictCategoryId) {
        logger.info("[删除字典分类]---其中字典分类ID为{}", dictCategoryId);
        final DictCategoryPo categoryPo = dictCategoryRepository.getById(dictCategoryId);
        if (null == categoryPo) {
            logger.error("待删除字典分类树ID{}不存在", dictCategoryId);
            throw new ApiException();
        }
        // 删除子级
        DictCategoryPo dictCategoryPo = new DictCategoryPo();
        dictCategoryPo.setStatus(Constant.STATUS_DEL);
        DictCategoryRequest request = new DictCategoryRequest();
        request.setParentId(dictCategoryId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        dictCategoryRepository.updateByParams(dictCategoryPo, request);

        // 删除当前字典分类
        dictCategoryPo.setId(dictCategoryId);
        dictCategoryRepository.updateById(dictCategoryPo);

        final Long parentId = categoryPo.getParentId();
        if (null == parentId) {
           return;
        }
        // 判断是否还存在子级,不存在则修改是否包含子级为不包含
        DictCategoryRequest dictCategoryRequest = new DictCategoryRequest();
        dictCategoryRequest.setParentId(parentId);
        dictCategoryRequest.setStatus(Constant.STATUS_NOT_DEL);
        final Long count = dictCategoryRepository.getCountByParams(dictCategoryRequest);
        if (count > 0) {
            return;
        }
        DictCategoryPo parent = new DictCategoryPo();
        parent.setParentId(parentId);
        parent.setHasChildren(false);
        dictCategoryRepository.updateById(parent);
    }

    @Override
    public IPage<DictCategoryResponse> getPageByParams(DictCategoryRequest request) {
        Page<DictCategoryPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<DictCategoryPo> poPage = dictCategoryRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<DictCategoryPo> poList = page.getRecords();
        final List<DictCategoryResponse> responseList = DictCategoryCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    @Override
    public List<DictCategoryResponse> getListByParams(DictCategoryRequest request) {
        final List<DictCategoryPo> dictCategoryPoList = dictCategoryRepository.getListByParams(request);
        return DictCategoryCvt.INSTANCE.poToResponseBatch(dictCategoryPoList);
    }

    /**
      * @description 唯一性验证.
      * @author wjd
      * @date 2022/11/8
      * @param request 待验证的实体
      */
    private DictCategoryPo validateDict(DictCategoryRequest request) {
        DictCategoryPo parentCategory = null;

        // 编码验证
        DictCategoryRequest dictCategoryRequest = new DictCategoryRequest();
        dictCategoryRequest.setNeId(request.getId());
        dictCategoryRequest.setCode(request.getCode());
        dictCategoryRequest.setStatus(Constant.STATUS_NOT_DEL);
        final Long count = dictCategoryRepository.getCountByParams(dictCategoryRequest);
        if (count > 0) {
            logger.warn("字典分类编码{}已存在", request.getCode());
            throw new ApiException("编码已存在!");
        }
        // 初始化默认赋值
        final Long parentId = request.getParentId();
        if (parentId == null || parentId == 0L) {
            request.setParentId(0L);
            request.setLevel(1);
            request.setHasChildren(false);
        } else {
            // 查询父节点信息
            parentCategory = dictCategoryRepository.getById(parentId);
            if (null == parentCategory) {
                logger.error("根据父节点ID{}未查询到父节点信息", parentId);
                throw new ApiException();
            }
            final int level = parentCategory.getLevel() + 1;
            request.setLevel(level);
        }
        return parentCategory;
    }
}
