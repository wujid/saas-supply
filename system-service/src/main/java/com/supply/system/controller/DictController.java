package com.supply.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.supply.common.constant.Constant;
import com.supply.common.model.Result;
import com.supply.common.web.validate.AddGroup;
import com.supply.common.web.validate.UpdateGroup;
import com.supply.system.model.po.DictPo;
import com.supply.system.model.request.DictRequest;
import com.supply.system.model.response.DictResponse;
import com.supply.system.service.IDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjd
 * @description 数据字典控制层.
 * @date 2022-08-29
 */
@Api(tags="数据字典控制层")
@RestController
@RequestMapping("/dict")
public class DictController {

    private final IDictService dictService;

    public DictController(IDictService dictService) {
        this.dictService = dictService;
    }

    @ApiOperation(value = "新增数据字典")
    @PostMapping("/addDict")
    public Result<?> addDict(@RequestBody @Validated(value = AddGroup.class) DictRequest request) {
        dictService.addDict(request);
        return Result.ok();
    }

    @ApiOperation(value = "修改数据字典")
    @PostMapping("/updateDict")
    public Result<?> updateDict(@RequestBody @Validated(value = UpdateGroup.class) DictRequest request) {
        dictService.updateDict(request);
        return Result.ok();
    }

    @ApiOperation(value = "删除数据字典")
    @GetMapping("/delDict")
    public Result<?> delDict(@RequestParam Long dictId) {
        dictService.delDict(dictId);
        return Result.ok();
    }

    @ApiOperation(value = "数据字典分页信息")
    @GetMapping("/getDictPageByParams")
    public Result<IPage<DictResponse>> getDictPageByParams(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
                                                           @RequestParam(required = false) String code, @RequestParam(required = false) String name) {
        DictRequest request = new DictRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setCode(code);
        request.setName(name);
        request.setStatus(Constant.STATUS_NOT_DEL);
        request.setOrderColumn(DictPo::getCreateTime);
        final IPage<DictResponse> page = dictService.getPageByParams(request);
        return Result.ok(page);
    }
}
