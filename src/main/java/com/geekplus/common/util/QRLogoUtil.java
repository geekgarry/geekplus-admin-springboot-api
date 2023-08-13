//package com.geekplus.common.util;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 二维码+logo+文字生成工具类
// *
// * @author zheng
// *
// */
//public class QRLogoUtil {
//
//	private static final Logger logger = LoggerFactory.getLogger(QRLogoUtil.class);
//	private static final int QRCOLOR = 0xFF000000; // 默认是黑色
//	private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色
//	//private static final String RELATIVE_PARENT_PATH = "src/main/resources/";
//	//private static final String RELATIVE_PARENT_PATH = "F:/resources/";
//	private static final String RELATIVE_PARENT_PATH = "/home/resources/";
//	private static final String SHARE_QR_CODE_IMG_PATH = "shareCode/";
//	private static final String INVITE_HOST_BASE = "http://h5.juheyinhang.com/customer/yaoqing/index.html?s=";// 邀请主机地址
//	private static final String QR_LOGO_DEFAULT = "http://cdn.juheyinhang.com/logo.png";
//	private static final String QR_TEXT_DEFAULT = "扫描加入中微直供";
//	private static StringBuilder sb = new StringBuilder();
////	private static QiniuUtil qiniuUtil = new QiniuUtil();
//
//	public static void main(String[] args) throws WriterException {
//		try {
//			getLogoQRCode("8888888888", QR_LOGO_DEFAULT, QR_TEXT_DEFAULT);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//    public static String getDefaultLogoQRCode(String shareCode) {
//    	return getLogoQRCode(shareCode,QR_LOGO_DEFAULT, "");
//    }
//    public static String getSelfDefaultLogoQRCode(String shareCode,String logoUrl) {
//    	if(null == logoUrl || "".equals(logoUrl.trim())) {
//    		logoUrl = QR_LOGO_DEFAULT;
//    	}
//    	return getLogoQRCode(shareCode,logoUrl, "");
//    }
//	/**
//	 * 生成logo二维码
//	 *
//	 * @param shareCode 分享码
//	 * @param logoUrl   中间图片地址 网络
//	 * @param logoTxt   二维码下方提示字符
//	 * @return
//	 */
//	public static String getLogoQRCode(String shareCode, String logoUrl, String logoTxt) {
//		String logoName = getShareCodeImgFileName(shareCode, logoUrl, logoTxt)+".png";
//		String shareImgPath = RELATIVE_PARENT_PATH + SHARE_QR_CODE_IMG_PATH + logoName;
//		File shareImg = new File(shareImgPath);
//		if (shareImg.exists()) {
//			logger.info(" {} 图片已经生成过，不用生成。", logoName);
//			return "http://cdn.juheyinhang.com/" + logoName;
//		}
//		long startTime = System.currentTimeMillis();
//		BufferedImage logoBufImg = getImgFromUrl(logoUrl);
//		if (null == logoBufImg) {
//			logger.warn("加载头像数据流失败");
//			return null;
//		}
//		String content = INVITE_HOST_BASE + shareCode;
//		try {
//			QRLogoUtil zp = new QRLogoUtil();
//			BufferedImage bim = zp.getQRCodeBufferedImage(content, BarcodeFormat.QR_CODE, 400, 400,
//					zp.getDecodeHintType());
//			bim = zp.addLogoQRCode(bim, logoBufImg, new LogoConfig(), logoTxt);
//			if (null == bim) {
//				logger.warn("拼接二维码图片文字失败");
//				return null;
//			}
//			saveImage(bim, RELATIVE_PARENT_PATH + SHARE_QR_CODE_IMG_PATH, logoName, "png");
//			/**
//			 * 上传到七牛云
//			 */
//			try {
//				logoName = qiniuUtil.uploadImg(new FileInputStream(new File(shareImgPath)), logoName);
//			} catch (FileNotFoundException e) {
//				logger.error(e.getMessage());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		logger.info("生成logo二维码图片处理消耗: {} 毫秒", System.currentTimeMillis() - startTime);
//		return logoName;
//	}
//
//	private static String getShareCodeImgFileName(String shareCode, String logoUrl, String logoTxt) {
//		sb.setLength(0);
//		sb.append(shareCode);
//		sb.append("_");
//		sb.append(logoUrl);
//		sb.append("_");
//		sb.append(logoTxt);
//		return StringUtil.MD5(sb.toString());
//	}
//
//	public static BufferedImage getImgFromUrl(String logoImgPath) {
//		long logoImgTime = System.currentTimeMillis();
//		Boolean isExit = false;
//		String logoName = getUrlFileName(logoImgPath);
//		if (null == logoName || "".equals(logoName.trim())) {
//			logoName = "logo.jpg";
//		}
//		isExit = isExistFile(logoName, "logo");
//		String logoFilePath = RELATIVE_PARENT_PATH + "logo/" + logoName;
//		String format = logoName.substring(logoName.lastIndexOf('.') + 1);
//		BufferedImage logoImg = null;
//		/**
//		 * 先判断是否存在该文件
//		 */
//		if (isExit) {
//			/**
//			 * 存在
//			 */
//			try {
//				logoImg = getBufferedImage(logoFilePath);
//			} catch (IOException e) {
//				logger.error(e.getMessage());
//			}
//		} else {
//			try {
//				logoImg = getBufferedImage(getInputStreamByGet(logoImgPath));
//			} catch (IOException e) {
//				logger.error(e.getMessage());
//			}
//			if (null != logoImg) {
//				logoImg = zoomInImage(logoImg, 100, 100);
//			}
//		}
//		if (!isExit) {
//			/**
//			 * 不存在 需要保存到本地
//			 */
//			saveImage(logoImg, RELATIVE_PARENT_PATH + "logo/", logoName, format);
//		}
//		logger.info("缩放logo图片并绘制消耗: {} 毫秒", System.currentTimeMillis() - logoImgTime);
//		return logoImg;
//	}
//
//	private static String getUrlFileName(String urlPath) {
//		return urlPath.substring(urlPath.lastIndexOf('/'));
//	}
//
//	private static Boolean isExistFile(String fileName, String relativePath) {
//		Boolean isExist = false;
//		if (null != fileName) {
//			File file = new File(relativePath + '/' + fileName);
//			isExist = file.exists();
//		}
//		return isExist;
//	}
//
//	private static BufferedImage getBufferedImage(String fileUrl) throws IOException {
//		File f = new File(fileUrl);
//		return ImageIO.read(f);
//	}
//
//	private static BufferedImage getBufferedImage(InputStream is) throws IOException {
//		return ImageIO.read(is);
//	}
//
//	private static InputStream getInputStreamByGet(String url) {
//		InputStream inputStream = null;
//		try {
//			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//			connection.setReadTimeout(5000);
//			connection.setConnectTimeout(5000);
//			connection.setRequestMethod("GET");
//			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//				inputStream = connection.getInputStream();
//				return inputStream;
//			}
//		} catch (IOException e) {
//			logger.error("获取网络图片出现异常，图片路径为：{}", url);
//			logger.error(e.getMessage());
//		}
//		return inputStream;
//	}
//
//	public static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
//		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
//
//		Graphics g = newImage.getGraphics();
//		g.drawImage(originalImage, 0, 0, width, height, null);
//		g.dispose();
//		return newImage;
//	}
//
//	private static boolean saveImage(BufferedImage savedImg, String saveDir, String fileName, String format) {
//		boolean flag = false;
//		/**
//		 * 先检查保存的图片格式是否正确
//		 */
//		String[] legalFormats = { "jpg", "JPG", "png", "PNG", "bmp", "BMP" };
//		int i = 0;
//		for (i = 0; i < legalFormats.length; i++) {
//			if (format.equals(legalFormats[i])) {
//				break;
//			}
//		}
//		/**
//		 * 图片格式不支持
//		 */
//		if (i == legalFormats.length) {
//			logger.info("不是保存所支持的图片格式!");
//			return false;
//		}
//
//		/**
//		 * 再检查文件后缀和保存的格式是否一致
//		 */
//		String postfix = fileName.substring(fileName.lastIndexOf('.') + 1);
//		if (!postfix.equalsIgnoreCase(format)) {
//			logger.info("待保存文件后缀和保存的格式不一致!");
//			return false;
//		}
//
//		String fileUrl = saveDir + fileName;
//		File file = new File(fileUrl);
//		try {
//			flag = ImageIO.write(savedImg, format, file);
//		} catch (IOException e) {
//			logger.error(e.getMessage());
//		}
//		return flag;
//	}
//
//	/**
//	 * 添加图片以及文字
//	 *
//	 * @param bim
//	 * @param logoPic
//	 * @param logoConfig
//	 * @param logoTxt
//	 * @return
//	 */
//	public BufferedImage addLogoQRCode(BufferedImage bim, BufferedImage logoPic, LogoConfig logoConfig,
//			String logoTxt) {
//		try {
//			/**
//			 * 读取二维码图片，并构建绘图对象
//			 */
//			BufferedImage image = bim;
//			Graphics2D g = image.createGraphics();
//
//			/**
//			 * 读取Logo图片
//			 */
//			BufferedImage logo = logoPic;
//			/**
//			 * 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
//			 */
//			int widthLogo = logo.getWidth(null) > image.getWidth() * 3 / 10 ? (image.getWidth() * 3 / 10)
//					: logo.getWidth(null),
//					heightLogo = logo.getHeight(null) > image.getHeight() * 3 / 10 ? (image.getHeight() * 3 / 10)
//							: logo.getWidth(null);
//
//			/**
//			 * logo放在中心
//			 */
//			int x = (image.getWidth() - widthLogo) / 2;
//			int y = (image.getHeight() - heightLogo) / 2;
//			/**
//			 * logo放在右下角 int x = (image.getWidth() - widthLogo); int y = (image.getHeight()
//			 * - heightLogo);
//			 */
//
//			// 开始绘制图片
//			g.drawImage(logo, x, y, widthLogo, heightLogo, null);
////            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
////            g.setStroke(new BasicStroke(logoConfig.getBorder()));
////            g.setColor(logoConfig.getBorderColor());
////            g.drawRect(x, y, widthLogo, heightLogo);
//			g.dispose();
//
//			// 把商品名称添加上去，商品名称不要太长哦，这里最多支持两行。太长就会自动截取啦
//			if (logoTxt != null && !logoTxt.equals("")) {
//				// 新的图片，把带logo的二维码下面加上文字
//				BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
//				Graphics2D outg = outImage.createGraphics();
//				// 画二维码到新的面板
//				outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
//				// 画文字到新的面板
//				outg.setColor(Color.BLACK);
//				outg.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号
//				int strWidth = outg.getFontMetrics().stringWidth(logoTxt);
//				if (strWidth > 399) {
////                  //长度过长就截取前面部分
////                  outg.drawString(productName, 0, image.getHeight() + (outImage.getHeight() - image.getHeight())/2 + 5 ); //画文字
//					// 长度过长就换行
//					String productName1 = logoTxt.substring(0, logoTxt.length() / 2);
//					String productName2 = logoTxt.substring(logoTxt.length() / 2, logoTxt.length());
//					int strWidth1 = outg.getFontMetrics().stringWidth(productName1);
//					int strWidth2 = outg.getFontMetrics().stringWidth(productName2);
//					outg.drawString(productName1, 200 - strWidth1 / 2,
//							image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 12);
//					BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_4BYTE_ABGR);
//					Graphics2D outg2 = outImage2.createGraphics();
//					outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
//					outg2.setColor(Color.BLACK);
//					outg2.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号
//					outg2.drawString(productName2, 200 - strWidth2 / 2,
//							outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
//					outg2.dispose();
//					outImage2.flush();
//					outImage = outImage2;
//				} else {
//					outg.drawString(logoTxt, 200 - strWidth / 2,
//							image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 12); // 画文字
//				}
//				outg.dispose();
//				outImage.flush();
//				image = outImage;
//			}
//			logo.flush();
//			image.flush();
//			return image;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 构建初始化二维码
//	 *
//	 * @param bm
//	 * @return
//	 */
//	public BufferedImage fileToBufferedImage(BitMatrix bm) {
//		BufferedImage image = null;
//		try {
//			int w = bm.getWidth(), h = bm.getHeight();
//			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//
//			for (int x = 0; x < w; x++) {
//				for (int y = 0; y < h; y++) {
//					image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return image;
//	}
//
//	/**
//	 * 生成二维码bufferedImage图片
//	 *
//	 * @param content       编码内容
//	 * @param barcodeFormat 编码类型
//	 * @param width         图片宽度
//	 * @param height        图片高度
//	 * @param hints         设置参数
//	 * @return
//	 */
//	public BufferedImage getQRCodeBufferedImage(String content, BarcodeFormat barcodeFormat, int width, int height,
//			Map<EncodeHintType, ?> hints) {
//		MultiFormatWriter multiFormatWriter = null;
//		BitMatrix bm = null;
//		BufferedImage image = null;
//		try {
//			multiFormatWriter = new MultiFormatWriter();
//			// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
//			bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);
//			int w = bm.getWidth();
//			int h = bm.getHeight();
//			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//
//			// 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
//			for (int x = 0; x < w; x++) {
//				for (int y = 0; y < h; y++) {
//					image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
//				}
//			}
//		} catch (WriterException e) {
//			e.printStackTrace();
//		}
//		return image;
//	}
//
//	/**
//	 * 设置二维码的格式参数
//	 *
//	 * @return
//	 */
//	public Map<EncodeHintType, Object> getDecodeHintType() {
//		// 用于设置QR二维码参数
//		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
//		// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
//		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//		// 设置编码方式
//		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//		hints.put(EncodeHintType.MARGIN, 0);
//		hints.put(EncodeHintType.MAX_SIZE, 350);
//		hints.put(EncodeHintType.MIN_SIZE, 100);
//
//		return hints;
//	}
//}
//
//class LogoConfig {
//	// logo默认边框颜色
//	public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
//	// logo默认边框宽度
//	public static final int DEFAULT_BORDER = 2;
//	// logo大小默认为照片的1/5
//	public static final int DEFAULT_LOGOPART = 5;
//
//	private final int border = DEFAULT_BORDER;
//	private final Color borderColor;
//	private final int logoPart;
//
//	/**
//	 * Creates a default config with on color {@link #} and off color
//	 * {@link #}, generating normal black-on-white barcodes.
//	 */
//	public LogoConfig() {
//		this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
//	}
//
//	public LogoConfig(Color borderColor, int logoPart) {
//		this.borderColor = borderColor;
//		this.logoPart = logoPart;
//	}
//
//	public Color getBorderColor() {
//		return borderColor;
//	}
//
//	public int getBorder() {
//		return border;
//	}
//
//	public int getLogoPart() {
//		return logoPart;
//	}
//}
