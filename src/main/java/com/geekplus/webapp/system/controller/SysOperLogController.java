package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.core.socket.WebSocketServer;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.system.entity.SysOperLog;
import com.geekplus.webapp.system.service.SysOperLogService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 系统操作日志 系统操作日志
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/operateLog")
public class SysOperLogController extends BaseController {
    @Resource
    private SysOperLogService sysOperLogService;
    @Resource
    private WebSocketServer webSocketServer;
    /**
     * 增加 系统操作日志
     */
    @PostMapping("/add")
    public Result add(@RequestBody SysOperLog sysOperLog) throws IOException {
        return toResult(sysOperLogService.insertSysOperLog(sysOperLog));
    }

    /**
    * 删除 系统操作日志
    */
    @RequiresPermissions("system:operLog:delete")
    @GetMapping("/delete")
    public Result remove(@RequestParam Long operId) {
        return toResult(sysOperLogService.deleteSysOperLogById(operId));
    }

    /**
     * 清空 系统操作日志
     */
    @RequiresPermissions("system:operLog:clean")
    @DeleteMapping("/clean")
    public Result cleanAll() {
        sysOperLogService.cleanTable();
        return Result.success();
    }

    /**
    * 批量删除 系统操作日志
    */
    @RequiresPermissions("system:operLog:delete")
    @DeleteMapping("/{operIds}")
    public Result remove(@PathVariable Long[] operIds) {
        return toResult(sysOperLogService.deleteSysOperLogByIds(operIds));
    }

    /**
    * 更新 系统操作日志
    */
    @PostMapping("/update")
    public Result edit(@RequestBody SysOperLog sysOperLog) {
        return toResult(sysOperLogService.updateSysOperLog(sysOperLog));
    }

    /**
    * 单条数据详情 系统操作日志
    */
    @RequiresPermissions("system:operLog:detail")
    @GetMapping("/detail")
    public Result detail(@RequestParam Long operId) {
        SysOperLog sysOperLog = sysOperLogService.selectSysOperLogById(operId);
        return Result.success(sysOperLog);
    }

    /**
    * 条件查询所有 系统操作日志
    */
    @RequiresPermissions("system:operLog:listAll")
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysOperLog sysOperLog) {
        //PageHelper.startPage(page, size);
        List<SysOperLog> list = sysOperLogService.selectSysOperLogList(sysOperLog);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(HttpStatusCode.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
    * 条件查询所有 系统操作日志
    */
    @RequiresPermissions("system:operlog:list")
    @GetMapping("/list")
    public PageDataInfo list(SysOperLog sysOperLog) {
        startPage();
        List<SysOperLog> list = sysOperLogService.selectSysOperLogList(sysOperLog);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 导出系统操作日志
     */
    @RequiresPermissions("system:operLog:export")
    @Log(title = "导出系统操作日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(SysOperLog sysOperLog)
    {
        List<SysOperLog> list = sysOperLogService.selectSysOperLogList(sysOperLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        return util.exportExcel(list, "sysOperLog");
    }
}
