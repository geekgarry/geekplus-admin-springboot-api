package com.geekplus.webapp.system.service;

import com.geekplus.webapp.system.entity.SysUserRole;
//import com.geekplus.core.Service;
import java.util.List;


/**
 * 系统用户角色关系表 系统用户角色关系表
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysUserRoleService {

    /**
    * 增加
    * @param sysUserRole
    * @return 系统用户角色关系表
    */
    public Integer insertSysUserRole(SysUserRole sysUserRole);

    /**
    * 批量增加
    * @param sysUserRoleList
    * @return 系统用户角色关系表
    */
    public Integer batchInsertSysUserRoleList(List<SysUserRole> sysUserRoleList);

    /**
    * 删除
    * @param userId
    */
    public Integer deleteSysUserRoleById(Long userId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysUserRoleByIds(Long[] userIds);

    /**
    * 修改
    * @param sysUserRole
    */
    public Integer updateSysUserRole(SysUserRole sysUserRole);

    /**
    * 批量修改
    * @param userIds
    */
    public Integer batchUpdateSysUserRoleList(Long[] userIds);

    /**
    * 查询全部
    */
    public List<SysUserRole> selectSysUserRoleList(SysUserRole sysUserRole);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysUserRole> selectUnionSysUserRoleList(SysUserRole sysUserRole);

    /**
    * 根据Id查询单条数据
    */
    public SysUserRole selectSysUserRoleById(Long userId);

    //删除用户角色
    public Integer deleteSysUserRole(SysUserRole sysUserRole);

    //删除用户角色
    public Integer batchDeleteSysUserRole(List<SysUserRole> sysUserRoleList);
}
