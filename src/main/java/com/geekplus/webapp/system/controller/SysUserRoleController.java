package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.annotation.RepeatSubmit;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.enums.OperatorType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.webapp.common.service.SysUserTokenService;
import com.geekplus.webapp.system.entity.SysUser;
import com.geekplus.webapp.system.entity.SysUserRole;
import com.geekplus.webapp.system.service.SysUserRoleService;
import com.geekplus.webapp.system.service.SysUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统用户角色关系表 系统用户角色关系表
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/userRole")
public class SysUserRoleController extends BaseController {
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysUserTokenService sysUserTokenService;

    /**
     * 增加 系统用户角色关系表
     */
    @Log(title = "添加用户和角色",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE)
    @PostMapping("/add")
    @RepeatSubmit
    public Result add(@RequestBody SysUserRole sysUserRole) {
        int count = sysUserRoleService.insertSysUserRole(sysUserRole);
        if(count>0){
            LoginUser loginUser = sysUserTokenService.getLoginUser(ServletUtil.getRequest());
            SysUser sysUser = sysUserService.getSysUserInfoBy(sysUserTokenService.getSysUserName());
            loginUser.setSysUser(sysUser);
            return Result.success();
        }else {
            return Result.error();
        }
    }

    /**
     * 增加 系统用户角色关系表
     */
    @Log(title = "添加用户和角色",businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    @RepeatSubmit
    public Result batchAdd(@RequestBody List<SysUserRole> sysUserRole) {
        int count = sysUserRoleService.batchInsertSysUserRoleList(sysUserRole);
        if(count>0){
            LoginUser loginUser = sysUserTokenService.getLoginUser(ServletUtil.getRequest());
            SysUser sysUser = sysUserService.getSysUserInfoBy(sysUserTokenService.getSysUserName());
            loginUser.setSysUser(sysUser);
            return Result.success();
        }else {
            return Result.error();
        }
    }

    /**
     * 删除 系统用户角色关系表
     */
    @Log(title = "删除用户和角色",businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    public Result remove(@RequestParam Long userId) {
        return toResult(sysUserRoleService.deleteSysUserRoleById(userId));
    }

    /**
     * 批量删除 系统用户角色关系表
     */
    @Log(title = "批量删除用户和角色",businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public Result remove(@PathVariable Long[] userIds) {
        return toResult(sysUserRoleService.deleteSysUserRoleByIds(userIds));
    }

    /**
     * 更新 系统用户角色关系表
     */
    @Log(title = "更新用户和角色",businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public Result edit(@RequestBody SysUserRole sysUserRole) {
        return toResult(sysUserRoleService.updateSysUserRole(sysUserRole));
    }

    /**
     * 单条数据详情 系统用户角色关系表
     */
    @GetMapping("/detail")
    public Result detail(@RequestParam Long userId) {
        SysUserRole sysUserRole = sysUserRoleService.selectSysUserRoleById(userId);
        return Result.success(sysUserRole);
    }

    /**
    * 条件查询所有 系统用户角色关系表
    */
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysUserRole sysUserRole) {
        //PageHelper.startPage(page, size);
        List<SysUserRole> list = sysUserRoleService.selectSysUserRoleList(sysUserRole);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(HttpStatusCode.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
    * 条件查询所有 系统用户角色关系表
    */
    @GetMapping("/list")
    public PageDataInfo list(SysUserRole sysUserRole) {
        startPage();
        List<SysUserRole> list = sysUserRoleService.selectSysUserRoleList(sysUserRole);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    /**
     * 删除系统用户角色关系表
     */
    @Log(title = "删除用户和角色",businessType = BusinessType.DELETE)
    @GetMapping("/deleteUserRole")
    public Result removeUserRole(SysUserRole sysUserRole) {
        int count=sysUserRoleService.deleteSysUserRole(sysUserRole);
        if(count>0){
            LoginUser loginUser = sysUserTokenService.getLoginUser(ServletUtil.getRequest());
            SysUser sysUser = sysUserService.getSysUserInfoBy(sysUserTokenService.getSysUserName());
            loginUser.setSysUser(sysUser);
            return Result.success();
        }else {
            return Result.error();
        }
    }

    /**
     * 删除系统用户角色关系表
     */
    @Log(title = "批量删除用户和角色",businessType = BusinessType.DELETE)
    @PutMapping("/batchDeleteUserRole")
    public Result removeUserRoleList(@RequestBody List<SysUserRole> sysUserRoleList) {
        int count=sysUserRoleService.batchDeleteSysUserRole(sysUserRoleList);
        if(count>0){
            LoginUser loginUser = sysUserTokenService.getLoginUser(ServletUtil.getRequest());
            SysUser sysUser = sysUserService.getSysUserInfoBy(sysUserTokenService.getSysUserName());
            loginUser.setSysUser(sysUser);
            return Result.success();
        }else {
            return Result.error();
        }
    }
}
