package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.annotation.RepeatSubmit;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.enums.OperatorType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.system.entity.SysConfig;
import com.geekplus.webapp.system.service.SysConfigService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController extends BaseController {
    @Resource
    private SysConfigService sysConfigService;

    /**
     * 增加
     */
    @Log(title = "新增系统配置",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE)
    @PostMapping("/add")
    @RepeatSubmit
    public Result add(@RequestBody SysConfig sysConfig) {
        return toResult(sysConfigService.insertSysConfig(sysConfig));
    }

    /**
    * 删除
    */
    @Log(title = "删除系统配置",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @GetMapping("/delete")
    public Result remove(@RequestParam Integer configId) {
        return toResult(sysConfigService.deleteSysConfigById(configId));
    }

    /**
    * 批量删除
    */
    @Log(title = "批量删除系统配置",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @DeleteMapping("/{configIds}")
    public Result remove(@PathVariable Integer[] configIds) {
        return toResult(sysConfigService.deleteSysConfigByIds(configIds));
    }

    /**
    * 更新
    */
    @Log(title = "修改系统配置",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE)
    @PostMapping("/update")
    public Result edit(@RequestBody SysConfig sysConfig) {
        return toResult(sysConfigService.updateSysConfig(sysConfig));
    }

    /**
    * 单条数据详情
    */
    @GetMapping("/detail")
    public Result detail(@RequestParam Integer configId) {
        SysConfig sysConfig = sysConfigService.selectSysConfigById(configId);
        return Result.success(sysConfig);
    }

    /**
    * 条件查询所有
    */
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysConfig sysConfig) {
        //PageHelper.startPage(page, size);
        List<SysConfig> list = sysConfigService.selectSysConfigList(sysConfig);
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
    public PageDataInfo list(SysConfig sysConfig) {
        startPage();
        List<SysConfig> list = sysConfigService.selectSysConfigList(sysConfig);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 导出系统配置
     */
    @Log(title = "导出系统配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(SysConfig sysConfig)
    {
        List<SysConfig> list = sysConfigService.selectSysConfigList(sysConfig);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        return util.exportExcel(list, "sysConfig");
    }
}
