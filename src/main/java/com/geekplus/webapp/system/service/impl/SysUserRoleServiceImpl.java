package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysUserRoleMapper;
import com.geekplus.webapp.system.entity.SysUserRole;
import com.geekplus.webapp.system.service.SysUserRoleService;
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
public class SysUserRoleServiceImpl implements SysUserRoleService {
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
    * 增加
    * @param sysUserRole
    * @return 系统用户角色关系表
    */
    public Integer insertSysUserRole(SysUserRole sysUserRole){
        return sysUserRoleMapper.insertSysUserRole(sysUserRole);
    }

    /**
    * 批量增加
    * @param sysUserRoleList
    * @return 系统用户角色关系表
    */
    public Integer batchInsertSysUserRoleList(List<SysUserRole> sysUserRoleList){
        return sysUserRoleMapper.batchInsertSysUserRoleList(sysUserRoleList);
    }

    /**
    * 删除
    * @param userId
    */
    public Integer deleteSysUserRoleById(Long userId){
        return sysUserRoleMapper.deleteSysUserRoleById(userId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysUserRoleByIds(Long[] userIds){
        return sysUserRoleMapper.deleteSysUserRoleByIds(userIds);
    }

    /**
    * 修改
    * @param sysUserRole
    */
    public Integer updateSysUserRole(SysUserRole sysUserRole){
        return sysUserRoleMapper.updateSysUserRole(sysUserRole);
    }

    /**
    * 批量修改某几个字段
    * @param userIds
    */
    public Integer batchUpdateSysUserRoleList(Long[] userIds){
        return sysUserRoleMapper.batchUpdateSysUserRoleList(userIds);
    }

    /**
    * 查询全部
    */
    public List<SysUserRole> selectSysUserRoleList(SysUserRole sysUserRole){
        return sysUserRoleMapper.selectSysUserRoleList(sysUserRole);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysUserRole> selectUnionSysUserRoleList(SysUserRole sysUserRole){
        return sysUserRoleMapper.selectUnionSysUserRoleList(sysUserRole);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysUserRole selectSysUserRoleById(Long userId){
        return sysUserRoleMapper.selectSysUserRoleById(userId);
    }

    //删除用户角色
    @Override
    public Integer deleteSysUserRole(SysUserRole sysUserRole) {
        return sysUserRoleMapper.deleteSysUserRole(sysUserRole);
    }

    @Override
    public Integer batchDeleteSysUserRole(List<SysUserRole> sysUserRoleList) {
        return sysUserRoleMapper.batchDeleteSysUserRole(sysUserRoleList);
    }
}
