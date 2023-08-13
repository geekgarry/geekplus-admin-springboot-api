package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysRoleMenu;
//import com.geekplus.core.Service;
import java.util.List;


/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysRoleMenuService {

    /**
    * 增加
    * @param sysRoleMenu
    * @return
    */
    public Integer insertSysRoleMenu(SysRoleMenu sysRoleMenu);

    /**
    * 批量增加
    * @param sysRoleMenuList
    * @return
    */
    public Integer batchInsertSysRoleMenuList(List<SysRoleMenu> sysRoleMenuList);

    /**
    * 删除
    * @param roleId
    */
    public Integer deleteSysRoleMenuById(Long roleId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysRoleMenuByIds(Long[] roleIds);

    /**
    * 修改
    * @param sysRoleMenu
    */
    public Integer updateSysRoleMenu(SysRoleMenu sysRoleMenu);

    /**
    * 批量修改
    * @param roleIds
    */
    public Integer batchUpdateSysRoleMenuList(Long[] roleIds);

    /**
    * 查询全部
    */
    public List<SysRoleMenu> selectSysRoleMenuList(SysRoleMenu sysRoleMenu);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysRoleMenu> selectUnionSysRoleMenuList(SysRoleMenu sysRoleMenu);

    /**
    * 根据Id查询单条数据
    */
    public SysRoleMenu selectSysRoleMenuById(Long roleId);

    /**
     * 普通删除角色菜单权限
     */
    public Integer deleteSysRoleMenu(SysRoleMenu sysRoleMenu);

    /**
     * 批量删除角色菜单权限
     */
    public Integer batchDeleteSysRoleMenu(List<SysRoleMenu> roleMenuList);
}
