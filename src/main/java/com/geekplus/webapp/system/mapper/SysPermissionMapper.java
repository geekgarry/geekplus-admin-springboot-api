package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysPermission;
import java.util.List;

/**
 * 系统权限表 系统权限表
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysPermissionMapper {

    /**
    * 增加
    * @param sysPermission
    * @return 系统权限表
    */
    Integer insertSysPermission(SysPermission sysPermission);

    /**
    * 批量增加
    * @param sysPermissionList
    * @return
    */
    public int batchInsertSysPermissionList(List<SysPermission> sysPermissionList);

    /**
    * 删除
    * @param permissionId
    */
    Integer deleteSysPermissionById(Long permissionId);

    /**
    * 批量删除
    */
    Integer deleteSysPermissionByIds(Long[] permissionIds);

    /**
    * 修改
    * @param sysPermission
    */
    Integer updateSysPermission(SysPermission sysPermission);

    /**
    * 批量修改魔偶几个字段
    * @param permissionIds
    */
    Integer batchUpdateSysPermissionList(Long[] permissionIds);

    /**
    * 查询全部
    */
    List<SysPermission> selectSysPermissionList(SysPermission sysPermission);

    /**
    * 查询全部,联合查询使用
    */
    List<SysPermission> selectUnionSysPermissionList(SysPermission sysPermission);

    /**
    * 根据Id查询单条数据
    */
    SysPermission selectSysPermissionById(Long permissionId);
}
