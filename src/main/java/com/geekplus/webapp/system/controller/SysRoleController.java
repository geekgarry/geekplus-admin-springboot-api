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
import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.service.SysMenuService;
import com.geekplus.webapp.system.service.SysRoleService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统角色表 系统角色表
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 增加 系统角色表
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE,isSaveRequestData = false)
    @PostMapping("/add")
    @RepeatSubmit
    public Result add(@RequestBody SysRole sysRole) {
        return toResult(sysRoleService.insertSysRole(sysRole));
    }

    /**
    * 删除 系统角色表
    */
    @RequiresPermissions("system:role:delete")
    @Log(title = "角色管理",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE,isSaveRequestData = false)
    @GetMapping("/delete")
    public Result remove(@RequestParam Long roleId) {
        return toResult(sysRoleService.deleteSysRoleById(roleId));
    }

    /**
    * 批量删除 系统角色表
    */
    @RequiresPermissions("system:role:delete")
    @Log(title = "角色管理",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE,isSaveRequestData = false)
    @DeleteMapping("/{roleIds}")
    public Result remove(@PathVariable Long[] roleIds) {
        return toResult(sysRoleService.deleteSysRoleByIds(roleIds));
    }

    /**
    * 更新 系统角色表
    */
    @RequiresPermissions("system:role:update")
    @Log(title = "角色管理",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE,isSaveRequestData = false)
    @PostMapping("/update")
    public Result edit(@RequestBody SysRole sysRole) {
        return toResult(sysRoleService.updateSysRole(sysRole));
    }

    /**
    * 单条数据详情 系统角色表
    */
    @RequiresPermissions("system:role:detail")
    @GetMapping("/detail")
    public Result detail(@RequestParam Long roleId) {
        SysRole sysRole = sysRoleService.selectSysRoleById(roleId);
        List<SysMenu> sysMenuList=sysMenuService.getMenuTreeByRoleId(roleId);
        sysRole.setSysMenuList(sysMenuList);
        return Result.success(sysRole);
    }

    /**
    * 条件查询所有 系统角色表
    */
    @RequiresPermissions("system:role:listAll")
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysRole sysRole) {
        //PageHelper.startPage(page, size);
        List<SysRole> list = sysRoleService.selectSysRoleList(sysRole);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(HttpStatusCode.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
    * 条件查询所有 系统角色表
    */
    @RequiresPermissions("system:role:list")
    @GetMapping("/list")
    public PageDataInfo list(SysRole sysRole) {
        startPage();
        List<SysRole> list = sysRoleService.selectSysRoleList(sysRole);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 导出系统角色表
     */
    @RequiresPermissions("system:role:export")
    @Log(title = "导出系统角色表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(SysRole sysRole)
    {
        List<SysRole> list = sysRoleService.selectSysRoleList(sysRole);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "sysRole");
    }
}
