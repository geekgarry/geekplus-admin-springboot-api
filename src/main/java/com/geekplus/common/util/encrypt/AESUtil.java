package com.geekplus.common.util.encrypt;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * author     : geekplus
 * email      :
 * date       : 9/22/24 1:46 AM
 * description: //TODO
 */
public class AESUtil {
    private static final String ALGORITHM = "AES";
    private static final String UTF8 = "UTF-8";
    private static final String CIPHERALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String KEY = "m*e6refg#j97gp@0";
    private static final String SECRET_KEY = "9@#97plus*&$1jk7"; // 16/24/32 字符

    static AES des = new AES(Mode.ECB, Padding.PKCS5Padding, KEY.getBytes());

    public static String encryptHex(String str) {
        return des.encryptHex(str);
    }


    public static String decryptHex(String str) {
        return des.decryptStr(str, CharsetUtil.CHARSET_UTF_8);
    }

    // 加密
    public static String encrypt(String data) {
        try{
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        }catch (Exception e){
            return data;
        }
    }

    // 解密
    public static String decrypt(String encryptedData) {
        try{
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] originalData = cipher.doFinal(decodedData);
            return new String(originalData);
        }catch (Exception e){
            return encryptedData;
        }
    }

    public static String encryptStr(String sSrc) {
        if (KEY == null) {
            throw new IllegalArgumentException("sSrc不能为空");
        }
        // 判断Key是否为16位
        if (KEY.length() != 16) {
            throw new IllegalArgumentException("sKey长度需要为16位");
        }

        try {
            byte[] raw = KEY.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");

            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));

            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptStr(String sSrc) {
        try {
            byte[] raw = KEY.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
            //先用base64解密
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, StandardCharsets.UTF_8);
            return originalString;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        //根据指定字符串生成秘钥
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128,new SecureRandom("geekplus".getBytes()));
        SecretKey sk = kg.generateKey();
        // 将密钥转换为Base64编码的字符串
        String keyStr = Base64.getEncoder().encodeToString(sk.getEncoded());
        System.out.println(keyStr);
        System.out.println(encrypt("GarryChan"));
        System.out.println(decrypt("GarryChan"));
    }
}
