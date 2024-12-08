package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysRoleMapper;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.service.SysRoleService;
//import com.geekplus.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2023/06/18.
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
    * 增加
    * @param sysRole
    * @return 系统角色表
    */
    public Integer insertSysRole(SysRole sysRole){
        return sysRoleMapper.insertSysRole(sysRole);
    }

    /**
    * 批量增加
    * @param sysRoleList
    * @return 系统角色表
    */
    public Integer batchInsertSysRoleList(List<SysRole> sysRoleList){
        return sysRoleMapper.batchInsertSysRoleList(sysRoleList);
    }

    /**
    * 删除
    * @param roleId
    */
    public Integer deleteSysRoleById(Long roleId){
        return sysRoleMapper.deleteSysRoleById(roleId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysRoleByIds(Long[] roleIds){
        return sysRoleMapper.deleteSysRoleByIds(roleIds);
    }

    /**
    * 修改
    * @param sysRole
    */
    public Integer updateSysRole(SysRole sysRole){
        return sysRoleMapper.updateSysRole(sysRole);
    }

    /**
    * 批量修改某几个字段
    * @param roleIds
    */
    public Integer batchUpdateSysRoleList(Long[] roleIds){
        return sysRoleMapper.batchUpdateSysRoleList(roleIds);
    }

    /**
    * 查询全部
    */
    public List<SysRole> selectSysRoleList(SysRole sysRole){
        return sysRoleMapper.selectSysRoleList(sysRole);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysRole> selectUnionSysRoleList(SysRole sysRole){
        return sysRoleMapper.selectUnionSysRoleList(sysRole);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysRole selectSysRoleById(Long roleId){
        return sysRoleMapper.selectSysRoleById(roleId);
    }

    @Override
    public SysRole selectSysRoleAndMenuTreeById(Long roleId) {
        return sysRoleMapper.selectSysRoleAndMenuTreeById(roleId);
    }

    @Override
    public List<SysRole> getRolesByUserId(String userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    public List<SysRole> getRolesByUserName(String userName) {
        return sysRoleMapper.selectRolesByUserName(userName);
    }
}
