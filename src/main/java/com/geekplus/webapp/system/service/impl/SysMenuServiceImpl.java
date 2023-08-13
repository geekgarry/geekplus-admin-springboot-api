package com.geekplus.webapp.system.service.impl;

import com.geekplus.webapp.system.mapper.SysMenuMapper;
import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.service.SysMenuService;
//import com.geekplus.core.AbstractService;
import com.geekplus.webapp.system.entity.SysRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2023/06/18.
 */
@Slf4j
@Service
@Transactional
public class SysMenuServiceImpl implements SysMenuService {
    @Resource
    private SysMenuMapper sysMenuMapper;

    /**
    * 增加
    * @param sysMenu
    * @return 系统菜单权限
    */
    public Integer insertSysMenu(SysMenu sysMenu){
        return sysMenuMapper.insertSysMenu(sysMenu);
    }

    /**
    * 批量增加
    * @param sysMenuList
    * @return 系统菜单权限
    */
    public Integer batchInsertSysMenuList(List<SysMenu> sysMenuList){
        return sysMenuMapper.batchInsertSysMenuList(sysMenuList);
    }

    /**
    * 删除
    * @param menuId
    */
    public Integer deleteSysMenuById(Long menuId){
        return sysMenuMapper.deleteSysMenuById(menuId);
    }

    /**
    * 批量删除
    */
    public Integer deleteSysMenuByIds(Long[] menuIds){
        return sysMenuMapper.deleteSysMenuByIds(menuIds);
    }

    /**
    * 修改
    * @param sysMenu
    */
    public Integer updateSysMenu(SysMenu sysMenu){
        return sysMenuMapper.updateSysMenu(sysMenu);
    }

    /**
    * 批量修改某几个字段
    * @param menuIds
    */
    public Integer batchUpdateSysMenuList(Long[] menuIds){
        return sysMenuMapper.batchUpdateSysMenuList(menuIds);
    }

    /**
    * 查询全部
    */
    public List<SysMenu> selectSysMenuList(SysMenu sysMenu){
        return sysMenuMapper.selectSysMenuList(sysMenu);
    }

    /**
     * 查询全部
     */
    public List<SysMenu> selectSysMenuTreeList(){
        return getParentMenuList(sysMenuMapper.selectSysMenuTreeList());
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<SysMenu> selectUnionSysMenuList(SysMenu sysMenu){
        return sysMenuMapper.selectUnionSysMenuList(sysMenu);
    }

    /**
    * 根据Id查询单条数据
    */
    public SysMenu selectSysMenuById(Long menuId){
        return sysMenuMapper.selectSysMenuById(menuId);
    }

    @Override
    public List<SysMenu> getSysMenuByRoles(List<SysRole> sysRoles) {
        return sysMenuMapper.selectMenusByRoles(sysRoles);
    }

    @Override
    public List<SysMenu> getMenuTreeByUserName(String userName) {
        return getParentMenuList(sysMenuMapper.selectMenuTreeByUserName(userName));
    }

    @Override
    public List<SysMenu> getMenuTreeByUserId(Long userId) {
        List<SysMenu> sysMenuList=sysMenuMapper.selectMenuTreeByUserId(userId);
        return getParentMenuList(sysMenuList);
    }

    @Override
    public List<SysMenu> getMenuTreeByRoleId(Long roleId) {
        return getParentMenuList(sysMenuMapper.selectMenuTreeByRoleId(roleId));
    }

    @Override
    public List<SysMenu> getMenuListByRoleId(Long roleId) {
        return sysMenuMapper.selectMenuTreeByRoleId(roleId);
    }

    @Override
    public List<Integer> getMenuIdListByRoleId(Long roleId) {
        return sysMenuMapper.selectMenuIdListByRoleId(roleId);
    }


    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu menu = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (menu.getParentId() == parentId)
            {
                recursionFn(list, menu);
                returnList.add(menu);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param item
     */
    private void recursionFn(List<SysMenu> list, SysMenu item)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, item);
        item.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu item)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == item.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu item)
    {
        return getChildList(list, item).size() > 0 ? true : false;
    }

    //递归方法，加载菜单为折叠形态
    public List<SysMenu> getParentMenuList(List<SysMenu> list){
        List<SysMenu> menuList=new ArrayList<>();
        list.stream().forEach(menu ->{
            //SysMenu menu = lt.next();
            if (menu.getParentId() == 0) {
                log.info("==========>数据"+menuList);
                menu.setChildren(getChild(list, menu.getMenuId()));
                menuList.add(menu);
            }
        });
        return menuList;
    }
    public List<SysMenu> getChild(List<SysMenu> list, Long menuId){
        List<SysMenu> childList=new ArrayList<>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();){
            SysMenu menu = iterator.next();
            if (menu.getParentId().equals(menuId)){
                log.info("==========>数据"+menu);
                menu.setChildren(getChild(list, menu.getMenuId()));
                childList.add(menu);
                log.info("==========>数据"+childList);
            }
        }
//        for (SysMenu menu:childList) {
//            menu.setChildren(getChild(list, menu.getMenuId()));
//        }
        return childList;
    }
}
