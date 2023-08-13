package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysRolePermissionMapper;
import com.geekplus.webapp.system.entity.SysRolePermission;
import com.geekplus.webapp.system.service.SysRolePermissionService;
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
public class SysRolePermissionServiceImpl implements SysRolePermissionService {
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
    * 增加
    * @param sysRolePermission
    * @return 用户角色权限关系表
    */
    public Integer insertSysRolePermission(SysRolePermission sysRolePermission){
        return sysRolePermissionMapper.insertSysRolePermission(sysRolePermission);
    }

    /**
    * 批量增加
    * @param sysRolePermissionList
    * @return 用户角色权限关系表
    */
    public Integer batchInsertSysRolePermissionList(List<SysRolePermission> sysRolePermissionList){
        return sysRolePermissionMapper.batchInsertSysRolePermissionList(sysRolePermissionList);
    }

    /**
    * 删除
    * @param roleId
    */
    public Integer deleteSysRolePermissionById(Long roleId){
        return sysRolePermissionMapper.deleteSysRolePermissionById(roleId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysRolePermissionByIds(Long[] roleIds){
        return sysRolePermissionMapper.deleteSysRolePermissionByIds(roleIds);
    }

    /**
    * 修改
    * @param sysRolePermission
    */
    public Integer updateSysRolePermission(SysRolePermission sysRolePermission){
        return sysRolePermissionMapper.updateSysRolePermission(sysRolePermission);
    }

    /**
    * 批量修改某几个字段
    * @param roleIds
    */
    public Integer batchUpdateSysRolePermissionList(Long[] roleIds){
        return sysRolePermissionMapper.batchUpdateSysRolePermissionList(roleIds);
    }

    /**
    * 查询全部
    */
    public List<SysRolePermission> selectSysRolePermissionList(SysRolePermission sysRolePermission){
        return sysRolePermissionMapper.selectSysRolePermissionList(sysRolePermission);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysRolePermission> selectUnionSysRolePermissionList(SysRolePermission sysRolePermission){
        return sysRolePermissionMapper.selectUnionSysRolePermissionList(sysRolePermission);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysRolePermission selectSysRolePermissionById(Long roleId){
        return sysRolePermissionMapper.selectSysRolePermissionById(roleId);
    }
}
