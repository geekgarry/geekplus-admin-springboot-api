package com.geekplus.webapp.system.service;
import com.geekplus.webapp.system.entity.SysUser;
//import com.geekplus.core.Service;
import java.util.List;
import java.util.Set;


/**
 * 系统用户表 系统用户表
 * Created by CodeGenerator on 2023/06/18.
 */
public interface SysUserService {

    /**
    * 增加
    * @param sysUser
    * @return 系统用户表
    */
    public Integer insertSysUser(SysUser sysUser);

    /**
     * 增加
     * @param sysUser
     * @return 系统用户表
     */
    public Integer insertSysUserEnCodePwd(SysUser sysUser);

    /**
    * 批量增加
    * @param sysUserList
    * @return 系统用户表
    */
    public Integer batchInsertSysUserList(List<SysUser> sysUserList);

    /**
    * 删除
    * @param userId
    */
    public Integer deleteSysUserById(Long userId);

    /**
    * 批量删除某几个字段
    */
    public Integer deleteSysUserByIds(Long[] userIds);

    /**
    * 修改
    * @param sysUser
    */
    public Integer updateSysUser(SysUser sysUser);

    /**
     * 修改密码
     * @param sysUser
     */
    public Integer updateSysUserPwd(SysUser sysUser);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
    * 批量修改
    * @param userIds
    */
    public Integer batchUpdateSysUserList(Long[] userIds);

    /**
    * 查询全部
    */
    public List<SysUser> selectSysUserList(SysUser sysUser);

    //通过密码和用户id查询
    SysUser selectSysUserByPassword(SysUser sysUser);

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<SysUser> selectUnionSysUserList(SysUser sysUser);

    /**
    * 根据Id查询单条数据
    */
    public SysUser selectSysUserById(Long userId);

    public SysUser sysUserLoginBy(String userName);

    public SysUser getSysUserInfoBy(String userName);

    Set<String> getSysUserMenuPerms(Long userId);

    public int updateSysUserByUsername(String userName, String loginIp);
}
