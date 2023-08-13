package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysUserRole;
import java.util.List;

/**
 * 系统用户角色关系表 系统用户角色关系表
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysUserRoleMapper {

    /**
    * 增加
    * @param sysUserRole
    * @return 系统用户角色关系表
    */
    Integer insertSysUserRole(SysUserRole sysUserRole);

    /**
    * 批量增加
    * @param sysUserRoleList
    * @return
    */
    public int batchInsertSysUserRoleList(List<SysUserRole> sysUserRoleList);

    /**
    * 删除
    * @param userId
    */
    Integer deleteSysUserRoleById(Long userId);

    /**
    * 批量删除
    */
    Integer deleteSysUserRoleByIds(Long[] userIds);

    /**
    * 修改
    * @param sysUserRole
    */
    Integer updateSysUserRole(SysUserRole sysUserRole);

    /**
    * 批量修改魔偶几个字段
    * @param userIds
    */
    Integer batchUpdateSysUserRoleList(Long[] userIds);

    /**
    * 查询全部
    */
    List<SysUserRole> selectSysUserRoleList(SysUserRole sysUserRole);

    /**
    * 查询全部,联合查询使用
    */
    List<SysUserRole> selectUnionSysUserRoleList(SysUserRole sysUserRole);

    /**
    * 根据Id查询单条数据
    */
    SysUserRole selectSysUserRoleById(Long userId);

    Integer deleteSysUserRole(SysUserRole sysUserRole);

    Integer batchDeleteSysUserRole(List<SysUserRole> sysUserRoleList);
}
