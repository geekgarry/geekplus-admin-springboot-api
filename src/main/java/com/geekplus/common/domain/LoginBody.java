package com.geekplus.common.domain;

/**
 * 用户登录请求体对象
 *
 * @author
 */
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String validateCode;

    /**
     * 唯一标识
     */
    private String validateKey;

    /**
     * 记住我
     */
    private Boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
