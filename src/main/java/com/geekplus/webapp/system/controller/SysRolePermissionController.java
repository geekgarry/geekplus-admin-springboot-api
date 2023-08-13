package com.geekplus.webapp.system.controller;

import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.webapp.system.entity.SysRolePermission;
import com.geekplus.webapp.system.service.SysRolePermissionService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户角色权限关系表 用户角色权限关系表
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/rolePermission")
public class SysRolePermissionController extends BaseController {
    @Resource
    private SysRolePermissionService sysRolePermissionService;

    /**
     * 增加 用户角色权限关系表
     */
    @PostMapping("/add")
    public Result add(@RequestBody SysRolePermission sysRolePermission) {
        return toResult(sysRolePermissionService.insertSysRolePermission(sysRolePermission));
    }

    /**
    * 删除 用户角色权限关系表
    */
    @GetMapping("/delete")
    public Result remove(@RequestParam Long roleId) {
        return toResult(sysRolePermissionService.deleteSysRolePermissionById(roleId));
    }

    /**
    * 批量删除 用户角色权限关系表
    */
    @DeleteMapping("/{roleIds}")
    public Result remove(@PathVariable Long[] roleIds) {
        return toResult(sysRolePermissionService.deleteSysRolePermissionByIds(roleIds));
    }

    /**
    * 更新 用户角色权限关系表
    */
    @PostMapping("/update")
    public Result edit(@RequestBody SysRolePermission sysRolePermission) {
        return toResult(sysRolePermissionService.updateSysRolePermission(sysRolePermission));
    }

    /**
    * 单条数据详情 用户角色权限关系表
    */
    @GetMapping("/detail")
    public Result detail(@RequestParam Long roleId) {
        SysRolePermission sysRolePermission = sysRolePermissionService.selectSysRolePermissionById(roleId);
        return Result.success(sysRolePermission);
    }

    /**
    * 条件查询所有 用户角色权限关系表
    */
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysRolePermission sysRolePermission) {
        //PageHelper.startPage(page, size);
        List<SysRolePermission> list = sysRolePermissionService.selectSysRolePermissionList(sysRolePermission);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(HttpStatusCode.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
    * 条件查询所有 用户角色权限关系表
    */
    @GetMapping("/list")
    public PageDataInfo list(SysRolePermission sysRolePermission) {
        startPage();
        List<SysRolePermission> list = sysRolePermissionService.selectSysRolePermissionList(sysRolePermission);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }
}
