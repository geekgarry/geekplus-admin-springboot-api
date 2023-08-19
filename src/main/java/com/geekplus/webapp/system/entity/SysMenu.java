package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.Date;
import java.util.List;

/**
 * 功能：系统菜单权限 对象:sys_menu
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     * 系统菜单权限 系统菜单权限
     */
    private Long menuId;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String menuName;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private Long parentId;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private Integer orderNum;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String path;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String component;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String isFrame;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String isCache;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String menuType;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String visible;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String status;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String perms;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String icon;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String createBy;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private Date createTime;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String updateBy;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private Date updateTime;

    /**
     * 系统菜单权限 系统菜单权限
     */
    private String remark;

	/** 子菜单 */
	private List<SysMenu> children = new ArrayList<SysMenu>();

	/**
	 *获取菜单ID
	 */
	public Long getMenuId(){
		return menuId;
	}

	/**
	 *设置菜单ID
	 */
	public void setMenuId(Long menuId){
		this.menuId = menuId;
	}
	/**
	 *获取菜单名称
	 */
	public String getMenuName(){
		return menuName;
	}

	/**
	 *设置菜单名称
	 */
	public void setMenuName(String menuName){
		this.menuName = menuName;
	}
	/**
	 *获取父菜单ID
	 */
	public Long getParentId(){
		return parentId;
	}

	/**
	 *设置父菜单ID
	 */
	public void setParentId(Long parentId){
		this.parentId = parentId;
	}
	/**
	 *获取显示顺序
	 */
	public Integer getOrderNum(){
		return orderNum;
	}

	/**
	 *设置显示顺序
	 */
	public void setOrderNum(Integer orderNum){
		this.orderNum = orderNum;
	}
	/**
	 *获取路由地址
	 */
	public String getPath(){
		return path;
	}

	/**
	 *设置路由地址
	 */
	public void setPath(String path){
		this.path = path;
	}
	/**
	 *获取组件路径
	 */
	public String getComponent(){
		return component;
	}

	/**
	 *设置组件路径
	 */
	public void setComponent(String component){
		this.component = component;
	}
	/**
	 *获取是否为外链（0是 1否）
	 */
	public String getIsFrame(){
		return isFrame;
	}

	/**
	 *设置是否为外链（0是 1否）
	 */
	public void setIsFrame(String isFrame){
		this.isFrame = isFrame;
	}
	/**
	 *获取是否缓存（0缓存 1不缓存）
	 */
	public String getIsCache(){
		return isCache;
	}

	/**
	 *设置是否缓存（0缓存 1不缓存）
	 */
	public void setIsCache(String isCache){
		this.isCache = isCache;
	}
	/**
	 *获取菜单类型（C目录 M菜单 B按钮）
	 */
	public String getMenuType(){
		return menuType;
	}

	/**
	 *设置菜单类型（C目录 M菜单 B按钮）
	 */
	public void setMenuType(String menuType){
		this.menuType = menuType;
	}
	/**
	 *获取菜单状态（0显示 1隐藏）
	 */
	public String getVisible(){
		return visible;
	}

	/**
	 *设置菜单状态（0显示 1隐藏）
	 */
	public void setVisible(String visible){
		this.visible = visible;
	}
	/**
	 *获取菜单状态（0正常 1停用）
	 */
	public String getStatus(){
		return status;
	}

	/**
	 *设置菜单状态（0正常 1停用）
	 */
	public void setStatus(String status){
		this.status = status;
	}
	/**
	 *获取权限标识
	 */
	public String getPerms(){
		return perms;
	}

	/**
	 *设置权限标识
	 */
	public void setPerms(String perms){
		this.perms = perms;
	}
	/**
	 *获取菜单图标
	 */
	public String getIcon(){
		return icon;
	}

	/**
	 *设置菜单图标
	 */
	public void setIcon(String icon){
		this.icon = icon;
	}
	/**
	 *获取创建者
	 */
	public String getCreateBy(){
		return createBy;
	}

	/**
	 *设置创建者
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *获取创建时间
	 */
	public Date getCreateTime(){
		return createTime;
	}

	/**
	 *设置创建时间
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**
	 *获取更新者
	 */
	public String getUpdateBy(){
		return updateBy;
	}

	/**
	 *设置更新者
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *获取更新时间
	 */
	public Date getUpdateTime(){
		return updateTime;
	}

	/**
	 *设置更新时间
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *获取备注
	 */
	public String getRemark(){
		return remark;
	}

	/**
	 *设置备注
	 */
	public void setRemark(String remark){
		this.remark = remark;
	}

	public List<SysMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("menuId", getMenuId())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("path", getPath())
            .append("component", getComponent())
            .append("isFrame", getIsFrame())
            .append("isCache", getIsCache())
            .append("menuType", getMenuType())
            .append("visible", getVisible())
            .append("status", getStatus())
            .append("perms", getPerms())
            .append("icon", getIcon())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
