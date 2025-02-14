package com.geekplus.webapp.system.mapper;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.framework.domain.server.Sys;
import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.entity.SysUser;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * 系统用户表 系统用户表
 * Created by CodeGenerator on 2023/06/18.
 */

public interface SysUserMapper {

    /**
    * 增加
    * @param sysUser
    * @return 系统用户表
    */
    Integer insertSysUser(SysUser sysUser);

    /**
    * 批量增加
    * @param sysUserList
    * @return
    */
    public int batchInsertSysUserList(List<SysUser> sysUserList);

    /**
    * 删除
    * @param userId
    */
    Integer deleteSysUserById(Long userId);

    /**
    * 批量删除
    */
    Integer deleteSysUserByIds(Long[] userIds);

    /**
    * 修改
    * @param sysUser
    */
    Integer updateSysUser(SysUser sysUser);

    Integer updateSysUserByUsername(SysUser sysUser);

    /**
    * 批量修改魔偶几个字段
    * @param userIds
    */
    Integer batchUpdateSysUserList(Long[] userIds);

    /**
    * 查询全部
    */
    List<SysUser> selectSysUserList(SysUser sysUser);

    /**
     * 查询全部
     */
    SysUser selectSysUserByPassword(SysUser sysUser);

    /**
    * 查询全部,联合查询使用
    */
    List<SysUser> selectUnionSysUserList(SysUser sysUser);

    /**
    * 根据Id查询单条数据
    */
    SysUser selectSysUserById(Long userId);

    /** 根据用户名查询用户的角色和菜单*/
    //List<Map<String,Object>> getRoleMenuByUsername(SysUser sysUser);

    //用户信息和角色获取
    SysUser sysUserLoginBy(String userName);

    //用户信息和角色
    SysUser getSysUserInfoBy(String userName);

    SysUser getSysUserInfoByPhone(String userName);

    SysUser getSysUserInfoByEmail(String userName);

    List<SysRole> selectUserRoles(Long userId);

    List<SysMenu> selectUserMenus(Long userId);

    Integer updateUserAvatar(SysUser sysUser);
}
