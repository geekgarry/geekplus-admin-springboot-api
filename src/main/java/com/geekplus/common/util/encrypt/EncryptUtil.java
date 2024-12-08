package com.geekplus.common.util.encrypt;

import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.util.base64.Base64Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/13/23 06:07
 * description: 做什么的？
 */
public class EncryptUtil {

    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    public static String toHexString(byte[] data) {
        if(data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length*2);
        for ( byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static String generateMd5Value(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            // 这个可具体更改,现在做例子不动了,可以自定义异常类进行抛出提醒
            throw new BusinessException("生成Token失败", e);
        }
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f" };

    /**
     * Base64 encode
     */
    public static String base64Encode(String data) {
        return Base64.encodeBase64String(data.getBytes());
    }

    /**
     * Base64 decode
     *
     * @throws UnsupportedEncodingException
     */
    public static String base64Decode(String data) throws UnsupportedEncodingException {
        return new String(Base64.decodeBase64(data.getBytes()), "utf-8");
    }

    /**
     * md5
     */
    public static String md5Hex(String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * sha1
     */
    public static String sha1Hex(String data) {
        return DigestUtils.sha1Hex(data);
    }

    /**
     * sha256
     */
    public static String sha256Hex(String data) {
        return DigestUtils.sha256Hex(data);
    }

    /**
     * MD5加密
     *
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 随机生成 salt 需要指定 它的字符串的长度
     *
     * @param len 字符串的长度
     * @return salt
     */
    public static String generateSalt(int len) {
        //一个Byte占两个字节
//        byte[] byteLen=new byte[1];
//        byteLen[0] = (byte)(len >> 1);
//        Random secureRandom = new Random();
//        return secureRandom.nextBytes(byteLen).toHex();
        return null;
    }

    /**
     * 获取加密后的密码，使用默认hash迭代的次数 1 次
     *
     * @param hashAlgorithm hash算法名称 MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512、etc。
     * @param password      需要加密的密码
     * @param salt          盐
     * @return 加密后的密码
     */
    public static String encryptPassword(String hashAlgorithm, String password, String salt) {
        return encryptPassword(hashAlgorithm, password, salt, 1);
    }

    /**
     * 获取加密后的密码，需要指定 hash迭代的次数
     *
     * @param hashAlgorithm  hash算法名称 MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512、etc。
     * @param password       需要加密的密码
     * @param salt           盐
     * @param hashIterations hash迭代的次数
     * @return 加密后的密码
     */
    public static String encryptPassword(String hashAlgorithm, String password, Object salt, int hashIterations) {
        SimpleHash hash = new SimpleHash(hashAlgorithm, password, salt, hashIterations);
        return hash.toString();
    }

    public static String md5EncryptPwd(String passWord){
        String hashAlgorithmName = "MD5";
        ByteSource credentialsSalt = ByteSource.Util.bytes("plus");
        int hashIterations = 1024;
        Object obj = new SimpleHash(hashAlgorithmName, passWord, credentialsSalt, hashIterations);
        return obj.toString();
    }

    public static String customerSHAEncryptPwd(String passWord){
        String hashAlgorithmName = "SHA";
        ByteSource credentialsSalt = ByteSource.Util.bytes("customer-plus");
        int hashIterations = 2048;
        encryptPassword(hashAlgorithmName,passWord,credentialsSalt,hashIterations);
        Object obj = encryptPassword(hashAlgorithmName,passWord,credentialsSalt,hashIterations);
        return Base64Util.encode(obj.toString());
    }

    /**
     * md5加密
     * @param key
     * @return
     */
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
