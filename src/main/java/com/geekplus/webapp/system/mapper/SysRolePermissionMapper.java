package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysRolePermission;
import java.util.List;

/**
 * 用户角色权限关系表 用户角色权限关系表
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysRolePermissionMapper {

    /**
    * 增加
    * @param sysRolePermission
    * @return 用户角色权限关系表
    */
    Integer insertSysRolePermission(SysRolePermission sysRolePermission);

    /**
    * 批量增加
    * @param sysRolePermissionList
    * @return
    */
    public int batchInsertSysRolePermissionList(List<SysRolePermission> sysRolePermissionList);

    /**
    * 删除
    * @param roleId
    */
    Integer deleteSysRolePermissionById(Long roleId);

    /**
    * 批量删除
    */
    Integer deleteSysRolePermissionByIds(Long[] roleIds);

    /**
    * 修改
    * @param sysRolePermission
    */
    Integer updateSysRolePermission(SysRolePermission sysRolePermission);

    /**
    * 批量修改魔偶几个字段
    * @param roleIds
    */
    Integer batchUpdateSysRolePermissionList(Long[] roleIds);

    /**
    * 查询全部
    */
    List<SysRolePermission> selectSysRolePermissionList(SysRolePermission sysRolePermission);

    /**
    * 查询全部,联合查询使用
    */
    List<SysRolePermission> selectUnionSysRolePermissionList(SysRolePermission sysRolePermission);

    /**
    * 根据Id查询单条数据
    */
    SysRolePermission selectSysRolePermissionById(Long roleId);
}
