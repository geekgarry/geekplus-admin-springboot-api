package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysRoleMenuMapper;
import com.geekplus.webapp.system.entity.SysRoleMenu;
import com.geekplus.webapp.system.service.SysRoleMenuService;
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
public class SysRoleMenuServiceImpl implements SysRoleMenuService {
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
    * 增加
    * @param sysRoleMenu
    * @return
    */
    public Integer insertSysRoleMenu(SysRoleMenu sysRoleMenu){
        return sysRoleMenuMapper.insertSysRoleMenu(sysRoleMenu);
    }

    /**
    * 批量增加
    * @param sysRoleMenuList
    * @return
    */
    public Integer batchInsertSysRoleMenuList(List<SysRoleMenu> sysRoleMenuList){
        return sysRoleMenuMapper.batchInsertSysRoleMenuList(sysRoleMenuList);
    }

    /**
    * 删除
    * @param roleId
    */
    public Integer deleteSysRoleMenuById(Long roleId){
        return sysRoleMenuMapper.deleteSysRoleMenuById(roleId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysRoleMenuByIds(Long[] roleIds){
        return sysRoleMenuMapper.deleteSysRoleMenuByIds(roleIds);
    }

    /**
    * 修改
    * @param sysRoleMenu
    */
    public Integer updateSysRoleMenu(SysRoleMenu sysRoleMenu){
        return sysRoleMenuMapper.updateSysRoleMenu(sysRoleMenu);
    }

    /**
    * 批量修改某几个字段
    * @param roleIds
    */
    public Integer batchUpdateSysRoleMenuList(Long[] roleIds){
        return sysRoleMenuMapper.batchUpdateSysRoleMenuList(roleIds);
    }

    /**
    * 查询全部
    */
    public List<SysRoleMenu> selectSysRoleMenuList(SysRoleMenu sysRoleMenu){
        return sysRoleMenuMapper.selectSysRoleMenuList(sysRoleMenu);
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysRoleMenu> selectUnionSysRoleMenuList(SysRoleMenu sysRoleMenu){
        return sysRoleMenuMapper.selectUnionSysRoleMenuList(sysRoleMenu);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysRoleMenu selectSysRoleMenuById(Long roleId){
        return sysRoleMenuMapper.selectSysRoleMenuById(roleId);
    }

    @Override
    public Integer deleteSysRoleMenu(SysRoleMenu sysRoleMenu) {
        return sysRoleMenuMapper.deleteSysRoleMenu(sysRoleMenu);
    }

    @Override
    public Integer batchDeleteSysRoleMenu(List<SysRoleMenu> roleMenuList) {
        return sysRoleMenuMapper.batchDeleteSysRoleMenu(roleMenuList);
    }
}
