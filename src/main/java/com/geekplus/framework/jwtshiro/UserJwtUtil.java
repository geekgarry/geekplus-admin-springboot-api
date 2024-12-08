package com.geekplus.framework.jwtshiro;

import com.plusplus.algorithm.Algorithm;
import com.geekplus.common.constant.Constant;
import com.plusplus.jwt.JwtPlus;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.*;

import java.security.SecureRandom;
import java.util.*;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/13/23 00:19
 * description: jwt操作工具类，通用型
 */
public class UserJwtUtil {

    //客户端系统系统过期时间
    private static final long EXPIRE_TIME = 360 * 3600000; // 10天的毫秒数
    private static final String TOKEN_SECRET = "!ad#12~plus+asd";
    // 简化后的密钥
    private static final byte[] SECRET_KEY_BYTES = Base64.getEncoder().encode(TOKEN_SECRET.getBytes());

    /*****************************客户端JWT工具******************************/

    /**
     * @Author geekplus
     * @Description //客户端用户登录签名
     * @Param
     * @Throws
     * @Return {@link }
     */
    public static String sign(String account, String tokenId) {
        //设置头部信息
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) RSAUtil.getPublicKey("jwtPublic"), (RSAPrivateKey) RSAUtil.getPrivateKey("jwtPrivate"));
//        String token = JWT.create().withHeader(header)
//                .withClaim("account", account)
//                .withClaim("tokenId", tokenId)
//                .withIssuedAt(new Date()) //创建的时间
//                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
//                .sign(algorithm);
        //携带username，password信息，生成签名
        String token = JwtPlus.builder()
                .claim("account",account)
                .claim("tokenId",tokenId)
                .issuedAt(new Date())
                .expireAt(new Date(System.currentTimeMillis()+EXPIRE_TIME*1000))
                .algorithm(algorithm)
                .sign();
        return token;
    }

    /**
     * 从数据声明生成令牌
     *
     * @param username 数据声明
     * @return 令牌
     */
    public static String signToken(String username, String tokenId) {
        // 调整密钥大小
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);

        Map<String, Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");

        Map<String, Object> payload = new HashMap<>();
        payload.put("account", username); // 使用缩写
        payload.put("tokenId", tokenId);
        return Jwts.builder()
                .header().add(header).and()
                .claims(payload)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(Keys.hmacShaKeyFor(keyBytes))
                .compact();
    }

    /**
     * @Author geekplus
     * @Description //客户端验证是否有效
     * @Param
     * @Throws
     * @Return {@link }
     */
    public static boolean verify(String token){
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

    /**
      * @Author geekplus
      * @Description //验证token
      * @Param
      * @Throws
      * @Return {@link }
      */
    public static JwtPlus.DecodeJwt verifyResult(String token) {
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) RSAUtil.getPublicKey("jwtPublic"), (RSAPrivateKey) RSAUtil.getPrivateKey("jwtPrivate"));
        //JWTVerifier verifier = JWT.require(algorithm).build();
        //DecodedJWT jwt = verifier.verify(token);
        JwtPlus.JwtVerifier jwtVerifier = JwtPlus.require().algorithm(algorithm);
        JwtPlus.DecodeJwt decodeJwt = jwtVerifier.verify(token);
        return decodeJwt;
    }

    //解析token拿到tokenId
    public static String getTokenId(String token){
        String tokenId = verifyResult(token).getClaim("tokenId");
        return tokenId;
    }

    public static String getCustomerTokenKey(String token){
        return Constant.PRE_REDIS_CUSTOMER_TOKEN + getTokenId(token);
    }

    public static String getCustomerTkIdKey(String tokenId){
        return Constant.PRE_REDIS_CUSTOMER_TOKEN + tokenId;
    }

    //解析token拿到统一的账户
    public static String getAccount(String token){
        String userName = verifyResult(token).getClaim("account");
        return userName;
    }

    //解析token拿到创建时间，也就是等于redis的开始时间
    public static long getStartTime(String token){
        long expireTime = verifyResult(token).getIssuedAt().getTime();
        return expireTime;
    }

    //解析token拿到过期时间
    public static long getExpireTime(String token){
        long expireTime = verifyResult(token).getExpireAt().getTime();
        return expireTime;
    }

    //解析token拿到phoneNumber
    public static String getPhoneNumber(String token){
        String userName = verifyResult(token).getClaim("phoneNumber");
        return userName;
    }

    //解析token拿到email
    public static String getEmail(String token){
        String userName = verifyResult(token).getClaim("email");
        return userName;
    }

    /**
     * 判断是否需要token刷新，即将过期时间的token，在最后剩余时间内判断需要刷新
     *
     * @return
     */
    public static boolean checkTokenRefresh(String cacheToken){
        // 获取token的过期时间
        long expireTime = getExpireTime(cacheToken);
        long currentTime= System.currentTimeMillis();
        // token有效时间-当前时间↑ < 需要刷新的有效时间？true需要刷新：false有效期内
        //剩余需要刷新时间
        if (expireTime - currentTime < 24 * 60 * 60 * 1000)
            return true;
        else
            return false;
    }

    /**
     * 判断是否需要token刷新，30分钟的短时间，在最后12分钟后判断需要刷新
     *
     * @return
     */
    public static boolean checkRefresh(String cacheToken){
        // 获取token的过期时间
        long expireTime = getExpireTime(cacheToken);
        long currentTime= System.currentTimeMillis();
        // token有效时间-当前时间↑ < 需要刷新的有效时间？true需要刷新：false有效期内
        //剩余需要刷新时间
        if (expireTime - currentTime < 720 * 1000)
            return true;
        else
            return false;
    }

    /**
      * @Author geekplus
      * @Description //判断是否过期
      * @Param
      * @Throws
      * @Return {@link }
      */
    public static boolean isExpire(String token){
        //DecodedJWT jwt = JWT.decode(token);
        return System.currentTimeMillis() > verifyResult(token).getExpireAt().getTime();
    }

    public static void main(String[] args) throws Exception {
//        String token1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ0b2tlbklkIjoiZXd1cmczdTI0MzJ1NTI0NTQ4MzV0YmV3Z2JzZnNkMzIiLCJleHAiOjE3Mjg2MDI1NzEsImlhdCI6MTcyNzMwNjU3MSwiYWNjb3VudCI6IkdhcnJ5In0.FqUf-t4a3L6X1_gdCKHDCJv_lqWyF632zL8REouPD9AZmsYJCSujqUzh3WNJJjK83ua7soPsv-swpyslvqz0RbmdLHaE-PhM-KsbA4UJB1P15xp3CFoK8gg7qGM0AD282RhW4f_TOUnIvQZSB1xezWS24hcioA-eSI5CosZPCpmYdJT4DZR_D1iSk-YKj0Vsr9SBuO12jwnXFZanQMQgS4zyHFpjhatZKKp_Iyco0PjYeh-pilH7J6-pi8m15jnZgSdLlEJcxeCbYW6AzSZVHNPJr_kw4xVW7w3q40Fnfl9_LvXMOPn9s3EO2ELCLvXpaZyZD3R9uiKtRtA6Q3FcWA";
//        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbklkIjoiZXd1cmczdTI0MzJ1NTI0NTQ4MzV0YmV3Z2JzZnNkMzIiLCJleHAiOjE3Mjg2MDQyOTQsImlhdCI6MTcyNzMwODI5NCwiYWNjb3VudCI6IkdhcnJ5In0.1FgRHaHOXS2d745oCYiMDaGjxdIk4Q6j2MocJh16sp8";
//        "efVyqSv9NMoOnOZ+5cmHNLH0SCuenrgeVZLQ3nU9RFgZGDSRyBAiyCXiqbNk03IyBsINbIjtzI85Pif/x73CkQrrBtVDZwpL3sfAZpDACScYTrThuCYjXxeD9af53Ry7XXCS6uLksiUptFJzpf+GuANtduaUIRIp8xa8q/AgzsYaNkubxBDsIvHQaLtzqOKcilyXvTO0uWiJ7cErhZvqA4/6w5Kilpc7EgVIQW9IhLiLE1eoSVu1w+QQViRGh1nhr0HgfZKclB92UY0wVuyRqW6bo8knFsSZGQ+fjdpOvVc=";
//        RSAUtil.generateKeyPair("jwtPublic2", "jwtPrivate2");
        String username ="Garry";
        String tokenId = "ewurg3u2432u52454835tbewgbsfsd32";
        String token = sign(username, tokenId);
        System.out.println(token);
        boolean b = verify(token);
        String userName= getAccount(token);
        System.out.println(b);
//        String tk1 = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyTmFtZSI6IkdhcnJ5In0.wK38_cd_RjLkH0j-ow1Ix4F6KwDHf9Rzt0IKv_IZpg1hNpCHlQEe3MqTH4vOHPlSAVx7vNcdoArUrKoZAzuWQg";
//        "efVyqSv9NMoOnOZ+5cmHNLH0SCuenrgeVZLQ3nU9RFgvk71ZT2vKSGPkaSLEfaVugwWwAge4ctQHa2UGjsvuzwzvq2G3gP1y4IRKypjjnPfqhxl1S6j5klUMPDltM2rbEqSB9gyfbw0udMAGxKjir8iClQchUEsYxndwLEgGpbjhxlbB4cSMVTToz79Z+6UjzfEL3/1hblm4IYVylRRhDXSWmvkNQX5ouRxuL4Ie7saKCfDPGgj9NPnyBC1WRuYCJCsI8GZbHFLFRhfzCETsTYUj5kd+ni/exPvlMhvzCes=";
//        "YWxnPUhTMjU2O3R5cD1KV1Q7.dXNlck5hbWU9R2FycnlDaGFuO3RpbWVzdGFtcD0xNzI3NTc0MjQ1Mzc2Ow.BHhYSxzvv73vv73vv70obzhBYDFQXg9Yby4V77-914dEPe-_vQx4HwFy";
//        System.out.println(AESUtil.encrypt(tk1));
//        System.out.println(signToken(username, tokenId));
        System.out.println(new Date());
        // 创建DateFormat对象
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
//        try {
//            ZonedDateTime dateTime = ZonedDateTime.parse("Sun Sep 29 19:34:03 CST 2024", formatter);
//            System.out.println(dateTime.toLocalDateTime());
//        } catch (DateTimeParseException e) {
//            e.printStackTrace();
//        }
    }
}
