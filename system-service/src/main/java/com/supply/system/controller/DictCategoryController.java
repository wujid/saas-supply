package com.supply.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.system.model.po.DictCategoryPo;
import com.supply.system.model.request.DictCategoryRequest;
import com.supply.system.model.response.DictCategoryResponse;
import com.supply.system.service.IDictCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wjd
 * @description 字典分类控制层.
 * @date 2022-11-08
 */
@Api(tags="字典分类控制层")
@RestController
@RequestMapping("/dictCategory")
public class DictCategoryController {

    private final IDictCategoryService dictCategoryService;

    public DictCategoryController(IDictCategoryService dictCategoryService) {
        this.dictCategoryService = dictCategoryService;
    }

    @ApiOperation(value = "新增字典分类")
    @PostMapping("/addDictCategory")
    public Result<Object> addDictCategory(@RequestBody DictCategoryRequest request) {
        dictCategoryService.addDictCategory(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改字典分类")
    @PostMapping("/updateDictCategory")
    public Result<Object> updateDictCategory(@RequestBody DictCategoryRequest request) {
        dictCategoryService.updateDictCategory(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除字典分类")
    @GetMapping("/delDictCategory")
    public Result<Object> delDictCategory(@RequestParam Long dictCategoryId) {
        dictCategoryService.delDictCategory(dictCategoryId);
        return Result.ok();
    }

    @ApiOperation(value = "根据自定义条件查询带分页的字典分类信息集")
    @GetMapping("/getDictCategoryPageByParams")
    public Result<IPage<DictCategoryResponse>> getDictCategoryPageByParams(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                           @RequestParam(required = false) String code, @RequestParam(required = false) String name,
                                                           @RequestParam(required = false) Long parentId) {
        DictCategoryRequest request = new DictCategoryRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setParentId(parentId);
        request.setCode(code);
        request.setLikeName(name);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(DictCategoryPo::getCreateTime);
        final IPage<DictCategoryResponse> page = dictCategoryService.getPageByParams(request);
        return Result.ok(page);
    }

    @ApiOperation(value = "根据父级ID获取子级集")
    @GetMapping("/getChildrenByParentId")
    public Result<Object> getChildrenByParentId(@RequestParam Long parentId) {
        DictCategoryRequest request = new DictCategoryRequest();
        request.setParentId(parentId);
        request.setStatus(Constant.STATUS_NOT_DEL);
        final List<DictCategoryResponse> list = dictCategoryService.getListByParams(request);
        return Result.ok(list);
    }
}
