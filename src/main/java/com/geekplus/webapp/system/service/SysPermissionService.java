package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysPermission;
//import com.geekplus.core.Service;
import java.util.List;


/**
 * 系统权限表 系统权限表
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysPermissionService {

    /**
    * 增加
    * @param sysPermission
    * @return 系统权限表
    */
    public Integer insertSysPermission(SysPermission sysPermission);

    /**
    * 批量增加
    * @param sysPermissionList
    * @return 系统权限表
    */
    public Integer batchInsertSysPermissionList(List<SysPermission> sysPermissionList);

    /**
    * 删除
    * @param permissionId
    */
    public Integer deleteSysPermissionById(Long permissionId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysPermissionByIds(Long[] permissionIds);

    /**
    * 修改
    * @param sysPermission
    */
    public Integer updateSysPermission(SysPermission sysPermission);

    /**
    * 批量修改
    * @param permissionIds
    */
    public Integer batchUpdateSysPermissionList(Long[] permissionIds);

    /**
    * 查询全部
    */
    public List<SysPermission> selectSysPermissionList(SysPermission sysPermission);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysPermission> selectUnionSysPermissionList(SysPermission sysPermission);

    /**
    * 根据Id查询单条数据
    */
    public SysPermission selectSysPermissionById(Long permissionId);
}
