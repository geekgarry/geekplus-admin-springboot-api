package com.geekplus.framework.jwtshiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: IFreshWeb
 * @description: 使用token验证用户是否登录
 * @author: GarryChan
 * @create: 2020-10-26 17:15
 **/
@Component
public class JwtTokenUtil {

    @Resource
    private RedisUtil redisUtil;

    //设置过期时间
    @Value("${token.expireTime}")
    private long EXPIRE_TIME;

    //剩余刷新时间
    @Value("${token.residueRefreshTime}")
    private long REFRESH_TIME;

    @Value("${token.secret}")
    private String TOKEN_SECRET;
    //token秘钥
    //private static final String TOKEN_SECRET = "ROCZFasChinaUUChanPlusIsCEO2023MAIKR";

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
        refreshToken(loginUser);

        return createToken(loginUser);
    }

    public String sign(String username) {
        Date date = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        return JWT.create()
                .withClaim("userName", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    public String token(LoginUser sysUser){
        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME*1000);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("userName",sysUser.getUserName())
                    .withClaim("email",sysUser.getEmail())
                    .withClaim("tokenId",sysUser.getTokenId())
                    //.withArrayClaim("roles", (String[]) sysUser.getSysRoleList().stream().map(SysRole::getRoleKey).toArray())
                    //.withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return token;
    }

    /**
     * 2.4 token刷新
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
                String newAuthorization = token(loginUser);
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

    public boolean verify(String token){
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            //DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    /**
     * 判断是否需要刷新
     * @param cacheToken 缓存中的token
     * @param currentTime 当前时间
     * @return
     */
    public boolean checkRefresh(String cacheToken, long currentTime){
        // 获取token的生成时间
        long current = getExpire(cacheToken);
        // token有效时间-当前时间↑ < 需要刷新的有效时间？true需要刷新：false有效期内
        //剩余需要刷新时间
        if (current - currentTime < REFRESH_TIME*1000)
            return true;
        else
            return false;
    }

    public boolean verify(String token,LoginUser loginUser){
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userName",loginUser.getUserName())
                    .withClaim("email",loginUser.getEmail())
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public DecodedJWT verifyResult(String token){
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }

    //解析token拿到etokenId
    public String getTokenIdFromToken(String token){
        String tokenId = verifyResult(token).getClaim("tokenId").asString();
        return tokenId;
    }

    //解析token拿到userNam
    public String getUserNameFromToken(String token){
        String userName = verifyResult(token).getClaim("userName").asString();
        return userName;
    }

    //获取过期时间
    public Long getExpire(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt().getTime();
        }catch (Exception e){
            return null;
        }
    }

    public boolean isExpire(String token){
        DecodedJWT jwt = JWT.decode(token);
        return System.currentTimeMillis() > jwt.getExpiresAt().getTime();
    }

    public static void main(String[] args) {
        JwtTokenUtil jwtTokenUtil=new JwtTokenUtil();
        String username ="GarryChan";
        String password = "123456";
        String tokenStr="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbklkIjoiZjkwNTcxNTUtOTliNy00Y2ViLWIyNjEtMDRkNDYxNjdkNTkwIiwidXNlck5hbWUiOiJnZWVrcGx1cyIsImV4cCI6MTY4OTYxMTIxOCwiZW1haWwiOiJnZWVrY2pqQGdlZWtwbHVzLnh5eiJ9.pw89yRosIfbT5zC67D3o2H2o0Ty1mSkRfz7Py8-3ozE";
        LoginUser loginUser=new LoginUser();
        loginUser.setUserName(username);
        loginUser.setPassword(password);
        String token = jwtTokenUtil.token(loginUser);
        //System.out.println(token);
        boolean b =jwtTokenUtil.verify(tokenStr);
        String userName=jwtTokenUtil.getUserNameFromToken(tokenStr);
        System.out.println(b);
        System.out.println(userName);
    }
}
