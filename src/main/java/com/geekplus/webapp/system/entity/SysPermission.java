package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能：系统权限表 对象:sys_permission
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysPermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     * 系统权限表 系统权限表
     */
    private Long permissionId;

    /**
     * 系统权限表 系统权限表
     */
    private String permissionName;

    /**
     * 系统权限表 系统权限表
     */
    private String remark;

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
	/**
	 *获取权限名称
	 */
	public String getPermissionName(){
		return permissionName;
	}

	/**
	 *设置权限名称
	 */
	public void setPermissionName(String permissionName){
		this.permissionName = permissionName;
	}
	/**
	 *获取权限列表
	 */
	public String getRemark(){
		return remark;
	}

	/**
	 *设置权限列表
	 */
	public void setRemark(String remark){
		this.remark = remark;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("permissionId", getPermissionId())
            .append("permissionName", getPermissionName())
            .append("remark", getRemark())
            .toString();
    }
}
