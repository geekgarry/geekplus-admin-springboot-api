package com.geekplus.common.util;

import com.geekplus.common.domain.FileEntity;
import com.geekplus.common.util.uuid.UUIDUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @ClassName FileUploadTool
 * @Description 文件上传工具类
 * @Author cpp
 * @Date 2018年4月2日 上午9:42:31
 */
@Component
public class FileUploadTool {

	//@Value("${tencent.path}")
	private String tenstorage = "http://tencloud.junbiao18.com";
	TransfMediaTool transfMediaTool = new TransfMediaTool();
	// 文件最大30M
	private static long upload_maxsize = 30 * 1024 * 1024;
	// 文件允许格式
	private static String[] allowFiles = { ".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".xlsx", ".gif",
			".png", ".jpg", ".jpeg", ".bmp", ".xls", ".mp4", ".flv", ".ppt", ".avi", ".mpg", ".wmv", ".3gp", ".mov",
			".asf", ".asx", ".vob", ".wmv9", ".rm", ".rmvb" };
	// 允许转码的视频格式（ffMpeg）
	//private static String[] allowFLV = { ".avi", ".mpg", ".wmv", ".3gp", ".mov", ".asf", ".asx", ".vob" };
	// 允许的视频转码格式(menCoder)
	//private static String[] allowAVI = { ".wmv9", ".rm", ".rmvb" };

	public FileEntity createFile( HttpServletRequest request) {
		 // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
                .getServletContext());
        if (multipartResolver.isMultipart(request)) {
        	 // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                return uploadFile(file);
            }
        }
		return null;
	}
    private FileEntity uploadFile(MultipartFile multipartFile){
    	 // 记录上传过程起始时的时间，用来计算上传时间
        long pre =  System.currentTimeMillis();
    	FileEntity entity = new FileEntity();
		boolean bflag = false;
		String fileName = multipartFile.getOriginalFilename();
		// 判断文件不为空
		if (multipartFile.getSize() != 0 && !multipartFile.isEmpty()) {
			bflag = true;
			// 判断文件大小
			if (multipartFile.getSize() <= upload_maxsize) {
				bflag = true;
				// 文件类型判断
				if (this.checkFileType(fileName)) {
					bflag = true;
				} else {
					bflag = false;
					System.out.println("文件类型不允许");
				}
			} else {
				bflag = false;
				System.out.println("文件大小超范围");
			}
		} else {
			bflag = false;
			System.out.println("文件为空");
		}
		if (bflag) {
			String newFileName = UUIDUtil.getFileUUID() + this.getFileExt(fileName);
			try {
				ObjectStorageUtil.simpleUploadFile(multipartFile.getInputStream(), "/upload/" + newFileName,multipartFile.getSize());
				entity.setSize(this.getSize(multipartFile.getSize()));
				// 设置视屏地址路径 host/video/2018282.mp4
				entity.setPath(tenstorage + "/upload/" + newFileName);
				entity.setTitleOrig(multipartFile.getOriginalFilename());
				entity.setTitleAlter(newFileName);
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				entity.setUploadTime(timestamp);
				return entity;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("上传 "+fileName+" 文件耗时："+(System.currentTimeMillis()-pre)+" 毫秒");
		return null;
    }
	/**
	 * 文件类型判断
	 *
	 * @param fileName
	 * @return
	 */
	private boolean checkFileType(String fileName) {
		Iterator<String> type = Arrays.asList(allowFiles).iterator();
		while (type.hasNext()) {
			String ext = type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 获取文件扩展名
	 *
	 * @return string
	 */
	private String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}


	private String getSize(long fileLength) {
		String size = "";
		DecimalFormat df = new DecimalFormat("#.00");
		if (fileLength < 1024) {
			size = df.format((double) fileLength) + "BT";
		} else if (fileLength < 1048576) {
			size = df.format((double) fileLength / 1024) + "KB";
		} else if (fileLength < 1073741824) {
			size = df.format((double) fileLength / 1048576) + "MB";
		} else {
			size = df.format((double) fileLength / 1073741824) + "GB";
		}
		return size;
	}
}
