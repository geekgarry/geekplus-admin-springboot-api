/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/17/23 23:41
 * description: 做什么的？
 */
package com.geekplus.common.util.image;

import cn.hutool.core.codec.Base64;
import com.geekplus.common.config.WebAppConfig;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.util.MatrixToImageWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

public class ImageUtil {

    /**
     * 图片生成工具
     * @param url
     * @return
     * @throws Exception
     */
    public static BufferedImage createImage(String url) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, Constant.UTF8);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE,
                Constant.QRCODE_SIZE, Constant.QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * text 扫描二维码展示的内容,width宽度，height长度
     * @param text
     * @param width
     * @param height
     * @return
     */
    public static String generateQRCodeImage(String text, int width, int height) {
        String Base64ImageStr="";
        try{
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            //Path path = FileSystems.getDefault().getPath("3.png");

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            //MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            Base64ImageStr = Base64.encode(pngData);
        } catch (WriterException | IOException e){
            e.printStackTrace();
        }
        return Base64ImageStr;
    }

    /**
      * @Author geekplus
      * @Description //计算图片清晰度
      * @Param [image]
      * @Throws
      * @Return {@link double}
      */
    public static double calculateSharpness(BufferedImage image) {
        double sum = 0;
        double count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                int gray = (red + green + blue) / 3;
                sum += gray;
                count++;
            }
        }
        double mean = sum / count;
        double variance = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                int gray = (red + green + blue) / 3;
                variance += Math.pow(gray - mean, 2);
            }
        }
        variance /= count;
        double sharpness = Math.sqrt(variance);
        return sharpness;
    }
}
