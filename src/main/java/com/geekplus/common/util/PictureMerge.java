package com.geekplus.common.util;

import com.geekplus.common.domain.FontText;
import com.geekplus.common.util.encrypt.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName: PictureMerge
 * @Description: 图片合成
 * @author: Garry
 * @date: 2018年8月30日 下午5:39:59
 */
public class PictureMerge {

	private static final Logger logger = LoggerFactory.getLogger(PictureMerge.class);

	//private static final String RELATIVE_PARENT_PATH = "src/main/resources/";
	//private static final String RELATIVE_PARENT_PATH = "F:/resources/";
	private static final String RELATIVE_PARENT_PATH = "/home/resources/";
	private static final String RELATIVE_TEMPLATE_PATH = RELATIVE_PARENT_PATH + "shareTemplate.png";
	private static final StringBuffer sb = new StringBuffer();

	private static FontText fontText = new FontText();
//    private static QiniuUtil qiniuUtil = new QiniuUtil();
	/**
	 * @param fileUrl
	 *            文件绝对路径或相对路径
	 * @return 读取到的缓存图像
	 * @throws IOException
	 *             路径错误或者不存在该文件时抛出IO异常
	 */
	private static BufferedImage getBufferedImage(String fileUrl) throws IOException {
		File f = new File(fileUrl);
		return ImageIO.read(f);
	}

	private static BufferedImage getBufferedImage(InputStream is) throws IOException {
		return ImageIO.read(is);
	}

	/**
	 * 获取网络图片流
	 *
	 * @param url
	 * @return
	 */
	private static InputStream getInputStreamByGet(String url) {
		InputStream inputStream = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setReadTimeout(5000);
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
				return inputStream;
			}
		} catch (IOException e) {
			logger.error("获取网络图片出现异常，图片路径为：{}", url);
			logger.error(e.getMessage());
		}
		return inputStream;
	}

	/**
	 * @param savedImg
	 *            待保存的图像
	 * @param saveDir
	 *            保存的目录
	 * @param fileName
	 *            保存的文件名，必须带后缀，比如 "beauty.jpg"
	 * @param format
	 *            文件格式：jpg、png或者bmp
	 * @return
	 */
	private static boolean saveImage(BufferedImage savedImg, String saveDir, String fileName, String format) {
		boolean flag = false;
		/**
		 * 先检查保存的图片格式是否正确
		 */
		String[] legalFormats = { "jpg", "JPG", "png", "PNG", "bmp", "BMP" };
		int i = 0;
		for (i = 0; i < legalFormats.length; i++) {
			if (format.equals(legalFormats[i])) {
				break;
			}
		}
		/**
		 * 图片格式不支持
		 */
		if (i == legalFormats.length) {
			logger.info("不是保存所支持的图片格式!");
			return false;
		}

		/**
		 * 再检查文件后缀和保存的格式是否一致
		 */
		String postfix = fileName.substring(fileName.lastIndexOf('.') + 1);
		if (!postfix.equalsIgnoreCase(format)) {
			logger.info("待保存文件后缀和保存的格式不一致!");
			return false;
		}

		String fileUrl = saveDir + fileName;
		File file = new File(fileUrl);
		try {
			flag = ImageIO.write(savedImg, format, file);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return flag;
	}

	/**
	 * 待合并的两张图必须满足这样的前提，如果水平方向合并，则高度必须相等；如果是垂直方向合并，宽度必须相等。
	 * mergeImage方法不做判断，自己判断。
	 *
	 * @param img1
	 *            待合并的第一张图
	 * @param img2
	 *            带合并的第二张图
	 * @param isHorizontal
	 *            为true时表示水平方向合并，为false时表示垂直方向合并
	 * @return 返回合并后的BufferedImage对象
	 * @throws IOException
	 */
	public static BufferedImage mergeImage(BufferedImage img1, BufferedImage img2, boolean isHorizontal, int startX,
			int startY) {
		int w1 = img1.getWidth();
		int h1 = img1.getHeight();
		int w2 = img2.getWidth();
		int h2 = img2.getHeight();

		/**
		 * 从图片中读取RGB
		 */
		int[] imageArrayOne = new int[w1 * h1];
		/**
		 * 逐行扫描图像中各个像素的RGB到数组中
		 */
		imageArrayOne = img1.getRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
		int[] imageArrayTwo = new int[w2 * h2];
		imageArrayTwo = img2.getRGB(0, 0, w2, h2, imageArrayTwo, 0, w2);

		/**
		 * 生成新图片
		 */
		BufferedImage destImage = null;
		/**
		 * 水平方向合并
		 */
		if (isHorizontal) {
			destImage = new BufferedImage(w1, h1, BufferedImage.TYPE_INT_RGB);
			/**
			 * 设置上半部分或左半部分的RGB
			 */
			destImage.setRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
			/**
			 * 设置下半部分的RGB
			 */
			destImage.setRGB(startX, startY, w2, h2, imageArrayTwo, 0, w2);
		} else {
			/**
			 * 垂直方向合并
			 */
			destImage = new BufferedImage(w1, h1 + h2, BufferedImage.TYPE_INT_RGB);
			/**
			 * 设置上半部分或左半部分的RGB
			 */
			destImage.setRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
			/**
			 * 设置下半部分的RGB
			 */
			destImage.setRGB(0, h1, w2, h2, imageArrayTwo, 0, w2);
		}
		return destImage;
	}

	/**
	 * <p>
	 * Title: getImageStream
	 * </p>
	 * <p>
	 * Description: 获取图片InputStream
	 * </p>
	 *
	 * @param destImg
	 * @return
	 */
	public static InputStream getImageStream(BufferedImage destImg) {
		InputStream is = null;

		BufferedImage bi = destImg;

		ByteArrayOutputStream bs = new ByteArrayOutputStream();

		ImageOutputStream imOut;
		try {
			imOut = ImageIO.createImageOutputStream(bs);

			ImageIO.write(bi, "png", imOut);

			is = new ByteArrayInputStream(bs.toByteArray());

		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return is;
	}

	/**
	 * <p>
	 * Title: drawTextInImg
	 * </p>
	 * <p>
	 * Description: 图片上添加文字业务需求要在图片上添加水
	 * </p>
	 *
	 * @param bimage
	 * @param text
	 * @param left top
	 */
	public static BufferedImage drawTextInImg(BufferedImage bimage, FontText text, int left, int top) {
		Graphics2D graphics2D = bimage.createGraphics();
		graphics2D.setColor(getColor(text.getWm_text_color()));
		graphics2D.setBackground(Color.BLACK);
		Font font = new Font(text.getWm_text_font(), Font.BOLD, text.getWm_text_size());
		graphics2D.setFont(font);
		graphics2D.drawString(text.getText(), left, top);
		graphics2D.dispose();
		return bimage;
	}

	/**
	 * 绘制图片
	 *
	 * @Title: drawExtraImgInImg
	 * @Description:
	 * @param bimage
	 * @param img
	 * @param left
	 * @param top
	 * @return BufferedImage
	 * @author Garry
	 * @date 2018年8月31日上午10:00:35
	 */
	public static BufferedImage drawExtraImgInImg(BufferedImage bimage, Image img, int left, int top) {
		Graphics2D g = bimage.createGraphics();
		g.setBackground(Color.white);
		g.drawImage(img, left, top, null);
		g.dispose();
		return bimage;
	}

	public static BufferedImage drawExtraImgInImg(BufferedImage bimage, Image img, int left, int top, int width,
			int height) {
		Graphics2D g = bimage.createGraphics();
		g.setBackground(Color.white);
		g.drawImage(img, left, top, width, height, null);
		g.dispose();
		return bimage;
	}

	private static Color getColor(String color) {
		// color #2395439
		if (color.charAt(0) == '#') {
			color = color.substring(1);
		}
		if (color.length() != 6) {
			return null;
		}
		try {
			int r = Integer.parseInt(color.substring(0, 2), 16);
			int g = Integer.parseInt(color.substring(2, 4), 16);
			int b = Integer.parseInt(color.substring(4), 16);
			return new Color(r, g, b);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	/**
	 * 对图片进行强制放大或缩小
	 *
	 * @param originalImage
	 *            原始图片
	 * @return
	 */
	public static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());

		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return newImage;
	}

	/**
	 * 拼接文字信息
	 */
	private static void appendTxtInfo(BufferedImage destImg, String logoTxt, String productTitle, String productPrice) {
		long txtInfoTime = System.currentTimeMillis();
		/**
		 * 首先添加所有的文字
		 */
		appendTxtInfoLogo(destImg, logoTxt);
		appendTxtInfoProduct(destImg, productTitle);
		appendTxtInfoPrice(destImg, productPrice);
		logger.info("添加文字信息总消耗: {} 毫秒", System.currentTimeMillis() - txtInfoTime);
	}

	/**
	 * 拼接logo文字信息
	 *
	 * @Title: appendTxtInfoLogo
	 * @Description:
	 * @param destImg
	 * @param logoTxt
	 *            void
	 * @author Garry
	 * @date 2018年8月31日下午4:07:09
	 */
	private static void appendTxtInfoLogo(BufferedImage destImg, String logoTxt) {
		/**
		 * logo文字相关参数 介绍 冬青w3 23px #fff 104,102
		 */
		long txtInfoTime1 = System.currentTimeMillis();
		fontText.setText(logoTxt);
		fontText.setWm_text_color("#fff");
		fontText.setWm_text_font("黑体，Arial");
		fontText.setWm_text_pos(0);
		fontText.setWm_text_size(23);
		drawTextInImg(destImg, fontText, 104, 102);
		logger.info("添加文字信息1消耗: {} 毫秒", System.currentTimeMillis() - txtInfoTime1);
	}

	/**
	 * 拼接产品名称信息
	 *
	 * @Title: appendTxtInfoProduct
	 * @Description:
	 * @param destImg
	 * @param productTitle
	 *            void
	 * @author Garry
	 * @date 2018年8月31日下午4:07:30
	 */
	private static void appendTxtInfoProduct(BufferedImage destImg, String productTitle) {
		/**
		 * 商品标题 字体 冬青 w6 41px #c63535 38,190 777,231
		 */
		long txtInfoTime2 = System.currentTimeMillis();
		fontText.setText(productTitle);
		fontText.setWm_text_color("#c63535");
		fontText.setWm_text_size(41);
		drawTextInImg(destImg, fontText, 38, 190);
		logger.info("添加文字信息2消耗: {} 毫秒", System.currentTimeMillis() - txtInfoTime2);
	}

	/**
	 * 拼接产品价格信息
	 *
	 * @Title: appendTxtInfoPrice
	 * @Description:
	 * @param destImg
	 * @param productPrice
	 *            void
	 * @author Garry
	 * @date 2018年8月31日下午4:08:01
	 */
	private static void appendTxtInfoPrice(BufferedImage destImg, String productPrice) {
		/**
		 * 价格 573,1210 39px
		 */
		long txtInfoTime3 = System.currentTimeMillis();
		fontText.setText(productPrice);
		fontText.setWm_text_color("#fff");
		fontText.setWm_text_size(39);
		drawTextInImg(destImg, fontText, 573, 1210);
		logger.info("添加文字信息3消耗: {} 毫秒", System.currentTimeMillis() - txtInfoTime3);
	}
	/**
	 * 判断文件是否存在
	* @Title: isExistFile
	* @Description:
	* @param fileName
	* @param relativePath
	* @return Boolean
	* @author Garry
	* @date 2018年8月31日下午5:25:39
	 */
    private static Boolean  isExistFile(String fileName,String relativePath){
    	Boolean isExist = false;
    	if(null != fileName ){
    		File file = new File(RELATIVE_PARENT_PATH+relativePath+'/'+fileName);
    		isExist = file.exists();
    	}
    	return isExist;
    }
    /**
     * 获取文件名
    * @Title: getUrlFileName
    * @Description:
    * @param urlPath
    * @return String
    * @author Garry
    * @date 2018年8月31日下午5:31:25
     */
    private static String getUrlFileName(String urlPath){
    	return urlPath.substring(urlPath.lastIndexOf('/'));
    }
    /**
     * 保存远程图片文件到本地指定目录 文件命名同名
     * @param remotePath 远程路径地址
     * @param curPath 保存路径文件夹路径 ps text/exp1
     * @return 保存的本地文件路径
     */
    public static String saveImgToLocalFromRemote(String remotePath,String curPath) {
    	long logoImgTime = System.currentTimeMillis();
		Boolean isExit = false;
		String logoName = getUrlFileName(remotePath);
		if(null == logoName || "".equals(logoName.trim())){
			logoName = "logo.jpg";
		}
		isExit = isExistFile(logoName, curPath);
		String logoFilePath = RELATIVE_PARENT_PATH+curPath+"/"+logoName;
		String format = logoName.substring(logoName.lastIndexOf('.')+1);
		BufferedImage logoImg = null;
		/**
		 * 先判断是否存在该文件
		 */
		if(isExit){
			/**
			 * 存在
			 */
			logger.info("文件存在直接读取");
			try {
				logoImg = getBufferedImage(logoFilePath);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}else{
			/**
			 * 不存在 需要保存到本地
			 */
			logger.info("文件不存在从远程下载保存到本地");
			try {
				logoImg = getBufferedImage(getInputStreamByGet(remotePath));
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			saveImage(logoImg, RELATIVE_PARENT_PATH+curPath+"/", logoName, format);
		}
		logger.info("存储文件到本地耗时: {} 毫秒", System.currentTimeMillis() - logoImgTime);
		return logoFilePath;
    }
	/**
	 * 拼接logoimg
	 *
	 * @Title: appendLogoImg
	 * @Description:
	 * @param destImg
	 *            void
	 * @author Garry
	 * @date 2018年8月31日下午4:08:19
	 */
	private static BufferedImage appendLogoImg(BufferedImage destImg, String logoImgPath) {
		BufferedImage bfi = null;
		long logoImgTime = System.currentTimeMillis();
		Boolean isExit = false;
		String logoName = getUrlFileName(logoImgPath);
		if(null == logoName || "".equals(logoName.trim())){
			logoName = "logo.jpg";
		}
		isExit = isExistFile(logoName, "logo");
		String logoFilePath = RELATIVE_PARENT_PATH+"logo/"+logoName;
		String format = logoName.substring(logoName.lastIndexOf('.')+1);
		BufferedImage logoImg = null;
		/**
		 * 先判断是否存在该文件
		 */
		if(isExit){
			/**
			 * 存在
			 */
			try {
				logoImg = getBufferedImage(logoFilePath);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}else{
			try {
				logoImg = getBufferedImage(getInputStreamByGet(logoImgPath));
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			if (null != logoImg) {
				logoImg = zoomInImage(logoImg, 58, 58);
			}
		}
		bfi = drawExtraImgInImg(destImg, logoImg, 34, 53);
		if(!isExit){
			/**
			 * 不存在 需要保存到本地
			 */
			saveImage(logoImg, RELATIVE_PARENT_PATH+"logo/", logoName, format);
		}
		logger.info("缩放logo图片并绘制消耗: {} 毫秒", System.currentTimeMillis() - logoImgTime);
		return bfi;
	}

	/**
	 * 拼接商品图片
	 *
	 * @Title: appendProductImg
	 * @Description:
	 * @param destImg
	 *            void
	 * @author Garry
	 * @date 2018年8月31日下午4:08:35
	 */
	private static void appendProductImg(BufferedImage destImg, String productPath) {
		long productTime = System.currentTimeMillis();
		Integer productWidhtAndHeight = 800;
		Boolean isExit = false;
		String productName = getUrlFileName(productPath);
		if(null == productName || "".equals(productName.trim())){
			productName = "product.png";
		}
		isExit = isExistFile(productName, "product");
		String productFilePath = RELATIVE_PARENT_PATH+"product/"+productName;
		String format = productName.substring(productName.lastIndexOf('.')+1);
		BufferedImage productImg = null;
		/**
		 * 判断本地是否存在该商品图片
		 */
		if(isExit){
			try {
				productImg = getBufferedImage(productFilePath);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}else{
			try {
				productImg = getBufferedImage(getInputStreamByGet(productPath));
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		if (null == productImg) {
			return;
		}
		Integer productWidth = productImg.getWidth();
		Integer productHeight = productImg.getHeight();
		Double whRate = Double.valueOf(productHeight) / Double.valueOf(productWidth);

		int nowHeight = (int) (whRate * productWidhtAndHeight);
		productImg = zoomInImage(productImg, productWidhtAndHeight, nowHeight);
		Integer relativeFixPx = 0;
		Integer fixedRelaPx = (809 - productWidhtAndHeight) / 2;
		if (nowHeight < productWidhtAndHeight) {
			relativeFixPx = (productWidhtAndHeight - nowHeight) / 2;
		} else {
			nowHeight = productWidhtAndHeight;
			productImg = drawExtraImgInImg(new BufferedImage(productWidhtAndHeight, productWidhtAndHeight, productImg.getType()),
					productImg, 0, (nowHeight - productWidhtAndHeight) / 2, productWidhtAndHeight,
					productWidhtAndHeight);
		}
		drawExtraImgInImg(destImg, productImg, 0 + fixedRelaPx, 314 + fixedRelaPx + relativeFixPx,
				productWidhtAndHeight, nowHeight);
		if(!isExit){
			/**
			 * 不存在 需要保存到本地
			 */
			saveImage(productImg, RELATIVE_PARENT_PATH+"product/", productName, format);
		}
		logger.info("剪裁缩放商品图片并绘制消耗: {} 毫秒", System.currentTimeMillis() - productTime);
	}

	/**
	 * 拼接二维码信息 按照 商品名称进行命名 查询该文件是否存在 若存在直接进行读取 不重复生成
	 *
	 * @Title: appendQRCode
	 * @Description:
	 * @param destImg
	 *            void
	 * @author Garry
	 * @date 2018年8月31日下午4:08:46
	 */
	private static void appendQRCode(BufferedImage destImg, String codeUrl) {
		long qrcodeTime = System.currentTimeMillis();
		String fileName = getUrlFileName(codeUrl).hashCode()+".png";
		String qrcodeUrl = RELATIVE_PARENT_PATH + "qrcode/" + fileName;
		if(!isExistFile(fileName, "qrcode")){
			try {
				QRUtil.generateQRCode(codeUrl, 225, 225, "png", qrcodeUrl);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		BufferedImage qrcodeImg = null;
		try {
			qrcodeImg = getBufferedImage(qrcodeUrl);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		drawExtraImgInImg(destImg, qrcodeImg, 35, 1142);
		logger.info("生成二维码并绘制消耗: {} 毫秒", System.currentTimeMillis() - qrcodeTime);
	}
	/**
	 * 获取分享图片的名称  根据传入的参数名称进行md5加密
	* @Title: getShareImgName
	* @Description:
	* @param logoTxt
	* @param productTitle
	* @param productPrice
	* @param codeUrl
	* @param logoImgPath
	* @param productPath
	* @return String
	* @author Garry
	* @date 2018年9月1日上午8:49:21
	 */
    public static String  getShareImgName(String logoTxt,String productTitle,String productPrice,String codeUrl,String logoImgPath,String productPath){
    	sb.setLength(0);
    	sb.append(logoTxt);
    	sb.append('_');
    	sb.append(productTitle);
    	sb.append('_');
    	sb.append(productPrice);
    	sb.append('_');
    	sb.append(codeUrl);
    	sb.append('_');
    	sb.append(logoImgPath);
    	sb.append('_');
    	sb.append(productPath);
    	return EncryptUtil.MD5(sb.toString());
    }
}
