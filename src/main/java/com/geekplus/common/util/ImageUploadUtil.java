//package com.geekplus.common.util;
//
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * @ClassName ImageUploadUtil
// * @Description 图片上传工具类
// * @Author Zheng
// * @Date 2017年9月29日 下午4:16:31
// */
//public class ImageUploadUtil {
//	private static QiniuUtil qiniuUtil = new QiniuUtil();
//	// 图片类型
//	private static List<String> fileTypes = new ArrayList<String>();
//	private static long upload_maxsize = 5 * 1024 * 1024;
//	static {
//		fileTypes.add(".jpg");
//		fileTypes.add(".jpeg");
//		fileTypes.add(".bmp");
//		fileTypes.add(".gif");
//		fileTypes.add(".png");
//	}
//
//	/**
//	 * 图片上传
//	 *
//	 * @Title upload
//	 * @param request
//	 * @return
//	 * @throws IllegalStateException
//	 * @throws IOException
//	 */
//	public static String upload(HttpServletRequest request) throws IllegalStateException, IOException {
//		// 创建一个通用的多部分解析器
//		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
//				request.getSession().getServletContext());
//		// 图片名称
//		String fileName = null;
//		// 判断 request 是否有文件上传,即多部分请求
//		if (multipartResolver.isMultipart(request)) {
//			// 转换成多部分request
//			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//			// 取得request中的所有文件名
//			Iterator<String> iter = multiRequest.getFileNames();
//			while (iter.hasNext()) {
//				// 记录上传过程起始时的时间，用来计算上传时间
//				long pre = System.currentTimeMillis();
//				// 取得上传文件
//				MultipartFile file = multiRequest.getFile(iter.next());
//				// 判断大小
//				if (file.getSize() > upload_maxsize) {
//					return "error: 图片大小超出限制";
//				}
//				if (file != null) {
//					// 取得当前上传文件的文件名称
//					String myFileName = file.getOriginalFilename();
//					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//					if (myFileName.trim() != "") {
//						fileName = qiniuUtil.uploadImg(file.getInputStream(),
//								StringUtil.getRndString2() + myFileName.substring(myFileName.lastIndexOf(".")));
//					}
//				}
//				// 记录上传该文件后的时间
//				long finaltime = System.currentTimeMillis();
//				System.err.println("图片上传耗时(毫秒):" + (finaltime - pre));
//			}
//		}
//		return fileName;
//	}
//
//	/**
//	 * 多文件上传
//	 * @param request
//	 * @return
//	 * @throws IllegalStateException
//	 * @throws IOException
//	 */
//	public static List<String> upload2(HttpServletRequest request) throws IllegalStateException, IOException {
//          // 创建一个通用的多部分解析器
//		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
//				request.getSession().getServletContext());
//        // 图片名称
//		List<String> fileNames = new ArrayList<>();
//        // 判断 request 是否有文件上传,即多部分请求
//		if (multipartResolver.isMultipart(request)) {
//			// 转换成多部分request
//			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//			// 取得request中的所有文件名
//			Iterator<String> iter = multiRequest.getFileNames();
//			while (iter.hasNext()) {
//				// 记录上传过程起始时的时间，用来计算上传时间
//				long pre = System.currentTimeMillis();
//				// 取得上传文件
//				MultipartFile file = multiRequest.getFile(iter.next());
//				// 判断大小
//				if (file.getSize() > upload_maxsize) {
//					System.err.println("error: 图片大小超出限制; "+file.getSize()+"/"+upload_maxsize);
//					continue;
//				}
//				if (file != null) {
//					// 取得当前上传文件的文件名称
//					String myFileName = file.getOriginalFilename();
//					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//					if (myFileName.trim() != "") {
//						String fileName = qiniuUtil.uploadImg(file.getInputStream(),
//								StringUtil.getRndString2() + myFileName.substring(myFileName.lastIndexOf(".")));
//						fileNames.add(fileName);
//					}
//				}
//				// 记录上传该文件后的时间
//				long finaltime = System.currentTimeMillis();
//				System.err.println("图片上传耗时(毫秒):" + (finaltime - pre));
//			}
//		}
//		return fileNames;
//	}
//	/**
//	 * base64图片上传
//	 * @param base64Str
//	 * @param extraFile 图片拓展名 png
//	 * @return 上传图片 回传链接地址 失败则为 null
//	 */
//    public static String uploadBase64Img(String base64Str, String extraFile) {
//    	// 记录上传过程起始时的时间，用来计算上传时间
//		long pre = System.currentTimeMillis();
//    	String fileNames = StringUtil.getRndString2()+"."+extraFile;
//    	String localFilePath = Base64Util.getPicFormatBASE64(base64Str, fileNames);
//    	if(localFilePath == null) {
//    		return null;
//    	}
//    	try {
//    		File localFile = new File(localFilePath);
//    		if(!localFile.exists()) {
//    			return null;
//    		}
//			InputStream is = new FileInputStream(localFile);
//			fileNames = qiniuUtil.uploadImg(is,fileNames);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		}
//    	// 记录上传该文件后的时间
//		long finaltime = System.currentTimeMillis();
//    	System.err.println("图片上传耗时(毫秒):" + (finaltime - pre));
//    	return fileNames;
//    }
//	/**
//	 * ckeditor文件上传功能，回调，传回图片路径，实现预览效果。
//	 *
//	 * @Title ckeditor
//	 * @param request
//	 * @param response
//	 * @throws IOException
//	 */
//	public static String ckeditor(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String fileName = upload(request);
//		response.setContentType("text/html;charset=UTF-8");
//		return fileName;
//	}
//	/**
//	 * 多图上传
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException
//	 */
//	public static List<String> mulUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		List<String> fileNames = upload2(request);
//		response.setContentType("text/html;charset=UTF-8");
//		return fileNames;
//	}
//}
