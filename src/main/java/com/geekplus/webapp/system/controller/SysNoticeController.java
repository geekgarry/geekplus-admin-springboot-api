package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.enums.OperatorType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.system.entity.SysNotice;
import com.geekplus.webapp.system.service.SysNoticeService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/notice")
public class SysNoticeController extends BaseController {
    @Resource
    private SysNoticeService sysNoticeService;

    /**
     * 增加
     */
    @RequiresPermissions("system:notice:add")
    @Log(title = "新增系统通知",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE,isSaveRequestData = false)
    @PostMapping("/add")
    public Result add(@RequestBody SysNotice sysNotice) {
        return toResult(sysNoticeService.insertSysNotice(sysNotice));
    }

    /**
    * 删除
    */
    @RequiresPermissions("system:notice:delete")
    @Log(title = "删除系统通知",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE,isSaveRequestData = false)
    @GetMapping("/delete")
    public Result remove(@RequestParam Long noticeId) {
        return toResult(sysNoticeService.deleteSysNoticeById(noticeId));
    }

    /**
    * 批量删除
    */
    @RequiresPermissions("system:notice:delete")
    @Log(title = "批量删除系统通知",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE,isSaveRequestData = false)
    @DeleteMapping("/{noticeIds}")
    public Result remove(@PathVariable Long[] noticeIds) {
        return toResult(sysNoticeService.deleteSysNoticeByIds(noticeIds));
    }

    /**
    * 更新
    */
    @RequiresPermissions("system:notice:update")
    @Log(title = "更新系统通知",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE,isSaveRequestData = false)
    @PostMapping("/update")
    public Result edit(@RequestBody SysNotice sysNotice) {
        return toResult(sysNoticeService.updateSysNotice(sysNotice));
    }

    /**
    * 单条数据详情
    */
    @RequiresPermissions("system:notice:detail")
    @GetMapping("/detail")
    public Result detail(@RequestParam Long noticeId) {
        SysNotice sysNotice = sysNoticeService.selectSysNoticeById(noticeId);
        return Result.success(sysNotice);
    }

    /**
    * 条件查询所有
    */
    @RequiresPermissions("system:notice:listAll")
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysNotice sysNotice) {
        //PageHelper.startPage(page, size);
        List<SysNotice> list = sysNoticeService.selectSysNoticeList(sysNotice);
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
    @RequiresPermissions("system:notice:list")
    @GetMapping("/list")
    public PageDataInfo list(SysNotice sysNotice) {
        startPage();
        List<SysNotice> list = sysNoticeService.selectSysNoticeList(sysNotice);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 导出系统通知列表
     */
    @RequiresPermissions("system:notice:export")
    @Log(title = "导出系统通知列表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(SysNotice sysNotice)
    {
        List<SysNotice> list = sysNoticeService.selectSysNoticeList(sysNotice);
        ExcelUtil<SysNotice> util = new ExcelUtil<SysNotice>(SysNotice.class);
        return util.exportExcel(list, "sysNotice");
    }
}
