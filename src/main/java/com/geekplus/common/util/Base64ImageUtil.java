package com.geekplus.common.util;

//import sun.misc.BASE64Decoder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

/**
 * Base64工具转换成图片
 * @author WeiZheng
 *
 */
public class Base64ImageUtil {

//	private static final String PATH_PARENT = "src/main/resources/base64/";
	private static final String PATH_PARENT = "/home/resources/base64/";

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	String fileName = "temp.png";
//        String con = getPicBASE64("src/main/resources/"+fileName);
//        System.out.println("---------------BASE64----------");
//    	System.out.println(con);
    	//data:image/png;base64,
//        getPicFormatBASE64(con,fileName);
    }
	 /**
     * 图片BASE64 编码
     */
    public static String getPicBASE64(String picPath) {
        String content = null;
        try {
            FileInputStream fileForInput = new FileInputStream(picPath);
            byte[] bytes = new byte[fileForInput.available()];
            fileForInput.read(bytes);
            // 解密，解密的结果是一个byte数组
            Base64.Encoder encoder = Base64.getEncoder();
            //content = new BASE64Encoder().encode(bytes); // 具体的编码方法
            content = encoder.encodeToString(bytes); // 具体的编码方法
            fileForInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * base64解码
     * @param base64Str
     * @param fileName
     * @return 文件相对路径
     */
    public static String getPicFormatBASE64(String base64Str, String fileName) {
    	fileName = PATH_PARENT+fileName;
        try {
            // 解密，解密的结果是一个byte数组
            Base64.Decoder decoder = Base64.getDecoder();
//            byte[] result = new BASE64Decoder().decodeBuffer(base64Str.trim());
            byte[] result = decoder.decode(base64Str.trim());
//            RandomAccessFile inOut = new RandomAccessFile(picPath, "rw"); // r,rw,rws,rwd
//            inOut.write(result);
//            inOut.close();
            for(int i=0;i<result.length;++i)
            {
                if(result[i]<0)
                {//调整异常数据
                	result[i]+=256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(fileName);
            out.write(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileName;
    }
}
