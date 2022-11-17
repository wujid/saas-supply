package com.supply.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.system.model.po.DictItemPo;
import com.supply.system.model.request.DictItemRequest;
import com.supply.system.model.response.DictItemResponse;
import com.supply.system.service.IDictItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wjd
 * @description
 * @date 2022-08-29
 */
@Api(tags="数据字典集信息控制层")
@RestController
@RequestMapping("/dictItem")
public class DictItemController {

    private final IDictItemService dictItemService;

    public DictItemController(IDictItemService dictItemService) {
        this.dictItemService = dictItemService;
    }

    @ApiOperation(value = "新增数据字典集")
    @PostMapping("/addDictItem")
    public Result<Object> addDictItem(@RequestBody DictItemRequest request) {
        dictItemService.addDictItem(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改数据字典集")
    @PostMapping("/updateDictItem")
    public Result<Object> updateDictItem(@RequestBody DictItemRequest request) {
        dictItemService.updateDictItem(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除数据字典集")
    @GetMapping("/delDictItem")
    public Result<Object> delDictItem(@RequestParam Long dictItemId) {
        dictItemService.delDictItem(dictItemId);
        return Result.ok();
    }

    @ApiOperation(value = "数据字典集分页信息")
    @GetMapping("/getDictItemPageByParams")
    public Result<IPage<DictItemResponse>> getDictItemPageByParams(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                                   @RequestParam(required = false) Long dictId,
                                                                   @RequestParam(required = false) String label, @RequestParam(required = false) String value) {
        DictItemRequest request = new DictItemRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setDictId(dictId);
        request.setLabel(label);
        request.setValue(value);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(DictItemPo::getSort);
        request.setIsAsc(true);
        final IPage<DictItemResponse> page = dictItemService.getPageByParams(request);
        return Result.ok(page);
    }

    @ApiOperation(value = "根据编码获取数据字典集")
    @GetMapping("/getDictItemListByCode")
    public Result<Object> getDictItemListByCode(@RequestParam String dictCode) {
        final List<DictItemResponse> list = dictItemService.getListByCode(dictCode);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据编码集查询对应的数据字典集映射信息")
    @PostMapping("/getMapByCodes")
    public Result<Object> getMapByCodes(@RequestBody Set<String> codeList) {
        final Map<String, List<DictItemResponse>> map = dictItemService.getMapByCodes(codeList);
        return Result.ok(map);
    }

    @ApiOperation(value = "获取所有数据字典code映射字典信息集")
    @GetMapping("/getAllMap")
    public Result<Object> getAllMap() {
        final Map<String, List<DictItemResponse>> map = dictItemService.getAllMap();
        return Result.ok(map);
    }
}
