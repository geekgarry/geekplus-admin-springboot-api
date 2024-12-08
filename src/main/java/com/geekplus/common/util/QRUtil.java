package com.geekplus.common.util;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName QRGenerate
 * @Description 二维码生成/解析工具
 * @Author GeekPlus
 * @Date 2017年10月31日 下午4:52:37
 */
public class QRUtil {
    /**
     * QR生成
     * @param text
     * @param width
     * @param height
     * @param format
     * @return
     * @throws Exception
     */
	public static String generateQRCode(String text,int width,int height,String format)throws Exception{
		Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);
		String pathName = "D:/new.png";
		File outputFile = new File(pathName);
		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
		return pathName;
	}
	/**
	 *
	* @Title: generateQRCode
	* @Description:
	* @param text 内容
	* @param width 宽度
	* @param height 高度
	* @param format 格式 png
	* @param pathName 存储路径地址 加上文件名
	* @return
	* @throws Exception String
	* @author WeiZheng
	* @date 2018年8月31日上午10:15:05
	 */
	public static String generateQRCode(String text,int width,int height,String format,String pathName)throws Exception{
		Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);
		File outputFile = new File(pathName);
		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
		return pathName;
	}
	public static String parseQRCode(String filePath){
		String content = "error";
		try{
			File file =new File(filePath);
			BufferedImage image = ImageIO.read(file);
			// 根据图片生成解析源
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			// 二进制解析器
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			// 设置二位码附加属性 例如编码格式
			Map<DecodeHintType,Object> hints = new HashMap<>();
			hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
			MultiFormatReader formatReader = new MultiFormatReader();
			Result result =formatReader.decode(binaryBitmap, hints);

			System.out.println("result为:"+result.toString());
			// 条形码格式
			System.out.println("resultFormat为:"+result.getBarcodeFormat());
			System.out.println("resultText为:"+result.getText());
			// 设置返回值
			content = result.getText();
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}
	/**
	 * 生成随机字母数字组合
	 * @param length
	 * @return
	 */
	public  static String generateNumCode(int length) {
        String val = "";
        String charStr = "char";
        String numStr = "num";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? charStr : numStr;
            //输出字母还是数字
            if (charStr.equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if (numStr.equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
	/**
	 * result为:HTTPS://QR.ALIPAY.COM/FKX06202EF2MDXESJUPTBB
       resultFormat为:QR_CODE
       resultText为:HTTPS://QR.ALIPAY.COM/FKX06202EF2MDXESJUPTBB
                    解析的二维码的内容:HTTPS://QR.ALIPAY.COM/FKX06202EF2MDXESJUPTBB
                    这是支付宝付款码的解析 ，支付宝会生成一个付款链接 访问的话 会触发后续操作
	 * @param args
	 */
	public static void main(String[] args) {
		String pathName = "D:/weixin.png";
		String content = parseQRCode(pathName);
		System.out.println("解析的二维码的内容:"+content);
	}

	/**
	 * 进行二维码的生成和解析测试
	 */
	public static void test(){
		String text = generateNumCode(12);
		System.out.println("随机生成的12位验证码:"+text);

		// 设置二维码图片属性
		int width = 100;
		int height = 100;
		String format = "png";

		try{
			// 生成二维码图片
			String pathName = generateQRCode(text, width, height, format);
			System.out.println("生成二维码图片路径:"+pathName);

			String content = parseQRCode(pathName);
			System.out.println("解析的二维码的内容:"+content);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void test2(){
		// 设置二维码图片属性
		int width = 100;
		int height = 100;
		String format = "png";
		try {
			generateQRCode("http://106.14.139.191:8080/", width, height, format);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void test3(){
//		String pathName = "D:/new3.jpg";
		String pathName = "D:/weixin.png";
		String content = parseQRCode(pathName);
		System.out.println("解析的二维码的内容:"+content);
	}
}
