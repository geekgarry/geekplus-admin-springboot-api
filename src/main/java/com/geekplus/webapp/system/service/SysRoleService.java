package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysRole;
//import com.geekplus.core.Service;
import java.util.List;


/**
 * 系统角色表 系统角色表
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysRoleService {

    /**
    * 增加
    * @param sysRole
    * @return 系统角色表
    */
    public Integer insertSysRole(SysRole sysRole);

    /**
    * 批量增加
    * @param sysRoleList
    * @return 系统角色表
    */
    public Integer batchInsertSysRoleList(List<SysRole> sysRoleList);

    /**
    * 删除
    * @param roleId
    */
    public Integer deleteSysRoleById(Long roleId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysRoleByIds(Long[] roleIds);

    /**
    * 修改
    * @param sysRole
    */
    public Integer updateSysRole(SysRole sysRole);

    /**
    * 批量修改
    * @param roleIds
    */
    public Integer batchUpdateSysRoleList(Long[] roleIds);

    /**
    * 查询全部
    */
    public List<SysRole> selectSysRoleList(SysRole sysRole);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysRole> selectUnionSysRoleList(SysRole sysRole);

    /**
    * 根据Id查询单条数据
    */
    public SysRole selectSysRoleById(Long roleId);

    public SysRole selectSysRoleAndMenuTreeById(Long roleId);

    List<SysRole> getRolesByUserId(String userId);
    List<SysRole> getRolesByUserName(String userName);
}
