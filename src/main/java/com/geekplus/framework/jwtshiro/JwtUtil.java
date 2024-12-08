package com.geekplus.framework.jwtshiro;

import com.geekplus.common.domain.LoginUser;
import com.plusplus.algorithm.Algorithm;
import com.plusplus.jet.Jet;
import com.plusplus.jwt.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: GeekPlus
 * @description: 使用token验证用户是否登录，管理系统的核心Jwt工具类
 * @author: GarryChan
 * @create: 2020-10-26 17:15
 **/
@Component
public class JwtUtil {

    //设置过期时间
    @Value("${token.expireTime}")
    private long EXPIRE_TIME;

    //剩余刷新时间,剩余时间小于12分钟
    @Value("${token.residueRefreshTime}")
    private long REFRESH_TIME;

    @Value("${token.secret}")
    private String TOKEN_SECRET;
    //token秘钥
    //private final String TOKEN_SECRET = "ROCZFasChinaUUChanPlusIsCEO2023MAIKR";

    /**
      * @Author geekplus
      * @Description //关键用户信息和过期时间加密生成token
      * @Param
      * @Throws
      * @Return {@link }
      */
    public String sign(LoginUser sysUser){
        try {
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            String token = Jet.builder()
                    .claim("userName",sysUser.getSysUser().getUserName())
                    .claim("tokenId",sysUser.getTokenId())
                    .issuedAt(new Date())
                    .expireAt(new Date(System.currentTimeMillis()+EXPIRE_TIME*1000))
                    .algorithm(algorithm)
                    .sign();
//            String token = JWT.create().withHeader(header)
//                    .withClaim("userName",sysUser.getUserName())
//                    .withClaim("tokenId",tokenId)
//                    .withIssuedAt(new Date()) //创建的时间
//                    .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRE_TIME*1000)) //过期时间
//                    .sign(algorithm);
            return token;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean verify(String token){
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        try {
            verifyResult(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Jet.DecodeJet verifyResult(String token) {
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
//        JWTVerifier verifier = JWT.require(algorithm).build();
//        DecodedJWT jwt = verifier.verify(AESUtil.decrypt(token));
        //Jet.JetVerifier jetVerifier = Jet.require().algorithm(algorithm);
        Jet.DecodeJet decodeJet = Jet.require().algorithm(algorithm).verify(token);
        return decodeJet;
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

    //解析token拿到tokenId
    public String getTokenIdFromToken(String token){
        String tokenId = verifyResult(token).getClaim("tokenId");
        return tokenId;
    }

    //解析token拿到userNam
    public String getUserNameFromToken(String token){
        String userName = verifyResult(token).getClaim("userName");
        return userName;
    }

    //获取过期时间
    public Long getExpire(String token){
        try {
            //DecodedJWT jwt = JWT.decode(token);
            return verifyResult(token).getExpireAt().getTime();
        }catch (Exception e){
            return null;
        }
    }

}
