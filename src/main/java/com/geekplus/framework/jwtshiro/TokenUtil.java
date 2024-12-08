package com.geekplus.framework.jwtshiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.plusplus.jet.Jet;
import com.plusplus.jwt.Jwt;
import com.plusplus.jwt.JwtPlus;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Description:Token生成工具
 * 第一部分我们称它为头部（header),第二部分我们称其为载荷（payload, 类似于飞机上承载的物品)，第三部分是签证（signature).
 * Author: Garry
 * Date: 2023-10-02
 * Time: 下午 5:05
 */

public class TokenUtil {

    // 用于生成签名的密钥
    private static final String SECRET_KEY = "!ad#12~plus+asd";


    // 生成 Token 方法
    public static String generateToken(String userName) {
        // 头部（Header）信息
        Map<String, String> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        // 载体（Payload）信息
        Map<String, String> payload = new HashMap<>();
        long timestamp = System.currentTimeMillis();
        String randomStr = generateRandomString(8);
        payload.put("userName", userName);
        payload.put("timestamp", String.valueOf(timestamp));
        payload.put("random", randomStr);

        // 对头部和载体进行 Base64 编码
        String headerEncoded = encodeBase64(mapToString(header));
        String payloadEncoded = encodeBase64(mapToString(payload));

        // 生成签名
        String signature = generateSignature(headerEncoded, payloadEncoded);

        // 合成最终的 Token
        return headerEncoded + "." + payloadEncoded + "." + signature;
    }

    // 生成签名方法
    private static String generateSignature(String header, String payload) {
        String data = header + "." + payload;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((data + SECRET_KEY).getBytes(StandardCharsets.UTF_8));
            return encodeBase64(new String(hash, StandardCharsets.UTF_8)); // 使用Base64 URL安全编码
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }

    // 生成随机字符串
    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    // Base64 编码
    private static String encodeBase64(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    // Base64 解码
    private static String decodeBase64(String data) {
        return new String(Base64.getUrlDecoder().decode(data), StandardCharsets.UTF_8);
    }

    // 将HashMap转换为字符串
    private static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        }
        return sb.toString();
    }

    // 将字符串转换回HashMap
    private static Map<String, String> stringToMap(String token) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = token.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    // 验证 Token 方法
    public static boolean validateToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        String headerEncoded = parts[0];
        String payloadEncoded = parts[1];
        String signature = parts[2];

        // 验证签名
        String expectedSignature = generateSignature(headerEncoded, payloadEncoded);
        if (!expectedSignature.equals(signature)) {
            return false;
        }

        // 解码并验证载体
        Map<String, String> payload = stringToMap(decodeBase64(payloadEncoded));
        String timestampStr = payload.get("timestamp");
        if (timestampStr == null) {
            return false;
        }

        // 验证时间戳是否在有效期内（例如，Token 只在1小时内有效）
        long timestamp = Long.parseLong(timestampStr);
        long currentTime = System.currentTimeMillis();
        long expirationTime = 60 * 60 * 1000; // 1小时有效期
        return currentTime - timestamp < expirationTime;
    }

    // 从 Token 中获取载体部分的方法
    public static Map<String, String> getPayload(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid Token format");
        }

        String payloadEncoded = parts[1];
        return stringToMap(decodeBase64(payloadEncoded));
    }

    public static void main(String[] args) {
        // 生成 Token
//        String userId = "12345";
//        String token = generateToken(userId);
//        System.out.println("Generated Token: " + token);
//
//        // 验证 Token
//        boolean isValid = validateToken(token);
//        System.out.println("Is Token valid? " + isValid);
//
//        System.out.println(getPayload(token).get("userId"));
        // 自定义头部和载体生成Token

        Jwt.SECRET_KEY = "@>U&@F;)@!H.VM)U";
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", "GarryChan");
        claims.put("tokenId", "snfjasnfjnasjkbfajksb32423532b52");
        com.plusplus.algorithm.Algorithm algorithmPlus = com.plusplus.algorithm.Algorithm.HMACMd5("sjfjkbjfksbjfkbsjbfjks");
        String token = Jwt.builder()
                .withPayload(claims)
                .expireAt(new Date(System.currentTimeMillis() + 5000))
                .issuedAt(new Date())
//                .secret("sjfjkbjfksbjfkbsjbfjks")
                .algorithm(algorithmPlus)
                .sign();

        System.out.println(token);
        String tk111 = Jet.builder().withPayload(claims)
                .expireAt(new Date(System.currentTimeMillis() + 5000))
                .issuedAt(new Date())
                .algorithm(algorithmPlus)
                .sign();
        System.out.println(tk111);
        Algorithm algorithm = Algorithm.HMAC256("sjfjkbjfksbjfkbsjbfjks");
        //设置头部信息
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        String token11 = JWT.create().withHeader(header)
                    .withClaim("userName","dsjjdsjk")
                    .withClaim("tokenId","snfjasnfjnasjkbfajksb32423532b52")
                    .withIssuedAt(new Date()) //创建的时间
                    .withExpiresAt(new Date(System.currentTimeMillis()+50000)) //过期时间
                    .sign(algorithm);
        System.out.println(token11);

        // 验证 Token
        boolean isValid = Jwt.require().algorithm(algorithmPlus).verifyToken(token);
        System.out.println("Is Token valid? " + isValid);

//        System.out.println(JwtPlus.require().secret("urhgesnkdngknkdsngk548y6480").verify(tk1));
        // 从 Token 中获取载体内容
//        Map<String, Object> payload = JetPlus.require().secret("urhgesnkdngknkdsngk548y6480").getClaims(token);
        Jwt.DecodeJwt decodeJwt = Jwt.require().algorithm(algorithmPlus).verify(token);
        System.out.println("IssueAt: " + decodeJwt.getIssuedAt());
        System.out.println("Claims: " + decodeJwt.getExpireAt());
        System.out.println("Claims: " + decodeJwt.getClaim("userName"));
        // 模拟 Token 过期后验证
        try {
            Thread.sleep(2000); // 模拟超过1小时
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean isExpired1 = Jwt.require().algorithm(algorithmPlus).verifyToken(token);
        System.out.println(new Date());
        System.out.println("Is Token expired? " + isExpired1);
        // 模拟 Token 过期后验证
        try {
            Thread.sleep(5000); // 模拟超过1小时
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean isExpired = Jwt.require().algorithm(algorithmPlus).verifyToken(token);
        System.out.println(new Date());
        System.out.println("Is Token expired? " + isExpired);
    }

}
