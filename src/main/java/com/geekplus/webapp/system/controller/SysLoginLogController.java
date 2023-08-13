package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.system.entity.SysLoginLog;
import com.geekplus.webapp.system.service.SysLoginLogService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统登录日志 系统登录日志
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/loginLog")
public class SysLoginLogController extends BaseController {
    @Resource
    private SysLoginLogService sysLoginLogService;

    /**
     * 增加 系统登录日志
     */
    @PostMapping("/add")
    public Result add(@RequestBody SysLoginLog sysLoginLog) {
        return toResult(sysLoginLogService.insertSysLoginLog(sysLoginLog));
    }

    /**
    * 删除 系统登录日志
    */
    @RequiresPermissions("system:loginLog:delete")
    @GetMapping("/delete")
    public Result remove(@RequestParam Long logId) {
        return toResult(sysLoginLogService.deleteSysLoginLogById(logId));
    }

    /**
     * 清空 系统登录日志
     */
    @RequiresPermissions("system:loginLog:clean")
    @DeleteMapping("/clean")
    public Result cleanAll() {
        sysLoginLogService.cleanTable();
        return Result.success();
    }

    /**
    * 批量删除 系统登录日志
    */
    @RequiresPermissions("system:loginLog:delete")
    @DeleteMapping("/{logIds}")
    public Result remove(@PathVariable Long[] logIds) {
        return toResult(sysLoginLogService.deleteSysLoginLogByIds(logIds));
    }

    /**
    * 更新 系统登录日志
    */
    @PostMapping("/update")
    public Result edit(@RequestBody SysLoginLog sysLoginLog) {
        return toResult(sysLoginLogService.updateSysLoginLog(sysLoginLog));
    }

    /**
    * 单条数据详情 系统登录日志
    */
    @RequiresPermissions("system:loginLog:detail")
    @GetMapping("/detail")
    public Result detail(@RequestParam Long logId) {
        SysLoginLog sysLoginLog = sysLoginLogService.selectSysLoginLogById(logId);
        return Result.success(sysLoginLog);
    }

    /**
    * 条件查询所有 系统登录日志
    */
    @RequiresPermissions("system:loginLog:listAll")
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysLoginLog sysLoginLog) {
        //PageHelper.startPage(page, size);
        List<SysLoginLog> list = sysLoginLogService.selectSysLoginLogList(sysLoginLog);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(HttpStatusCode.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
    * 条件查询所有 系统登录日志
    */
    @RequiresPermissions("system:loginLog:list")
    @GetMapping("/list")
    public PageDataInfo list(SysLoginLog sysLoginLog) {
        startPage();
        List<SysLoginLog> list = sysLoginLogService.selectSysLoginLogList(sysLoginLog);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 导出系统登录日志
     */
    @RequiresPermissions("system:loginLog:export")
    @Log(title = "导出系统登录日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(SysLoginLog sysLoginLog)
    {
        List<SysLoginLog> list = sysLoginLogService.selectSysLoginLogList(sysLoginLog);
        ExcelUtil<SysLoginLog> util = new ExcelUtil<SysLoginLog>(SysLoginLog.class);
        return util.exportExcel(list, "sysLoginLog");
    }
}
