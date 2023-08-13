package com.geekplus.webapp.system.controller;

import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.webapp.system.entity.SysPermission;
import com.geekplus.webapp.system.service.SysPermissionService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统权限表 系统权限表
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController extends BaseController {
    @Resource
    private SysPermissionService sysPermissionService;

    /**
     * 增加 系统权限表
     */
    @PostMapping("/add")
    public Result add(@RequestBody SysPermission sysPermission) {
        return toResult(sysPermissionService.insertSysPermission(sysPermission));
    }

    /**
    * 删除 系统权限表
    */
    @GetMapping("/delete")
    public Result remove(@RequestParam Long permissionId) {
        return toResult(sysPermissionService.deleteSysPermissionById(permissionId));
    }

    /**
    * 批量删除 系统权限表
    */
    @DeleteMapping("/{permissionIds}")
    public Result remove(@PathVariable Long[] permissionIds) {
        return toResult(sysPermissionService.deleteSysPermissionByIds(permissionIds));
    }

    /**
    * 更新 系统权限表
    */
    @PostMapping("/update")
    public Result edit(@RequestBody SysPermission sysPermission) {
        return toResult(sysPermissionService.updateSysPermission(sysPermission));
    }

    /**
    * 单条数据详情 系统权限表
    */
    @GetMapping("/detail")
    public Result detail(@RequestParam Long permissionId) {
        SysPermission sysPermission = sysPermissionService.selectSysPermissionById(permissionId);
        return Result.success(sysPermission);
    }

    /**
    * 条件查询所有 系统权限表
    */
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysPermission sysPermission) {
        //PageHelper.startPage(page, size);
        List<SysPermission> list = sysPermissionService.selectSysPermissionList(sysPermission);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(HttpStatusCode.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
    * 条件查询所有 系统权限表
    */
    @GetMapping("/list")
    public PageDataInfo list(SysPermission sysPermission) {
        startPage();
        List<SysPermission> list = sysPermissionService.selectSysPermissionList(sysPermission);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }
}
