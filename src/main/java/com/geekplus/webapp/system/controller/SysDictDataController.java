package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.annotation.RepeatSubmit;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.system.entity.SysDictData;
import com.geekplus.webapp.system.service.SysDictDataService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/dict/data")
public class SysDictDataController extends BaseController {
    @Resource
    private SysDictDataService sysDictDataService;

    /**
     * 增加
     */
    @PostMapping("/add")
    @RepeatSubmit
    public Result add(@RequestBody SysDictData sysDictData) {
        return toResult(sysDictDataService.insertSysDictData(sysDictData));
    }

    /**
    * 删除
    */
    @GetMapping("/delete")
    public Result remove(@RequestParam Long dictCode) {
        return toResult(sysDictDataService.deleteSysDictDataById(dictCode));
    }

    /**
    * 批量删除
    */
    @DeleteMapping("/{dictCodes}")
    public Result remove(@PathVariable Long[] dictCodes) {
        return toResult(sysDictDataService.deleteSysDictDataByIds(dictCodes));
    }

    /**
    * 更新
    */
    @PostMapping("/update")
    public Result edit(@RequestBody SysDictData sysDictData) {
        return toResult(sysDictDataService.updateSysDictData(sysDictData));
    }

    /**
    * 单条数据详情
    */
    @GetMapping("/detail")
    public Result detail(@RequestParam Long dictCode) {
        SysDictData sysDictData = sysDictDataService.selectSysDictDataById(dictCode);
        return Result.success(sysDictData);
    }

    /**
    * 条件查询所有
    */
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysDictData sysDictData) {
        //PageHelper.startPage(page, size);
        List<SysDictData> list = sysDictDataService.selectSysDictDataList(sysDictData);
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
    public PageDataInfo list(SysDictData sysDictData) {
        startPage();
        List<SysDictData> list = sysDictDataService.selectSysDictDataList(sysDictData);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 导出系统字典数据
     */
    @Log(title = "导出系统字典数据", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(SysDictData sysDictData)
    {
        List<SysDictData> list = sysDictDataService.selectSysDictDataList(sysDictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "sysDictData");
    }
}
