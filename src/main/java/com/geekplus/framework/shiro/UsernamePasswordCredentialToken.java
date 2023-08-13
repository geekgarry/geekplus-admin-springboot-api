/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/24 10:40 下午
 * description: 做什么的？
 */
package com.geekplus.framework.shiro;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * 参考org.apache.shiro.authcUsernamePasswordToken，增加了用户类型参数
 * @author caihz
 * @see org.apache.shiro.authc.UsernamePasswordToken
 */
public class UsernamePasswordCredentialToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码, in char[] format
     */
    private char[] password;

    /**
     * 是否记住我
     * 默认值：<code>false</code>
     */
    private boolean rememberMe = false;

    /**
     * 主机名称或ip
     */
    private String host;

    /**
     * 用户类型
     */
    private String usertype;

    public UsernamePasswordCredentialToken() {
    }

    /**
     * 构造方法
     * @param username 用户名
     * @param password 密码（char[]）
     * @param rememberMe 是否记住我
     * @param host 主机或ip
     * @param usertype 用户类型
     */
    public UsernamePasswordCredentialToken(final String username, final char[] password,
                                           final boolean rememberMe, final String host, final String usertype) {

        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
        this.host = host;
        this.usertype = usertype;
    }

    /**
     * 构造方法
     * @param username 用户名
     * @param password 密码（String）
     * @param rememberMe 是否记住我
     * @param host 主机或ip
     * @param usertype 用户类型
     */
    public UsernamePasswordCredentialToken(final String username, final String password,
                                         final boolean rememberMe, final String host, final String usertype) {
        this(username, password != null ? password.toCharArray() : null, rememberMe, host, usertype);
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public char[] getPassword() {
        return password;
    }
    public void setPassword(char[] password) {
        this.password = password;
    }

    /**
     * Simply returns {@link #getUsername() getUsername()}.
     *
     * @return the {@link #getUsername() username}.
     * @see org.apache.shiro.authc.AuthenticationToken#getPrincipal()
     */
    public Object getPrincipal() {
        return getUsername();
    }

    /**
     * Returns the {@link #getPassword() password} char array.
     *
     * @return the {@link #getPassword() password} char array.
     * @see org.apache.shiro.authc.AuthenticationToken#getCredentials()
     */
    public Object getCredentials() {
        return getPassword();
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public boolean isRememberMe() {
        return rememberMe;
    }
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    public String getUsertype() {
        return usertype;
    }
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    /**
     * 清除数据
     * 密码如果不为空，设置成0x00
     */
    public void clear() {
        this.username = null;
        this.host = null;
        this.rememberMe = false;
        this.usertype = null;

        if (this.password != null) {
            for (int i = 0; i < password.length; i++) {
                this.password[i] = 0x00;
            }
            this.password = null;
        }

    }

    /**
     * 重写toString方法
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" - ");
        sb.append(username);
        sb.append(", usertype=").append(usertype);
        sb.append(", rememberMe=").append(rememberMe);
        if (host != null) {
            sb.append(" (").append(host).append(")");
        }
        return sb.toString();
    }
}
