/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/13/23 00:19
 * description: 做什么的？
 */
package com.geekplus.framework.jwtshiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.entity.SysUser;

import java.util.*;
import java.util.stream.Stream;

public class JWTUtil {
    private static final long EXPIRE = 30 * 60 * 1000;
    private static final String SECRET = "!ad#12~";

    public static String getToken(SysUser user) {
        JWTCreator.Builder builder = JWT.create();
        //设置头部信息
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        Stream<String> list=user.getSysRoleList().stream().map(SysRole::getRoleKey);
        return builder.withHeader(header)
                .withClaim("email", user.getUserName())
                .withClaim("role", (Date)list)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }
}
