package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能：用户角色权限关系表 对象:sys_role_permission
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysRolePermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     * 用户角色权限关系表 用户角色权限关系表
     */
    private Long roleId;

    /**
     * 用户角色权限关系表 用户角色权限关系表
     */
    private Long permissionId;

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
	 *获取权限ID
	 */
	public Long getPermissionId(){
		return permissionId;
	}

	/**
	 *设置权限ID
	 */
	public void setPermissionId(Long permissionId){
		this.permissionId = permissionId;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("permissionId", getPermissionId())
            .toString();
    }
}
