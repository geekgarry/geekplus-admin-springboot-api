package com.geekplus.framework.jwtshiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 9/20/24 1:32 AM
 * description: 适用前端的客户JwtToken
 */
public class UserJwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String token;

    public UserJwtToken(String token) {
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
}
