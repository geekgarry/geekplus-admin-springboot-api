package com.geekplus.webapp.system.mapper;

import com.geekplus.webapp.system.entity.SysRole;
import java.util.List;

/**
 * 系统角色表 系统角色表
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysRoleMapper {

    /**
    * 增加
    * @param sysRole
    * @return 系统角色表
    */
    Integer insertSysRole(SysRole sysRole);

    /**
    * 批量增加
    * @param sysRoleList
    * @return
    */
    public int batchInsertSysRoleList(List<SysRole> sysRoleList);

    /**
    * 删除
    * @param roleId
    */
    Integer deleteSysRoleById(Long roleId);

    /**
    * 批量删除
    */
    Integer deleteSysRoleByIds(Long[] roleIds);

    /**
    * 修改
    * @param sysRole
    */
    Integer updateSysRole(SysRole sysRole);

    /**
    * 批量修改魔偶几个字段
    * @param roleIds
    */
    Integer batchUpdateSysRoleList(Long[] roleIds);

    /**
    * 查询全部
    */
    List<SysRole> selectSysRoleList(SysRole sysRole);

    /**
    * 查询全部,联合查询使用
    */
    List<SysRole> selectUnionSysRoleList(SysRole sysRole);

    /**
    * 根据Id查询单条数据
    */
    SysRole selectSysRoleById(Long roleId);

    SysRole selectSysRoleAndMenuTreeById(Long roleId);

    List<SysRole> selectRolesByUserId(String userId);

    List<SysRole> selectRolesByUserName(String userName);
}
