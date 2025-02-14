package com.geekplus.common.util.encrypt;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.geekplus.common.myexception.BusinessException;

import javax.crypto.Cipher;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 加密/解密工具
 *
 * @author geekplus
 * @date 2023-07-26 10:02:08
 */
public class RSAUtil {
    /**
     * 公钥文件名称
     */
    private final static String PUBLIC_KEY_FILE_NAME = "publicKey.pem";
    /**
     * 私钥文件名称
     */
    private final static String PRIVATE_KEY_FILE_NAME = "privateKey.pem";

    /**
     * 类型
     */
    public static final String ENCRYPT_TYPE = "RSA"; // RSA_ALGORITHM 加密方式

    private static final int DEFAULT_KEY_SIZE = 2048;

    /**
     * 生成RSA密钥对。
     *
     * @return KeyPair 包含公钥和私钥的密钥对。
     * @throws NoSuchAlgorithmException 如果RSA算法不可用，抛出此异常。
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // 实例化一个密钥对生成器，指定算法为RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ENCRYPT_TYPE);
        // 初始化密钥对生成器，指定密钥长度为2048位
        keyPairGenerator.initialize(2048); // 密钥大小为2048位
        // 生成密钥对
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 从文件中读取公钥
     *
     * @param filename 公钥保存路径，相对于classpath
     * @return  PublicKey 公钥对象
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) {
        byte[] bytes = new byte[0];
        try {
            bytes = readFileByte(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getPublicKey(bytes);
    }

    /**
     * 从文件中读取密钥
     *
     * @param filename 私钥保存路径，相对于classpath
     * @return  PrivateKey 私钥对象
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) {
        byte[] bytes = new byte[0];
        try {
            bytes = readFileByte(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getPrivateKey (bytes);
    }

    /**
     * 获取公钥
     * 公钥的字节形式。
     * @param bytes 公钥的字节形式
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(byte[] bytes) {
        bytes = Base64.getDecoder ( ).decode (bytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec (bytes);
        KeyFactory factory = null;
        try {
            factory = KeyFactory.getInstance ("RSA");
            return factory.generatePublic (spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取密钥
     *
     * @param bytes 私钥的字节形式
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(byte[] bytes) {
        bytes = Base64.getDecoder ( ).decode (bytes);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec (bytes);
        KeyFactory factory = null;
        try {
            factory = KeyFactory.getInstance ("RSA");
            return factory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用公钥加密数据。
     *
     * @param data 待加密的明文数据。
     * @param publicKey 公钥，用于加密数据。
     * @return String 加密后的数据，以Base64编码表示。
     * @throws Exception 如果加密过程中发生错误，抛出此异常。
     *
     * 此部分代码首先实例化一个Cipher对象，用于执行加密操作。它使用RSA算法，
     * 并初始化为加密模式。然后，使用提供的公钥将Cipher对象配置为准备加密状态。
     * 最后，将待加密的数据转换为字节数组，进行加密操作，并将加密后的数据
     * 使用Base64编码为字符串返回。
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 使用私钥解密数据。
     *
     * @param encryptedData 加密后的数据，以Base64编码表示。
     * @param privateKey 私钥，用于解密数据。
     * @return String 解密后的明文数据。
     * @throws Exception 如果解密过程中发生错误，抛出此异常。
     */
    public static String decrypt(String encryptedData, PrivateKey privateKey) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    //获取公钥(Base64编码)
    public static String getPublicKey() throws Exception {
        String publicKeyPath =
                RSAUtil.class.getClassLoader().getResource(PUBLIC_KEY_FILE_NAME).getFile();//路径中不能有中文
        return readFile(publicKeyPath);
    }

    //获取私钥(Base64编码)
    public static String getPrivateKey() throws Exception {
        String privateKeyPath =
                RSAUtil.class.getClassLoader().getResource(PRIVATE_KEY_FILE_NAME).getFile();//路径中不能有中文
        return readFile(privateKeyPath);
    }

    /**
     * 公钥加密
     *
     * @param content 内容
     * @return {@link String }
     * @author
     */
    public static String encrypt(String content) throws Exception {
        try {
            RSA rsa = new RSA(null, getPublicKey());
            return rsa.encryptBase64(content, KeyType.PublicKey);
        } catch (Exception e) {
            throw new BusinessException("加密失败") ;
        }
    }

    /**
     * 私钥解密
     *
     * @param content 内容
     * @return {@link String }
     * @author qs.zhang
     * @date 2023-07-26 16:09:12
     */
    public static String decrypt(String content) throws Exception {
        try {
            RSA rsa = new RSA(getPrivateKey(), null);
            return rsa.decryptStr(content, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new BusinessException("解密失败") ;
        }
    }

    /**
     * 根据密文，生存rsa公钥和私钥,并写入指定文件
     *
     * @param publicKeyFilename  公钥文件路径
     * @param privateKeyFilename 私钥文件路径
     * @param secret             生成密钥的密文
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret, int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance ("RSA");
            SecureRandom secureRandom = new SecureRandom(secret.getBytes());
            keyPairGenerator.initialize(Math.max(keySize, DEFAULT_KEY_SIZE), secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            // 获取公钥并写出
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            publicKeyBytes = Base64.getEncoder().encode(publicKeyBytes);
            writeFile(publicKeyFilename, publicKeyBytes);
            // 获取私钥并写出
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            privateKeyBytes = Base64.getEncoder().encode(privateKeyBytes);
            writeFile(privateKeyFilename, privateKeyBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private static byte[] readFileByte(String fileName) throws IOException {
        return Files.readAllBytes(new File(fileName).toPath());
    }

    private static void writeFile(String destPath, byte[] bytes) {
        File dest = new File(destPath);
        if (!dest.exists()) {
            try {
                dest.createNewFile();
                Files.write(dest.toPath(), bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取公私钥-请获取一次后保存公私钥使用
     *
     * @param publicKeyFilename  公钥生成的路径
     * @param privateKeyFilename 私钥生成的路径
     */
    public static void generateKeyPair(String publicKeyFilename, String privateKeyFilename) {
        try {
            String path = RSAUtil.class.getClassLoader().getResource("").getPath();

            KeyPair pair = SecureUtil.generateKeyPair(ENCRYPT_TYPE);
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();
            // 获取 公钥和私钥 的 编码格式（通过该 编码格式 可以反过来 生成公钥和私钥对象）
            byte[] pubEncBytes = publicKey.getEncoded();
            byte[] priEncBytes = privateKey.getEncoded();

            // 把 公钥和私钥 的 编码格式 转换为 Base64文本 方便保存
            String pubEncBase64 = Base64.getEncoder().encodeToString(pubEncBytes);
            String priEncBase64 = Base64.getEncoder().encodeToString(priEncBytes);

            FileWriter pub = new FileWriter(publicKeyFilename);
            FileWriter pri = new FileWriter(privateKeyFilename);

            pub.write(pubEncBase64);
            pri.write(priEncBase64);

            pub.flush();
            pub.close();

            pri.flush();
            pri.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件路径读取文件内容
     *
     * @param fileInPath
     * @throws IOException
     */
    public static String readFile(@NotNull Object fileInPath) throws IOException {
        BufferedReader br = null;
        if (fileInPath == null) {
            return null;
        }
        if (fileInPath instanceof String) {
            br = new BufferedReader(new FileReader(new File((String) fileInPath)));
        } else if (fileInPath instanceof InputStream) {
            br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
        }
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        br.close();
        return buffer.toString();
    }
}
