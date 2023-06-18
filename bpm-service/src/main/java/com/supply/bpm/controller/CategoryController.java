package com.supply.bpm.controller;

import com.supply.bpm.model.request.CategoryRequest;
import com.supply.bpm.model.response.CategoryResponse;
import com.supply.bpm.service.ICategoryService;
import com.supply.common.model.Result;
import com.supply.common.web.util.ContextUtil;
import com.supply.common.web.validate.AddGroup;
import com.supply.common.web.validate.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wjd
 * @description 流程分类信息权限控制层.
 * @date 2022-12-20
 */
@Api(tags="流程分类信息权限控制层")
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final ICategoryService categoryService;


    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "新增流程分类")
    @PostMapping("/addCategory")
    public Result<?> addCategory(@RequestBody @Validated(AddGroup.class) CategoryRequest request) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        request.setTenantId(tenantId);
        categoryService.addCategory(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改流程分类")
    @PostMapping("/updateCategory")
    public Result<?> updateCategory(@RequestBody @Validated(UpdateGroup.class) CategoryRequest request) {
        categoryService.updateCategory(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除流程分类")
    @GetMapping("/delCategory")
    public Result<?> delCategory(@RequestParam Long categoryId) {
        categoryService.delCategory(categoryId);
        return Result.ok();
    }

    @ApiOperation(value = "流程分类树结构")
    @GetMapping("/getCategoryTreeByParams")
    public Result<List<CategoryResponse>> getCategoryTreeByParams(@RequestParam(required = false) Long parentId, @RequestParam(required = false) String name) {
        final Long tenantId = ContextUtil.getCurrentTenantId();
        final List<CategoryResponse> list = categoryService.getCategoryTree(parentId, name, tenantId);
        return Result.ok(list);
    }
}
