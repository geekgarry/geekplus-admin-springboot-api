package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysRole;

import java.util.List;

/**
 * 系统菜单权限 系统菜单权限
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysMenuMapper {

    /**
    * 增加
    * @param sysMenu
    * @return 系统菜单权限
    */
    Integer insertSysMenu(SysMenu sysMenu);

    /**
    * 批量增加
    * @param sysMenuList
    * @return
    */
    public int batchInsertSysMenuList(List<SysMenu> sysMenuList);

    /**
    * 删除
    * @param menuId
    */
    Integer deleteSysMenuById(Long menuId);

    /**
    * 批量删除
    */
    Integer deleteSysMenuByIds(Long[] menuIds);

    /**
    * 修改
    * @param sysMenu
    */
    Integer updateSysMenu(SysMenu sysMenu);

    /**
    * 批量修改魔偶几个字段
    * @param menuIds
    */
    Integer batchUpdateSysMenuList(Long[] menuIds);

    /**
    * 查询全部
    */
    List<SysMenu> selectSysMenuList(SysMenu sysMenu);

    /**
      * @Author geekplus
      * @Description //查询所以菜单，树形结构
      * @Param
      * @Throws
      * @Return {@link }
      */
    List<SysMenu> selectSysMenuTreeList();

    /**
    * 查询全部,联合查询使用
    */
    List<SysMenu> selectUnionSysMenuList(SysMenu sysMenu);

    /**
    * 根据Id查询单条数据
    */
    SysMenu selectSysMenuById(Long menuId);

    //根据用户的角色查询菜单权限permission列表
    List<SysMenu> selectMenuPermsByRoles(List<SysRole> sysRoles);

    //根据用户名查询用户菜单权限permission列表
    List<SysMenu> selectMenuPermsByUsername(String userName);

    //根据用户ID查询用户菜单权限permission列表
    List<SysMenu> selectMenuPermsByUserId(Long userId);

    //根据userName查询权限菜单
    List<SysMenu> selectMenuTreeByUsername(String userName);

    //这个是根据userId查询该用户的权限菜单列表，跟上面的比起来是把所有步骤都写在一个sql语句中
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    //根据角色关键字数组查询菜单列表
    List<SysMenu> selectMenuTreeByRoleKeys(String[] roleKeys);

    //根据角色ID查询菜单列表
    List<SysMenu> selectMenuTreeByRoleId(Long roleId);

    //根据角色ID查询菜单权限permission列表
    List<Integer> selectMenuIdListByRoleId(Long roleId);
}
