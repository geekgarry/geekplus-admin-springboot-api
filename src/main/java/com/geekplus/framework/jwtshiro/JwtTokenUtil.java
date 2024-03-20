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
      * @Author geekplus
      * @Description //关键用户信息和过期时间加密生成token
      * @Param 
      * @Throws 
      * @Return {@link }
      */
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
                    //.withClaim("userId",sysUser.getUserId())
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
     * 判断是否需要刷新，这里为了不必要每次请求都要刷新生成token，
     * 采用在30分钟过期时间内的最后剩12分钟内再进行刷新，
     * 也就是登录后30分钟内超过18分钟后在进行刷新
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

    //获取过期时间
    public Long getExpire(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt().getTime();
        }catch (Exception e){
            return null;
        }
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
