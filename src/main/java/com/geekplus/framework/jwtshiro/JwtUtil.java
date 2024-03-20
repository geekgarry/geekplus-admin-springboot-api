/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/13/23 00:19
 * description: 做什么的？
 */
package com.geekplus.framework.jwtshiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.uuid.IdUtils;
import com.geekplus.framework.domain.server.Sys;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.entity.SysUser;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Stream;

public class JwtUtil {
    private static final long EXPIRE_TIME = 30 * 60 * 1000;
    private static final String TOKEN_SECRET = "!ad#12~";

    @Resource
    private RedisUtil redisUtil;

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser)
    {
        String tokenId = IdUtils.fastUUID();
        loginUser.setTokenId(tokenId);
        loginUser.setLoginTime(new Date());
        refreshTokenId(loginUser);

        return sign(loginUser);
    }

//    public String sign(String username) {
//        Date date = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
//        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
//        return JWT.create()
//                .withClaim("userName", username)
//                .withExpiresAt(date)
//                .sign(algorithm);
//    }

    /**
      * @Author geekplus
      * @Description //签名token
      * @Param
      * @Throws
      * @Return {@link }
      */
    public static String sign(LoginUser user) {
        JWTCreator.Builder builder = JWT.create();
        //设置头部信息
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        //Stream<String> list=user.getSysRoleList().stream().map(SysRole::getRoleKey);
        return builder.withHeader(header)
                .withClaim("userName", user.getUserName())
                .withClaim("tokenId", user.getTokenId())
                //.withClaim("role", (Date)list)
                //.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    /**
      * @Author geekplus
      * @Description //验证token
      * @Param
      * @Throws
      * @Return {@link }
      */
    public static DecodedJWT verify(String token,LoginUser loginUser) {
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //DecodedJWT jwt = verifier.verify(token);
        return JWT.require(algorithm)
                //.withClaim("userName",loginUser.getUserName())
                //.withClaim("email",loginUser.getEmail())
                .build().verify(token);
    }

    public boolean verify(String token){
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWT.require(algorithm)
                    //.withClaim("userName",loginUser.getUserName())
                    //.withClaim("email",loginUser.getEmail())
                    .build().verify(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    /**
     * token刷新
     *
     * @return
     */
    public void refreshTokenId(LoginUser loginUser) {
        // 获取当前时间戳
        long current = loginUser.getLoginTime().getTime();
        long currentTime= System.currentTimeMillis();
        // token有效时间-当前时间↑ < 需要刷新的有效时间？true需要刷新：false有效期内
        //剩余需要刷新时间
        if (current - currentTime < 720*1000) {
            //防止重要信息泄漏
            loginUser.setPassword(null);
            redisUtil.set(Constant.LOGIN_USER_TOKEN + ':' + loginUser.getTokenId(), loginUser);
            // 设置超时时间【这里除以1000是因为设置时间单位为秒了】【一般续签的时间都会乘以2】
            redisUtil.expire(Constant.LOGIN_USER_TOKEN + ':' + loginUser.getTokenId(), EXPIRE_TIME);
        }
    }
    /**
     * token刷新
     *
     * @return
     */
    public boolean refreshToken(LoginUser loginUser) {
        // 定义前缀+token 为缓存中的key，得到对应的value（cacheToken）
        String cacheToken = String.valueOf(redisUtil.get(Constant.LOGIN_USER_TOKEN + ':' + loginUser.getTokenId()));
        // 判断缓存中的token是否存在
        if (cacheToken != null && !cacheToken.equals("") && !cacheToken.equals("null")) {
            // 校验token有效性
            // 缓存中存在，验证失败（JwtUtil.verify在上一篇文章中已经介绍）
            if (!verify(cacheToken)) {
                // 重新sign，得到新的token
                String newAuthorization = sign(loginUser);
                // 写入到缓存中，key不变，将value换成新的token
                redisUtil.set(Constant.LOGIN_USER_TOKEN + ':' + loginUser.getTokenId(), newAuthorization);
                // 设置超时时间【这里除以1000是因为设置时间单位为秒了】【一般续签的时间都会乘以2】
                redisUtil.expire(Constant.LOGIN_USER_TOKEN + ':' + loginUser.getTokenId(), EXPIRE_TIME);
                // 缓存中存在，验证成功
            } else {
                // 上面的写法，与下面的相同
                // 用户这次请求JWTToken值还在生命周期内，重新put新的生命周期时间（有效时间）
                redisUtil.set(Constant.LOGIN_USER_TOKEN + ':' + loginUser.getTokenId(), cacheToken, EXPIRE_TIME);
            }
            return true;
        }
        return false;
    }

    /**
      * @Author geekplus
      * @Description //判断是否过期
      * @Param
      * @Throws
      * @Return {@link }
      */
    public boolean isExpire(String token){
        DecodedJWT jwt = JWT.decode(token);
        return System.currentTimeMillis() > jwt.getExpiresAt().getTime();
    }
}
