package com.geekplus.common.util.sysmenu;

import com.geekplus.webapp.system.entity.SysMenu;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 9/10/23 00:02
 * description: 做什么的？
 */
@Slf4j
public class SysMenuUtil {

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public static List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
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
    private static void recursionFn(List<SysMenu> list, SysMenu item)
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
    private static List<SysMenu> getChildList(List<SysMenu> list, SysMenu item)
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
    private static boolean hasChild(List<SysMenu> list, SysMenu item)
    {
        return getChildList(list, item).size() > 0 ? true : false;
    }

    //递归方法，加载菜单为折叠形态
    public static List<SysMenu> getParentMenuList(List<SysMenu> list){
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
    public static List<SysMenu> getChild(List<SysMenu> list, Long menuId){
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
