package com.geekplus.webapp.system.service.impl;

import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.util.encrypt.EncryptUtil;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.mapper.SysUserMapper;
import com.geekplus.webapp.system.entity.SysUser;
import com.geekplus.webapp.system.service.SysUserService;
//import com.geekplus.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2023/06/18.
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    /**
    * 增加
    * @param sysUser
    * @return 系统用户表
    */
    @Override
    @Transactional
    public Integer insertSysUser(SysUser sysUser){
        boolean hasUserInfo=sysUserMapper.selectSysUserByPassword(sysUser)!=null?true:false;
        if(hasUserInfo){
            throw new BusinessException("当前用户名已经存在！");
        }
        return sysUserMapper.insertSysUser(sysUser);
    }

    @Override
    @Transactional
    public Integer insertSysUserEnCodePwd(SysUser sysUser) {
        sysUser.setPassword(EncryptUtil.md5EncryptPwd(sysUser.getPassword()));
        boolean hasUserInfo=sysUserMapper.selectSysUserByPassword(sysUser)!=null?true:false;
        if(hasUserInfo){
            throw new BusinessException("当前用户名已经存在！");
        }
        return sysUserMapper.insertSysUser(sysUser);
    }

    /**
    * 批量增加
    * @param sysUserList
    * @return 系统用户表
    */
    public Integer batchInsertSysUserList(List<SysUser> sysUserList){
        return sysUserMapper.batchInsertSysUserList(sysUserList);
    }

    /**
    * 删除
    * @param userId
    */
    public Integer deleteSysUserById(Long userId){
        return sysUserMapper.deleteSysUserById(userId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysUserByIds(Long[] userIds){
        return sysUserMapper.deleteSysUserByIds(userIds);
    }

    /**
    * 修改
    * @param sysUser
    */
    public Integer updateSysUser(SysUser sysUser){
        return sysUserMapper.updateSysUser(sysUser);
    }

    @Override
    public Integer updateSysUserPwd(SysUser sysUser) {
        sysUser.setPassword(EncryptUtil.md5EncryptPwd(sysUser.getPassword()));
        return sysUserMapper.updateSysUser(sysUser);
    }

    @Override
    @Transactional
    public boolean updateUserAvatar(String userName, String avatar) {
        SysUser sysUser=new SysUser();
        sysUser.setUsername(userName);
        sysUser.setAvatar(avatar);
        return sysUserMapper.updateUserAvatar(sysUser) > 0;
    }

    /**
    * 批量修改某几个字段
    * @param userIds
    */
    public Integer batchUpdateSysUserList(Long[] userIds){
        return sysUserMapper.batchUpdateSysUserList(userIds);
    }

    /**
    * 查询全部
    */
    public List<SysUser> selectSysUserList(SysUser sysUser){
        return sysUserMapper.selectSysUserList(sysUser);
    }

    @Override
    public SysUser selectSysUserByPassword(SysUser sysUser) {
        sysUser.setPassword(EncryptUtil.md5EncryptPwd(sysUser.getPassword()));
        return sysUserMapper.selectSysUserByPassword(sysUser);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysUser> selectUnionSysUserList(SysUser sysUser){
        return sysUserMapper.selectUnionSysUserList(sysUser);
    }

    /**
    * 根据Id查询单条数据
    */
    @Override
    public SysUser selectSysUserById(Long userId){
        return sysUserMapper.selectSysUserById(userId);
    }

    /**
    *通过username查询用户
     */
    @Override
    public SysUser sysUserLoginBy(String username) {
        return sysUserMapper.sysUserLoginBy(username);
    }

    @Override
    public SysUser getSysUserInfoBy(String username) {
        return sysUserMapper.getSysUserInfoBy(username);
    }

    @Override
    public Set<String> getSysUserMenuPerms(Long userId) {
        List<SysMenu> sysMenus = sysUserMapper.selectUserMenus(userId);
        return sysMenus.stream().filter(sysMenu -> !StringUtils.isEmpty(sysMenu.getPerms())).map(SysMenu::getPerms).collect(Collectors.toSet());
    }

    @Override
    public int updateSysUserByUsername(String userName, String loginIp) {
        SysUser sysUser=new SysUser();
        sysUser.setLoginTime(new Date());
        sysUser.setUsername(userName);
        sysUser.setLoginIp(loginIp);
        return sysUserMapper.updateSysUserByUsername(sysUser);
    }
}
