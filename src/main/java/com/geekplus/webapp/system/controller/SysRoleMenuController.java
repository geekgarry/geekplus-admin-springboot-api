package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.annotation.RepeatSubmit;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.webapp.common.service.SysUserTokenService;
import com.geekplus.webapp.system.entity.SysRoleMenu;
import com.geekplus.webapp.system.entity.SysUser;
import com.geekplus.webapp.system.service.SysRoleMenuService;
import com.geekplus.webapp.system.service.SysUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/roleMenu")
public class SysRoleMenuController extends BaseController {
    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysUserTokenService sysUserTokenService;

    /**
     * 增加
     */
    @Log(title = "添加角色和菜单权限",businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @RepeatSubmit
    public Result add(@RequestBody SysRoleMenu sysRoleMenu) {
        int count = sysRoleMenuService.insertSysRoleMenu(sysRoleMenu);
        if(count>0){
            LoginUser loginUser = sysUserTokenService.getLoginUser(ServletUtil.getRequest());
            SysUser sysUser = loginUser.getSysUser();
            Set sysUserPerms = sysUserService.getSysUserMenuPerms(sysUser.getUserId());
            loginUser.setSysMenuList(sysUserPerms);
            return Result.success();
        }else {
            return Result.error();
        }
    }

    /**
     * 增加
     */
    @Log(title = "批量添加角色和菜单权限",businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    @RepeatSubmit
    public Result batchAdd(@RequestBody List<SysRoleMenu> sysRoleMenu) {
        int count = sysRoleMenuService.batchInsertSysRoleMenuList(sysRoleMenu);
        if(count>0){
            LoginUser loginUser = sysUserTokenService.getLoginUser(ServletUtil.getRequest());
            SysUser sysUser = loginUser.getSysUser();
            Set sysUserPerms = sysUserService.getSysUserMenuPerms(sysUser.getUserId());
            loginUser.setSysMenuList(sysUserPerms);
            return Result.success();
        }else {
            return Result.error();
        }
    }

    /**
    * 删除
    */
    @Log(title = "删除角色和菜单权限",businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    public Result remove(@RequestParam Long roleId) {
        return toResult(sysRoleMenuService.deleteSysRoleMenuById(roleId));
    }

    /**
    * 批量删除
    */
    @Log(title = "批量删除角色和菜单权限",businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public Result remove(@PathVariable Long[] roleIds) {
        return toResult(sysRoleMenuService.deleteSysRoleMenuByIds(roleIds));
    }

    /**
    * 更新
    */
    @Log(title = "更新角色和菜单权限",businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public Result edit(@RequestBody SysRoleMenu sysRoleMenu) {
        return toResult(sysRoleMenuService.updateSysRoleMenu(sysRoleMenu));
    }

    /**
    * 单条数据详情
    */
    @GetMapping("/detail")
    public Result detail(@RequestParam Long roleId) {
        SysRoleMenu sysRoleMenu = sysRoleMenuService.selectSysRoleMenuById(roleId);
        return Result.success(sysRoleMenu);
    }

    /**
    * 条件查询所有
    */
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysRoleMenu sysRoleMenu) {
        //PageHelper.startPage(page, size);
        List<SysRoleMenu> list = sysRoleMenuService.selectSysRoleMenuList(sysRoleMenu);
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
    public PageDataInfo list(SysRoleMenu sysRoleMenu) {
        startPage();
        List<SysRoleMenu> list = sysRoleMenuService.selectSysRoleMenuList(sysRoleMenu);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 删除系统用户角色关系表
     */
    @Log(title = "删除用户和角色关系",businessType = BusinessType.DELETE)
    @GetMapping("/deleteRoleMenu")
    public Result removeRoleMenu(SysRoleMenu sysRoleMenu) {
        int count=sysRoleMenuService.deleteSysRoleMenu(sysRoleMenu);
        if(count>0){
            LoginUser loginUser = sysUserTokenService.getLoginUser(ServletUtil.getRequest());
            SysUser sysUser = loginUser.getSysUser();
            Set sysUserPerms = sysUserService.getSysUserMenuPerms(sysUser.getUserId());
            loginUser.setSysMenuList(sysUserPerms);
            return Result.success();
        }else {
            return Result.error();
        }
    }

    /**
     * 删除系统用户角色关系表
     */
    @Log(title = "批量删除用户和角色关系",businessType = BusinessType.DELETE)
    @PutMapping("/batchDeleteRoleMenu")
    public Result removeRoleMenuList(@RequestBody List<SysRoleMenu> sysRoleMenuList) {
        int count=sysRoleMenuService.batchDeleteSysRoleMenu(sysRoleMenuList);
        if(count>0) {
            LoginUser loginUser = sysUserTokenService.getLoginUser(ServletUtil.getRequest());
            SysUser sysUser = loginUser.getSysUser();
            Set sysUserPerms = sysUserService.getSysUserMenuPerms(sysUser.getUserId());
            loginUser.setSysMenuList(sysUserPerms);
            return Result.success();
        }else {
            return Result.error();
        }
    }
}
