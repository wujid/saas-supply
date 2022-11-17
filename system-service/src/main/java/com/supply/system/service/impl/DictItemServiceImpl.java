package com.supply.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.supply.common.constant.Constant;
import com.supply.common.util.CommonUtil;
import com.supply.system.cvt.DictItemCvt;
import com.supply.system.model.po.DictItemPo;
import com.supply.system.model.po.DictPo;
import com.supply.system.model.request.DictItemRequest;
import com.supply.system.model.request.DictRequest;
import com.supply.system.model.response.DictItemResponse;
import com.supply.system.repository.IDictItemRepository;
import com.supply.system.repository.IDictRepository;
import com.supply.system.service.IDictItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
@Service
public class DictItemServiceImpl implements IDictItemService {
    private static final Logger logger = LoggerFactory.getLogger(DictItemServiceImpl.class);

    private final IDictItemRepository dictItemRepository;

    private final IDictRepository dictRepository;

    public DictItemServiceImpl(IDictItemRepository dictItemRepository, IDictRepository dictRepository) {
        this.dictItemRepository = dictItemRepository;
        this.dictRepository = dictRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDictItem(DictItemRequest request) {
        logger.info("[新增数据字典集]---待新增的实体信息为{}", JSON.toJSONString(request));
        // 转换保存
        final DictItemPo dictItemPo = DictItemCvt.INSTANCE.requestToPo(request);
        dictItemRepository.save(dictItemPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictItem(DictItemRequest request) {
        logger.info("[修改数据字典集]---待修改的实体信息为{}", JSON.toJSONString(request));
        // 转换保存
        final DictItemPo dictItemPo = DictItemCvt.INSTANCE.requestToPo(request);
        dictItemRepository.updateById(dictItemPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delDictItem(Long dictItemId) {
        logger.info("[删除数据字典集]---其中数据字典集ID为{}", dictItemId);
        DictItemPo dictItemPo = new DictItemPo();
        dictItemPo.setId(dictItemId);
        dictItemPo.setStatus(Constant.STATUS_DEL);
        dictItemRepository.updateById(dictItemPo);
    }

    @Override
    public IPage<DictItemResponse> getPageByParams(DictItemRequest request) {
        Page<DictItemPo> page = new Page<>(request.getPageIndex(), request.getPageSize());
        final Page<DictItemPo> poPage = dictItemRepository.getPageByParams(page, request);
        if (poPage.getTotal() <= 0) {
            return new Page<>(request.getPageIndex(), request.getPageSize());
        }
        final List<DictItemPo> poList = page.getRecords();
        final List<DictItemResponse> responseList = DictItemCvt.INSTANCE.poToResponseBatch(poList);
        return CommonUtil.pageCvt(responseList, poPage);
    }

    @Override
    public List<DictItemResponse> getListByParams(DictItemRequest request) {
        final List<DictItemPo> dictItemPoList = dictItemRepository.getListByParams(request);
        return DictItemCvt.INSTANCE.poToResponseBatch(dictItemPoList);
    }

    @Override
    public List<DictItemResponse> getListByCode(String code) {
        // 根据编码查询出对应的业务字典类型
        DictRequest dictRequest = new DictRequest();
        dictRequest.setCode(code);
        dictRequest.setStatus(Constant.STATUS_NOT_DEL);
        final DictPo dictPo = dictRepository.getByParams(dictRequest);
        if (null == dictPo) {
            return null;
        }
        // 根据业务字典类型对应的主键ID查询出数据字典集
        DictItemRequest dictItemRequest = new DictItemRequest();
        dictItemRequest.setDictId(dictPo.getId());
        dictItemRequest.setStatus(Constant.STATUS_NOT_DEL);
        return this.getListByParams(dictItemRequest);
    }

    @Override
    public Map<String, List<DictItemResponse>> getMapByCodes(Set<String> codeList) {
        if (CollectionUtil.isEmpty(codeList)) {
            return null;
        }
        // 根据编码查询出对应的业务数据字典
        DictRequest dictRequest = new DictRequest();
        dictRequest.setCodeList(codeList);
        dictRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<DictPo> dictPoList = dictRepository.getListByParams(dictRequest);

        // 查询出所有的数据字典信息集
        final Set<Long> dictIds = dictPoList.stream().map(DictPo::getId).collect(Collectors.toSet());
        DictItemRequest dictItemRequest = new DictItemRequest();
        dictItemRequest.setDictIdList(dictIds);
        dictItemRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<DictItemResponse> itemResponseList = this.getListByParams(dictItemRequest);

        return this.getMap(dictPoList, itemResponseList);
    }

    @Override
    public Map<String, List<DictItemResponse>> getAllMap() {
        DictRequest dictRequest = new DictRequest();
        dictRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<DictPo> dictPoList = dictRepository.getListByParams(dictRequest);

        DictItemRequest dictItemRequest = new DictItemRequest();
        dictItemRequest.setStatus(Constant.STATUS_NOT_DEL);
        final List<DictItemResponse> itemResponseList = this.getListByParams(dictItemRequest);

        return this.getMap(dictPoList, itemResponseList);
    }

    /**
      * @description 组装数据字典code映射字典子项集信息.
      * @author wjd
      * @date 2022/11/8
      * @param dictPoList 字典集
      * @param itemResponseList 字典子项集
      * @return code => 字典子项集
      */
    private Map<String, List<DictItemResponse>> getMap(List<DictPo> dictPoList, List<DictItemResponse> itemResponseList) {
        if (CollectionUtil.isEmpty(dictPoList) || CollectionUtil.isEmpty(itemResponseList)) {
            return new HashMap<>();
        }
        final Map<Long, String> dictIdToCode = dictPoList.stream().collect(Collectors.toMap(DictPo::getId, DictPo::getCode, (k1, k2) -> k1));
        // 根据编码进行分组
        return itemResponseList.stream().collect(Collectors.groupingBy(item -> dictIdToCode.get(item.getDictId()), Collectors.toList()));
    }
}
