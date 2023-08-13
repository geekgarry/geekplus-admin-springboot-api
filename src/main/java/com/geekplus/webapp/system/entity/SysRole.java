package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;
import java.util.Date;
import java.util.List;

/**
 * 功能：系统角色表 对象:sys_role
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     * 系统角色表 系统角色表
     */
    private Long roleId;

    /**
     * 系统角色表 系统角色表
     */
    private String roleName;

    /**
     * 系统角色表 系统角色表
     */
    private String roleKey;

    /**
     * 系统角色表 系统角色表
     */
    private Integer roleSort;

    /**
     * 系统角色表 系统角色表
     */
    private String dataScope;

    /**
     * 系统角色表 系统角色表
     */
    private Integer menuCheckStrictly;

    /**
     * 系统角色表 系统角色表
     */
    private Integer deptCheckStrictly;

    /**
     * 系统角色表 系统角色表
     */
    private String status;

    /**
     * 系统角色表 系统角色表
     */
    private String delFlag;

    /**
     * 系统角色表 系统角色表
     */
    private String createBy;

    /**
     * 系统角色表 系统角色表
     */
    private Date createTime;

    /**
     * 系统角色表 系统角色表
     */
    private String updateBy;

    /**
     * 系统角色表 系统角色表
     */
    private Date updateTime;

    /**
     * 系统角色表 系统角色表
     */
    private String remark;

	private List<SysMenu> sysMenuList;

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
	 *获取角色名称
	 */
	public String getRoleName(){
		return roleName;
	}

	/**
	 *设置角色名称
	 */
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}
	/**
	 *获取角色权限字符串
	 */
	public String getRoleKey(){
		return roleKey;
	}

	/**
	 *设置角色权限字符串
	 */
	public void setRoleKey(String roleKey){
		this.roleKey = roleKey;
	}
	/**
	 *获取显示顺序
	 */
	public Integer getRoleSort(){
		return roleSort;
	}

	/**
	 *设置显示顺序
	 */
	public void setRoleSort(Integer roleSort){
		this.roleSort = roleSort;
	}
	/**
	 *获取数据范围（1：全部数据权限 2：自定数据权限 3：本群组数据权限 4：本群组及以下数据权限）
	 */
	public String getDataScope(){
		return dataScope;
	}

	/**
	 *设置数据范围（1：全部数据权限 2：自定数据权限 3：本群组数据权限 4：本群组及以下数据权限）
	 */
	public void setDataScope(String dataScope){
		this.dataScope = dataScope;
	}
	/**
	 *获取菜单树选择项是否关联显示
	 */
	public Integer getMenuCheckStrictly(){
		return menuCheckStrictly;
	}

	/**
	 *设置菜单树选择项是否关联显示
	 */
	public void setMenuCheckStrictly(Integer menuCheckStrictly){
		this.menuCheckStrictly = menuCheckStrictly;
	}
	/**
	 *获取部门树选择项是否关联显示
	 */
	public Integer getDeptCheckStrictly(){
		return deptCheckStrictly;
	}

	/**
	 *设置部门树选择项是否关联显示
	 */
	public void setDeptCheckStrictly(Integer deptCheckStrictly){
		this.deptCheckStrictly = deptCheckStrictly;
	}
	/**
	 *获取角色状态（0正常 1停用）
	 */
	public String getStatus(){
		return status;
	}

	/**
	 *设置角色状态（0正常 1停用）
	 */
	public void setStatus(String status){
		this.status = status;
	}
	/**
	 *获取删除标志（0代表存在 2代表删除）
	 */
	public String getDelFlag(){
		return delFlag;
	}

	/**
	 *设置删除标志（0代表存在 2代表删除）
	 */
	public void setDelFlag(String delFlag){
		this.delFlag = delFlag;
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

	public List<SysMenu> getSysMenuList() {
		return sysMenuList;
	}

	public void setSysMenuList(List<SysMenu> sysMenuList) {
		this.sysMenuList = sysMenuList;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("dataScope", getDataScope())
            .append("menuCheckStrictly", getMenuCheckStrictly())
            .append("deptCheckStrictly", getDeptCheckStrictly())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
