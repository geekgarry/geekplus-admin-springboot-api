package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysPermissionMapper;
import com.geekplus.webapp.system.entity.SysPermission;
import com.geekplus.webapp.system.service.SysPermissionService;
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
public class SysPermissionServiceImpl implements SysPermissionService {
    @Resource
    private SysPermissionMapper sysPermissionMapper;

    /**
    * 增加
    * @param sysPermission
    * @return 系统权限表
    */
    public Integer insertSysPermission(SysPermission sysPermission){
        return sysPermissionMapper.insertSysPermission(sysPermission);
    }

    /**
    * 批量增加
    * @param sysPermissionList
    * @return 系统权限表
    */
    public Integer batchInsertSysPermissionList(List<SysPermission> sysPermissionList){
        return sysPermissionMapper.batchInsertSysPermissionList(sysPermissionList);
    }

    /**
    * 删除
    * @param permissionId
    */
    public Integer deleteSysPermissionById(Long permissionId){
        return sysPermissionMapper.deleteSysPermissionById(permissionId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysPermissionByIds(Long[] permissionIds){
        return sysPermissionMapper.deleteSysPermissionByIds(permissionIds);
    }

    /**
    * 修改
    * @param sysPermission
    */
    public Integer updateSysPermission(SysPermission sysPermission){
        return sysPermissionMapper.updateSysPermission(sysPermission);
    }

    /**
    * 批量修改某几个字段
    * @param permissionIds
    */
    public Integer batchUpdateSysPermissionList(Long[] permissionIds){
        return sysPermissionMapper.batchUpdateSysPermissionList(permissionIds);
    }

    /**
    * 查询全部
    */
    public List<SysPermission> selectSysPermissionList(SysPermission sysPermission){
        return sysPermissionMapper.selectSysPermissionList(sysPermission);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysPermission> selectUnionSysPermissionList(SysPermission sysPermission){
        return sysPermissionMapper.selectUnionSysPermissionList(sysPermission);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysPermission selectSysPermissionById(Long permissionId){
        return sysPermissionMapper.selectSysPermissionById(permissionId);
    }
}
