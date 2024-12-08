package com.geekplus.webapp.system.entity;

import com.geekplus.common.annotation.Excel;
import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

/**
 * 功能：系统用户表 对象:sys_user
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "用户序号", cellType = Excel.ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "群组号", type = Excel.Type.EXPORT)
    private Integer deptId;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "用户名")
    private String userName;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "昵称")
    private String nickName;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "用户类型")
    private String userType;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "邮箱")
    private String email;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "手机号")
    private String phoneNumber;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "性别", readConverterExp = "0=女,1=男,2=未知")
    private String gender;

    /**
     * 系统用户表 系统用户表
     */
    private String avatar;

    /**
     * 系统用户表 系统用户表
     */
    private String password;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "是否删除")
    private String delFlag;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "登录IP", type = Excel.Type.EXPORT)
    private String loginIp;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "最近登录", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date loginTime;

    /**
     * 系统用户表 系统用户表
     */
    private String createBy;

    /**
     * 系统用户表 系统用户表
     */
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date createTime;

    /**
     * 系统用户表 系统用户表
     */
    private String updateBy;

    /**
     * 系统用户表 系统用户表
     */
    private Date updateTime;

    /**
     * 系统用户表 系统用户表
     */
    private String remark;

    private SysDept sysDept;
	private List<SysRole> sysRoleList;

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
	 *获取群组ID
	 */
	public Integer getDeptId(){
		return deptId;
	}

	/**
	 *设置群组ID
	 */
	public void setDeptId(Integer deptId){
		this.deptId = deptId;
	}
	/**
	 *获取用户账户名
	 */
	public String getUserName(){
		return userName;
	}

	/**
	 *设置用户账户名
	 */
	public void setUserName(String userName){
		this.userName = userName;
	}
	/**
	 *获取用户昵称
	 */
	public String getNickName(){
		return nickName;
	}

	/**
	 *设置用户昵称
	 */
	public void setNickName(String nickName){
		this.nickName = nickName;
	}
	/**
	 *获取用户类型，是否为管理员
	 */
	public String getUserType(){
		return userType;
	}

	/**
	 *设置用户类型，是否为管理员
	 */
	public void setUserType(String userType){
		this.userType = userType;
	}
	/**
	 *获取用户邮件
	 */
	public String getEmail(){
		return email;
	}

	/**
	 *设置用户邮件
	 */
	public void setEmail(String email){
		this.email = email;
	}
	/**
	 *获取手机号
	 */
	public String getPhoneNumber(){
		return phoneNumber;
	}

	/**
	 *设置手机号
	 */
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	/**
	 *获取性别（0为女，1为男，2为未知）
	 */
	public String getGender(){
		return gender;
	}

	/**
	 *设置性别（0为女，1为男，2为未知）
	 */
	public void setGender(String gender){
		this.gender = gender;
	}
	/**
	 *获取头像地址
	 */
	public String getAvatar(){
		return avatar;
	}

	/**
	 *设置头像地址
	 */
	public void setAvatar(String avatar){
		this.avatar = avatar;
	}
	/**
	 *获取密码
	 */
	public String getPassword(){
		return password;
	}

	/**
	 *设置密码
	 */
	public void setPassword(String password){
		this.password = password;
	}
	/**
	 *获取账号状态（0正常，1停禁）
	 */
	public String getStatus(){
		return status;
	}

	/**
	 *设置账号状态（0正常，1停禁）
	 */
	public void setStatus(String status){
		this.status = status;
	}
	/**
	 *获取存在状态（0为存在，1删除）
	 */
	public String getDelFlag(){
		return delFlag;
	}

	/**
	 *设置存在状态（0为存在，1删除）
	 */
	public void setDelFlag(String delFlag){
		this.delFlag = delFlag;
	}
	/**
	 *获取最后登录IP
	 */
	public String getLoginIp(){
		return loginIp;
	}

	/**
	 *设置最后登录IP
	 */
	public void setLoginIp(String loginIp){
		this.loginIp = loginIp;
	}
	/**
	 *获取最后登录时间
	 */
	public Date getLoginTime(){
		return loginTime;
	}

	/**
	 *设置最后登录时间
	 */
	public void setLoginTime(Date loginTime){
		this.loginTime = loginTime;
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
	 *获取备注信息，可以是个人简介等
	 */
	public String getRemark(){
		return remark;
	}

	/**
	 *设置备注信息，可以是个人简介等
	 */
	public void setRemark(String remark){
		this.remark = remark;
	}

	public SysDept getSysDept() {
		return sysDept;
	}

	public void setSysDept(SysDept sysDept) {
		this.sysDept = sysDept;
	}

	public List<SysRole> getSysRoleList() {
		return sysRoleList;
	}

	public void setSysRoleList(List<SysRole> sysRoleList) {
		this.sysRoleList = sysRoleList;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("userType", getUserType())
            .append("email", getEmail())
            .append("phoneNumber", getPhoneNumber())
            .append("gender", getGender())
            .append("avatar", getAvatar())
            .append("password", getPassword())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginTime", getLoginTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
