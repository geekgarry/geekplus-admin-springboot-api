package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.system.entity.SysDictType;
import com.geekplus.webapp.system.service.SysDictTypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/dict/type")
public class SysDictTypeController extends BaseController {
    @Resource
    private SysDictTypeService sysDictTypeService;

    /**
     * 增加
     */
    @PostMapping("/add")
    public Result add(@RequestBody SysDictType sysDictType) {
        return toResult(sysDictTypeService.insertSysDictType(sysDictType));
    }

    /**
    * 删除
    */
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    public Result remove(@RequestParam Long dictId) {
        return toResult(sysDictTypeService.deleteSysDictTypeById(dictId));
    }

    /**
    * 批量删除
    */
    @Log(title = "批量删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public Result remove(@PathVariable Long[] dictIds) {
        return toResult(sysDictTypeService.deleteSysDictTypeByIds(dictIds));
    }

    /**
    * 更新
    */
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public Result edit(@RequestBody SysDictType sysDictType) {
        return toResult(sysDictTypeService.updateSysDictType(sysDictType));
    }

    /**
    * 单条数据详情
    */
    @GetMapping("/detail")
    public Result detail(@RequestParam Long dictId) {
        SysDictType sysDictType = sysDictTypeService.selectSysDictTypeById(dictId);
        return Result.success(sysDictType);
    }

    /**
    * 条件查询所有
    */
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysDictType sysDictType) {
        //PageHelper.startPage(page, size);
        List<SysDictType> list = sysDictTypeService.selectSysDictTypeList(sysDictType);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(HttpStatusCode.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
    * 条件查询所有
    */
    @GetMapping("/list")
    public PageDataInfo list(SysDictType sysDictType) {
        startPage();
        List<SysDictType> list = sysDictTypeService.selectSysDictTypeList(sysDictType);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 导出数据字典类型
     */
    @Log(title = "导出数据字典类型", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(SysDictType sysDictType)
    {
        List<SysDictType> list = sysDictTypeService.selectSysDictTypeList(sysDictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        return util.exportExcel(list, "sysDictType");
    }
}
