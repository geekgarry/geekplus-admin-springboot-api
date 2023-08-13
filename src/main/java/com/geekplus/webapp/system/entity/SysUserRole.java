package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能：系统用户角色关系表 对象:sys_user_role
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysUserRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     * 系统用户角色关系表 系统用户角色关系表
     */
    private Long userId;

    /**
     * 系统用户角色关系表 系统用户角色关系表
     */
    private Long roleId;

	/**
	 *获取用户ID
	 */
	public Long getUserId(){
		return userId;
	}

	/**
	 *设置用户ID
	 */
	public void setUserId(Long userId){
		this.userId = userId;
	}
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("roleId", getRoleId())
            .toString();
    }
}
