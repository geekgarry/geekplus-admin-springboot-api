package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.annotation.RepeatSubmit;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.enums.OperatorType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysUser;
import com.geekplus.webapp.system.service.SysMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统菜单权限 系统菜单权限
 * Created by CodeGenerator on 2023/06/18.
 */
@Slf4j
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 增加 系统菜单权限
     */
    @RequiresPermissions("system:menu:add")
    @Log(title = "新增菜单权限",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE)
    @PostMapping("/add")
    @RepeatSubmit
    public Result add(@RequestBody SysMenu sysMenu) {
        return toResult(sysMenuService.insertSysMenu(sysMenu));
    }

    /**
     * 删除 系统菜单权限
     */
    @RequiresPermissions("system:menu:delete")
    @Log(title = "删除菜单权限",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @GetMapping("/delete")
    public Result remove(@RequestParam Long menuId) {
        return toResult(sysMenuService.deleteSysMenuById(menuId));
    }

    /**
     * 批量删除 系统菜单权限
     */
    @RequiresPermissions("system:menu:delete")
    @Log(title = "批量删除菜单权限",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @DeleteMapping("/{menuIds}")
    public Result remove(@PathVariable Long[] menuIds) {
        return toResult(sysMenuService.deleteSysMenuByIds(menuIds));
    }

    /**
     * 更新 系统菜单权限
     */
    @RequiresPermissions("system:menu:update")
    @Log(title = "修改菜单权限",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE)
    @PostMapping("/update")
    public Result edit(@RequestBody SysMenu sysMenu) {
        return toResult(sysMenuService.updateSysMenu(sysMenu));
    }

    /**
     * 单条数据详情 系统菜单权限
     */
    @RequiresPermissions("system:menu:detail")
    @GetMapping("/detail")
    public Result detail(@RequestParam Long menuId) {
        SysMenu sysMenu = sysMenuService.selectSysMenuById(menuId);
        return Result.success(sysMenu);
    }

    /**
     * 条件查询所有 系统菜单权限
     */
    @RequiresPermissions("system:menu:listAll")
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysMenu sysMenu) {
        //PageHelper.startPage(page, size);
        List<SysMenu> list = sysMenuService.selectSysMenuList(sysMenu);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(HttpStatusCode.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
     * 条件查询所有 系统菜单权限
     */
    @RequiresPermissions("system:menu:list")
    @GetMapping("/list")
    public PageDataInfo list(SysMenu sysMenu) {
        startPage();
        List<SysMenu> list = sysMenuService.selectSysMenuList(sysMenu);
        //PageInfo pageInfo = new PageInfo(list);
        return getDataTable(list);
    }

    @GetMapping("/getMenu")
    public Result getMenuList(){
//        SysUser sysUser= (SysUser) SecurityUtils.getSubject().getPrincipal();
//        List<SysRole> roleList=sysRoleService.getRolesByUserId(sysUser.getUserId().toString());
//        List<SysMenu> menuList=sysMenuService.getSysMenuByRoles(roleList);
        PageHelper.startPage(1,10);
        SysUser sysUser= (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser==null||sysUser.equals("")){
            return Result.error(ApiExceptionEnum.NO_USER_ID);
        }
        log.info("=========================>"+sysUser.getUserId());
        List<SysMenu> menuList=sysMenuService.getMenuTreeByUserId(sysUser.getUserId());
        PageInfo<SysMenu> pageInfo=new PageInfo<>(menuList);
        return Result.success(pageInfo);
    }

    /**
     * @Author geekplus
     * @Description //获取用户的菜单权限选择树
     * @Param
     * @Throws
     * @Return {@link }
     */
    @GetMapping("/getMenuTreeSelected")
    public Result getMenuTreeSelected(@RequestParam Long roleId){
        Map<String,Object> map=new HashMap<>();
        List<SysMenu> sysMenuList=sysMenuService.selectSysMenuTreeList();
        List<Integer> menuIdList=sysMenuService.getMenuIdListByRoleId(roleId);
        map.put("menus",sysMenuList);
        map.put("checkedKeys",menuIdList);
        return Result.success(map);
    }

    /**
     * @Author geekplus
     * @Description //查询所有菜单权限ID
     * @Param
     * @Throws
     * @Return {@link }
     */
    @GetMapping("/getAllMenuIdList")
    public Result getAllMenuIdList(){
        List<Integer> menuIdList=sysMenuService.getMenuIdListByRoleId(null);
        return Result.success(menuIdList);
    }

    @GetMapping("/getMenuByUserId")
    public Result getMenuListByUserId(@RequestParam Long userId){
        return Result.success(sysMenuService.getMenuTreeByUserId(userId));
    }

    @GetMapping("/getMenuByRoleId")
    public Result getMenuListByRoleId(@RequestParam Long roleId){
        return Result.success(sysMenuService.getMenuTreeByRoleId(roleId));
    }

    @GetMapping("/getMenuTreeList")
    public Result getMenuTreeList(){
        return Result.success(sysMenuService.selectSysMenuTreeList());
    }

    /**
     * 导出系统菜单权限
     */
    @RequiresPermissions("system:menu:export")
    @Log(title = "导出系统菜单权限表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(SysMenu sysMenu)
    {
        List<SysMenu> list = sysMenuService.selectSysMenuList(sysMenu);
        ExcelUtil<SysMenu> util = new ExcelUtil<SysMenu>(SysMenu.class);
        return util.exportExcel(list, "sysMenu");
    }
}
