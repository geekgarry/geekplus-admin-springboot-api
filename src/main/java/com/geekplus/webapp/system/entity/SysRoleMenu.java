package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能： 对象:sys_role_menu
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysRoleMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     *
     */
    private Long roleId;

    /**
     *
     */
    private Long menuId;

	/**
	 *获取角色ID
	 */
	public Long getRoleId(){
		return roleId;
	}

	/**
	 *设置角色ID
	 */
	public void setRoleId(Long roleId){
		this.roleId = roleId;
	}
	/**
	 *获取角色菜单ID
	 */
	public Long getMenuId(){
		return menuId;
	}

	/**
	 *设置角色菜单ID
	 */
	public void setMenuId(Long menuId){
		this.menuId = menuId;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("menuId", getMenuId())
            .toString();
    }
}
