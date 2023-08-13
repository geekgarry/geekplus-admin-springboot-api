package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysRole;
//import com.geekplus.core.Service;
import java.util.List;


/**
 * 系统菜单权限 系统菜单权限
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysMenuService {

    /**
    * 增加
    * @param sysMenu
    * @return 系统菜单权限
    */
    public Integer insertSysMenu(SysMenu sysMenu);

    /**
    * 批量增加
    * @param sysMenuList
    * @return 系统菜单权限
    */
    public Integer batchInsertSysMenuList(List<SysMenu> sysMenuList);

    /**
    * 删除
    * @param menuId
    */
    public Integer deleteSysMenuById(Long menuId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysMenuByIds(Long[] menuIds);

    /**
    * 修改
    * @param sysMenu
    */
    public Integer updateSysMenu(SysMenu sysMenu);

    /**
    * 批量修改
    * @param menuIds
    */
    public Integer batchUpdateSysMenuList(Long[] menuIds);

    /**
    * 查询全部
    */
    public List<SysMenu> selectSysMenuList(SysMenu sysMenu);

    //树形结构菜显示
    public List<SysMenu> selectSysMenuTreeList();

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysMenu> selectUnionSysMenuList(SysMenu sysMenu);

    /**
    * 根据Id查询单条数据
    */
    public SysMenu selectSysMenuById(Long menuId);

    /**
     *查询菜单权限列表
     */
    List<SysMenu> getSysMenuByRoles(List<SysRole> sysRoles);

    /**
      * @Author geekplus
      * @Description //根据userName查询菜单权限
      * @Param
      * @Throws
      * @Return {@link }
      */
    List<SysMenu> getMenuTreeByUserName(String userName);

    /**
    *查询菜单权限树
    */
    List<SysMenu> getMenuTreeByUserId(Long userId);

    /**
    *查询菜单权限树
    */
    List<SysMenu> getMenuTreeByRoleId(Long roleId);

    /**
    *查询菜单权限列表
     */
    List<SysMenu> getMenuListByRoleId(Long roleId);

    /**
    *查询菜单ID列表
     */
    List<Integer> getMenuIdListByRoleId(Long roleId);
}
