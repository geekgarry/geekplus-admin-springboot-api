package com.geekplus.common.domain;

import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.entity.SysUser;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/5/23 18:06
 * description: 做什么的？
 */
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门名称 系统用户表
     */
    private String deptName;

    /**
     * 系统用户表 系统用户表
     */
    private String loginIp;

    /**
     * 系统用户表 系统用户表
     */
    private Date loginTime;

    private String tokenId;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    //用户信息，用户角色和用户权限菜单
    private SysUser sysUser;
    //权限信息
    private Set<String> sysMenuList;

    public LoginUser()
    {}

    public LoginUser(SysUser user, Set<String> permissionsMenu)
    {
        this.sysUser = user;
        this.sysMenuList = permissionsMenu;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
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

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public Set<String> getSysMenuList() {
        return sysMenuList;
    }

    public void setSysMenuList(Set<String> sysMenuList) {
        this.sysMenuList = sysMenuList;
    }
}
