package com.geekplus.framework.jwtshiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/24 9:55 下午
 * description: 做什么的？
 */
public class JwtToken implements AuthenticationToken {
    private static final long serialVersionUID = -2564928913725078138L;

    private String token;

    private String password;

    public JwtToken(String token){
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

//    private LoginType type;
//    public JwtToken() {
//        super();
//    }
//    public JwtToken(String username, String password, LoginType type, boolean rememberMe, String host) {
//        super(username, password, rememberMe,  host);
//        this.type = type;
//    }
//    public LoginType getType() {
//        return type;
//    }
//    public void setType(LoginType type) {
//        this.type = type;
//    }
//    /**
//     * 免密登录
//     * @param username
//     */
//    public JwtToken(String username) {
//        super(username, "", false, null);
//        this.type = LoginType.NOPASSWORD;
//    }
//
//    /**
//     * 账号密码登录
//     * @param username
//     * @param pwd
//     */
//    public JwtToken(String username, String pwd) {
//        super(username, pwd, false, null);
//        this.type = LoginType.PASSWORD;
//    }
}
