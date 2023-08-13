/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/5/23 18:06
 * description: 做什么的？
 */
package com.geekplus.common.domain;

import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysRole;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 系统用户表 系统用户表
     */
    private Long userId;

    /**
     * 系统用户表 系统用户表
     */
    private Integer deptId;

    /**
     * 部门名称 系统用户表
     */
    private String deptName;

    /**
     * 系统用户表 系统用户表
     */
    private String userName;

    /**
     * 系统用户表 系统用户表
     */
    private String nickName;

    /**
     * 系统用户表 系统用户表
     */
    private String userType;

    /**
     * 系统用户表 系统用户表
     */
    private String email;

    /**
     * 系统用户表 系统用户表
     */
    private String phoneunmber;

    /**
     * 系统用户表 系统用户表
     */
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
    private String status;

    /**
     * 系统用户表 系统用户表
     */
    private String delFlag;

    /**
     * 系统用户表 系统用户表
     */
    private String loginIp;

    /**
     * 系统用户表 系统用户表
     */
    private Date loginTime;

    /**
     * 系统用户表 系统用户表
     */
    private String createBy;

    /**
     * 系统用户表 系统用户表
     */
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

    private String tokenId;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    @Transient
    private String validateCode;
    @Transient
    private String validateKey;
    @Transient
    private Boolean rememberMe;

    private List<SysRole> sysRoleList;
    private List<SysMenu> sysMenuList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneunmber() {
        return phoneunmber;
    }

    public void setPhoneunmber(String phoneunmber) {
        this.phoneunmber = phoneunmber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getValidateKey() {
        return validateKey;
    }

    public void setValidateKey(String validateKey) {
        this.validateKey = validateKey;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public List<SysRole> getSysRoleList() {
        return sysRoleList;
    }

    public void setSysRoleList(List<SysRole> sysRoleList) {
        this.sysRoleList = sysRoleList;
    }

    public List<SysMenu> getSysMenuList() {
        return sysMenuList;
    }

    public void setSysMenuList(List<SysMenu> sysMenuList) {
        this.sysMenuList = sysMenuList;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
