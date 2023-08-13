package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysRolePermission;
//import com.geekplus.core.Service;
import java.util.List;


/**
 * 用户角色权限关系表 用户角色权限关系表
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysRolePermissionService {

    /**
    * 增加
    * @param sysRolePermission
    * @return 用户角色权限关系表
    */
    public Integer insertSysRolePermission(SysRolePermission sysRolePermission);

    /**
    * 批量增加
    * @param sysRolePermissionList
    * @return 用户角色权限关系表
    */
    public Integer batchInsertSysRolePermissionList(List<SysRolePermission> sysRolePermissionList);

    /**
    * 删除
    * @param roleId
    */
    public Integer deleteSysRolePermissionById(Long roleId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysRolePermissionByIds(Long[] roleIds);

    /**
    * 修改
    * @param sysRolePermission
    */
    public Integer updateSysRolePermission(SysRolePermission sysRolePermission);

    /**
    * 批量修改
    * @param roleIds
    */
    public Integer batchUpdateSysRolePermissionList(Long[] roleIds);

    /**
    * 查询全部
    */
    public List<SysRolePermission> selectSysRolePermissionList(SysRolePermission sysRolePermission);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysRolePermission> selectUnionSysRolePermissionList(SysRolePermission sysRolePermission);

    /**
    * 根据Id查询单条数据
    */
    public SysRolePermission selectSysRolePermissionById(Long roleId);
}
