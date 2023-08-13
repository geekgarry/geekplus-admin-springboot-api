package com.geekplus.common.util;

import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName MatrixToImageWriter
 * @Description 二位码生成
 * @Author Zheng
 * @Date 2017年10月31日 下午4:09:22
 */
public class MatrixToImageWriter {

	private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private MatrixToImageWriter(){

    }

    /**
     * 根据二位数组，生成图片
     * @param matrix
     * @return
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix){
    	int width = matrix.getWidth();
    	int height = matrix.getHeight();
    	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	for(int x = 0; x < width; x++){
    		for(int y = 0; y < height; y++){
    			image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
    		}
    	}
    	return image;
    }

    /**
     * 根据一定格式，写出文件
     * @param matrix
     * @param format
     * @param file
     * @throws IOException
     */
    public static void writeToFile(BitMatrix matrix,String format,File file) throws IOException{
    	BufferedImage image = toBufferedImage(matrix);
    	if(!ImageIO.write(image, format, file)){
    		throw new IOException("Could not write an image of format "+format+" to "+file);
    	}
    }

    /**
     * 形成输出流
     * @param matrix
     * @param format
     * @param stream
     * @throws IOException
     */
    public static void writeToStream(BitMatrix matrix,String format,OutputStream stream) throws IOException{
    	BufferedImage image = toBufferedImage(matrix);
    	if(!ImageIO.write(image, format, stream)){
    		throw new IOException("Could not write an image of format "+format);
    	}
    }

}
