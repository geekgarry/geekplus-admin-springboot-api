package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;
import java.util.List;

/**
 * 功能：部门表 对象:sys_dept
 *
 * @author CodeGenerator
 * @date 2023/07/17
 */
public class SysDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     * 部门表 部门表
     */
    private Long deptId;

    /**
     * 部门表 部门表
     */
    private Long parentId;

    /**
     * 部门表 部门表
     */
    private String ancestors;

    /**
     * 部门表 部门表
     */
    private String deptName;

    /**
     * 部门表 部门表
     */
    private Integer orderNum;

    /**
     * 部门表 部门表
     */
    private String leader;

    /**
     * 部门表 部门表
     */
    private String phone;

    /**
     * 部门表 部门表
     */
    private String email;

    /**
     * 部门表 部门表
     */
    private String status;

    /**
     * 部门表 部门表
     */
    private String delFlag;

    /**
     * 部门表 部门表
     */
    private String createBy;

    /**
     * 部门表 部门表
     */
    private Date createTime;

    /**
     * 部门表 部门表
     */
    private String updateBy;

    /**
     * 部门表 部门表
     */
    private Date updateTime;

    private List<SysDept> children;

	/**
	 *获取部门id
	 */
	public Long getDeptId(){
		return deptId;
	}

	/**
	 *设置部门id
	 */
	public void setDeptId(Long deptId){
		this.deptId = deptId;
	}
	/**
	 *获取父部门id
	 */
	public Long getParentId(){
		return parentId;
	}

	/**
	 *设置父部门id
	 */
	public void setParentId(Long parentId){
		this.parentId = parentId;
	}
	/**
	 *获取祖级列表
	 */
	public String getAncestors(){
		return ancestors;
	}

	/**
	 *设置祖级列表
	 */
	public void setAncestors(String ancestors){
		this.ancestors = ancestors;
	}
	/**
	 *获取部门名称
	 */
	public String getDeptName(){
		return deptName;
	}

	/**
	 *设置部门名称
	 */
	public void setDeptName(String deptName){
		this.deptName = deptName;
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
	 *获取负责人
	 */
	public String getLeader(){
		return leader;
	}

	/**
	 *设置负责人
	 */
	public void setLeader(String leader){
		this.leader = leader;
	}
	/**
	 *获取联系电话
	 */
	public String getPhone(){
		return phone;
	}

	/**
	 *设置联系电话
	 */
	public void setPhone(String phone){
		this.phone = phone;
	}
	/**
	 *获取邮箱
	 */
	public String getEmail(){
		return email;
	}

	/**
	 *设置邮箱
	 */
	public void setEmail(String email){
		this.email = email;
	}
	/**
	 *获取部门状态（0正常 1停用）
	 */
	public String getStatus(){
		return status;
	}

	/**
	 *设置部门状态（0正常 1停用）
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

	public List<SysDept> getChildren() {
		return children;
	}

	public void setChildren(List<SysDept> children) {
		this.children = children;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptId", getDeptId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("deptName", getDeptName())
            .append("orderNum", getOrderNum())
            .append("leader", getLeader())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
