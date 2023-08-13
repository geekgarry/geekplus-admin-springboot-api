package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysRoleMenu;
import java.util.List;

/**
 *
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysRoleMenuMapper {

    /**
    * 增加
    * @param sysRoleMenu
    * @return
    */
    Integer insertSysRoleMenu(SysRoleMenu sysRoleMenu);

    /**
    * 批量增加
    * @param sysRoleMenuList
    * @return
    */
    public int batchInsertSysRoleMenuList(List<SysRoleMenu> sysRoleMenuList);

    /**
    * 删除
    * @param roleId
    */
    Integer deleteSysRoleMenuById(Long roleId);

    /**
    * 批量删除
    */
    Integer deleteSysRoleMenuByIds(Long[] roleIds);

    /**
    * 修改
    * @param sysRoleMenu
    */
    Integer updateSysRoleMenu(SysRoleMenu sysRoleMenu);

    /**
    * 批量修改魔偶几个字段
    * @param roleIds
    */
    Integer batchUpdateSysRoleMenuList(Long[] roleIds);

    /**
    * 查询全部
    */
    List<SysRoleMenu> selectSysRoleMenuList(SysRoleMenu sysRoleMenu);

    /**
    * 查询全部,联合查询使用
    */
    List<SysRoleMenu> selectUnionSysRoleMenuList(SysRoleMenu sysRoleMenu);

    /**
    * 根据Id查询单条数据
    */
    SysRoleMenu selectSysRoleMenuById(Long roleId);

    /**
      * 普通删除角色菜单权限
      */
    Integer deleteSysRoleMenu(SysRoleMenu sysRoleMenu);

    /**
     * 批量删除角色菜单权限
     */
    Integer batchDeleteSysRoleMenu(List<SysRoleMenu> sysRoleMenuList);
}
