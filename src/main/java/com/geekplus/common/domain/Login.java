///**
// *
// * @Package: com.juheyinhang.goldretail_common.entity
// * @author: weizheng
// * @date: 2018年7月18日 下午2:27:58
// */
//package com.geekplus.common.domain;
//
//import javax.persistence.Column;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import java.util.Date;
//
///**
// * @ClassName: Login
// * @Description: 登录实体
// * @author: GarryChan
// * @date: 2018年7月18日 下午2:27:58
// */
//public class Login {
//
//	  /**
//     * 编号
//     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String id;
//
//    /**
//     * 账户名称
//     */
//    @Column(name = "account_name")
//    private String accountName;
//
//    /**
//     * 密码
//     */
//    private String password;
//
//    @Column(name = "login_time")
//    private Date loginTime;
//
//    /**
//     * 令牌
//     */
//    private String token;
//
//    /**
//     * 账户状态 0 正常 1 封号中 2 异常
//     */
//    private Integer status;
//
//    /**
//     * 账户信息编号 外键
//     */
//    @Column(name = "account_id")
//    private String accountId;
//
//    /**
//     * 账户等级 100 正常用户  1314 管理员 259 经销商 396 分销商
//     */
//    @Column(name = "user_level")
//    private Integer userLevel;
//
//    /**
//     * 备注信息
//     */
//    private String note;
//
//    public Login() {
//	}
//
//	public Login(String accountName, String password, Date loginTime, String token, String accountId) {
//		this.accountName = accountName;
//		this.password = password;
//		this.loginTime = loginTime;
//		this.token = token;
//		this.accountId = accountId;
//	}
//
//	/**
//     * 获取编号
//     *
//     * @return id - 编号
//     */
//    public String getId() {
//        return id;
//    }
//
//    /**
//     * 设置编号
//     *
//     * @param id 编号
//     */
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    /**
//     * 获取账户名称
//     *
//     * @return account_name - 账户名称
//     */
//    public String getAccountName() {
//        return accountName;
//    }
//
//    /**
//     * 设置账户名称
//     *
//     * @param accountName 账户名称
//     */
//    public void setAccountName(String accountName) {
//        this.accountName = accountName;
//    }
//
//    /**
//     * 获取密码
//     *
//     * @return password - 密码
//     */
//    public String getPassword() {
//        return password;
//    }
//
//    /**
//     * 设置密码
//     *
//     * @param password 密码
//     */
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    /**
//     * @return login_time
//     */
//    public Date getLoginTime() {
//        return loginTime;
//    }
//
//    /**
//     * @param loginTime
//     */
//    public void setLoginTime(Date loginTime) {
//        this.loginTime = loginTime;
//    }
//
//    /**
//     * 获取令牌
//     *
//     * @return token - 令牌
//     */
//    public String getToken() {
//        return token;
//    }
//
//    /**
//     * 设置令牌
//     *
//     * @param token 令牌
//     */
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    /**
//     * 获取账户状态 0 正常 1 封号中 2 异常
//     *
//     * @return status - 账户状态 0 正常 1 封号中 2 异常
//     */
//    public Integer getStatus() {
//        return status;
//    }
//
//    /**
//     * 设置账户状态 0 正常 1 封号中 2 异常
//     *
//     * @param status 账户状态 0 正常 1 封号中 2 异常
//     */
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    /**
//     * 获取账户信息编号 外键
//     *
//     * @return account_id - 账户信息编号 外键
//     */
//    public String getAccountId() {
//        return accountId;
//    }
//
//    /**
//     * 设置账户信息编号 外键
//     *
//     * @param accountId 账户信息编号 外键
//     */
//    public void setAccountId(String accountId) {
//        this.accountId = accountId;
//    }
//
//    /**
//     * 获取账户等级 100 正常用户  1314 管理员 259 经销商 396 分销商
//     *
//     * @return user_level - 账户等级 100 正常用户  1314 管理员 259 经销商 396 分销商
//     */
//    public Integer getUserLevel() {
//        return userLevel;
//    }
//
//    /**
//     * 设置账户等级 100 正常用户  1314 管理员 259 经销商 396 分销商
//     *
//     * @param userLevel 账户等级 100 正常用户  1314 管理员 259 经销商 396 分销商
//     */
//    public void setUserLevel(Integer userLevel) {
//        this.userLevel = userLevel;
//    }
//
//    /**
//     * 获取备注信息
//     *
//     * @return note - 备注信息
//     */
//    public String getNote() {
//        return note;
//    }
//
//    /**
//     * 设置备注信息
//     *
//     * @param note 备注信息
//     */
//    public void setNote(String note) {
//        this.note = note;
//    }
//}
