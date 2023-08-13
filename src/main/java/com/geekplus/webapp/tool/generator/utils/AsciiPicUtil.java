package com.geekplus.webapp.tool.generator.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @program: spring-boot-project-mybatis
 * @description: 字符画生成工具类
 * @author: GarryChan
 * @create: 2020-11-26 11:51
 **/
public class AsciiPicUtil {
    /**
     * @param path
     *            图片路径
     */
    public static void createAsciiPic(final String path) {
        final String base = "@#&$%*o!;.";// 字符串由复杂到简单
        try {
            final BufferedImage image = ImageIO.read(new File(path));
            for (int y = 0; y < image.getHeight(); y += 2) {
                for (int x = 0; x < image.getWidth(); x++) {
                    final int pixel = image.getRGB(x, y);
                    final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                    final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    final int index = Math.round(gray * (base.length() + 1) / 255);
                    System.out.print(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
                }
                System.out.println();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * test
     *
     * @param args
     */
    public static void main(final String[] args) {
        AsciiPicUtil.createAsciiPic("E:\\GEEKCJJFile\\我的素材文件\\1fccdcd813483d9bcb1b4389742d8ae2.jpg");
    }
}
