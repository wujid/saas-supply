package com.supply.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.common.constant.Constant;
import com.supply.common.exception.ApiException;
import com.supply.common.util.CommonUtil;
import com.supply.system.cvt.DictCvt;
import com.supply.system.model.po.DictItemPo;
import com.supply.system.model.po.DictPo;
import com.supply.system.model.request.DictItemRequest;
import com.supply.system.model.request.DictRequest;
import com.supply.system.model.response.DictResponse;
import com.supply.system.repository.IDictItemRepository;
import com.supply.system.repository.IDictRepository;
import com.supply.system.service.IDictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
@Service
public class DictServiceImpl implements IDictService {
    private static final Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);

    private final IDictRepository dictRepository;

    private final IDictItemRepository dictItemRepository;

    public DictServiceImpl(IDictRepository dictRepository, IDictItemRepository dictItemRepository) {
        this.dictRepository = dictRepository;
        this.dictItemRepository = dictItemRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDict(DictRequest request) {
        logger.info("[新增数据字典]---待新增的实体信息为{}", JSON.toJSONString(request));
        // 数据验证
        this.validateDict(request);
        // 转换保存
        final DictPo dictPo = DictCvt.INSTANCE.requestToPo(request);
        dictRepository.save(dictPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(DictRequest request) {
        logger.info("[修改数据字典]---待修改的实体信息为{}", JSON.toJSONString(request));
        // 数据验证
        this.validateDict(request);
        // 转换保存
        final DictPo dictPo = DictCvt.INSTANCE.requestToPo(request);
        dictRepository.updateById(dictPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delDict(Long dictId) {
        logger.info("[删除数据字典]---其中字典ID为{}", dictId);
        // 删除字典
        DictPo dictPo = new DictPo();
        dictPo.setId(dictId);
        dictPo.setStatus(Constant.STATUS_DEL);
        dictRepository.updateById(dictPo);
        // 删除对应的数据字典集
        DictItemPo dictItemPo = new DictItemPo();
        dictItemPo.setStatus(Constant.STATUS_DEL);
        DictItemRequest dictItemRequest = new DictItemRequest();
        dictItemRequest.setDictId(dictId);
        dictItemRequest.setStatus(Constant.STATUS_NOT_DEL);
        dictItemRepository.updateByParams(dictItemPo, dictItemRequest);
    }

    @Override
    public IPage<DictResponse> getPageByParams(DictRequest request) {
        Page<DictPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<DictPo> poPage = dictRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<DictPo> poList = page.getRecords();
        final List<DictResponse> responseList = DictCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    /**
      * @description 新增/修改唯一性验证.
      * @author wjd
      * @date 2022/11/7
      * @param request 待验证实体
      */
    private void validateDict(DictRequest request) {
        // 验证编码是否唯一
        DictRequest dictRequest = new DictRequest();
        dictRequest.setNeId(request.getId());
        dictRequest.setStatus(Constant.STATUS_NOT_DEL);
        dictRequest.setCode(request.getCode());
        final Long count = dictRepository.getCountByParams(dictRequest);
        if (count > 0) {
            logger.warn("编码{}已存在", request.getCode());
            throw new ApiException("编码已存在!");
        }
    }

}
