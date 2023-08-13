package com.geekplus.common.util;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @ClassName ObjectStorageUtil
 * @Description 对象存储 腾讯云 视频存储
 * @Author Zheng
 * @Date 2018年5月21日 上午9:33:30
 */
public class ObjectStorageUtil {

	public static final Logger logger = LoggerFactory.getLogger(ObjectStorageUtil.class);

	public static final String APP_ID = "1256767207";
	public static final String SECRET_ID = "AKIDs4A5suN7CNspJ1GG8vmVG7pO1Us5LRas";
	public static final String SECRET_KEY = "mur00odmlZCdCVBzugHwaJrkVRGyVr4b";
	public static String STORAGE_REGION = "ap-guangzhou";
	public static String BUCKET_NAME = "zheng-1256767207";
	public static String PATH = "tencloud.junbiao18.com";
	public static COSCredentials cred ;
	public static ClientConfig clientConfig ;
	public static COSClient cosClient ;

	static {
		init();
	}
	public static void init(){
		// 1 初始化用户身份信息(secretId, secretKey)
		cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
		// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		clientConfig = new ClientConfig(new Region(STORAGE_REGION));
		// 3 生成cos客户端
		cosClient = new COSClient(cred, clientConfig);
		// bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
		//String bucketName = BUCKET_NAME;
	}
	/**
	 * 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
	 * 大文件上传请参照 API 文档高级 API 上传
	 * @param filePath 本地文件路径
	 * @param key 指定要上传到 COS 上的路径
	 */
	public static void simpleUploadFile(String filePath,String key){
		File localFile = new File(filePath);
		PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, localFile);
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		logger.info(putObjectResult.toString());
	}
	public static void simpleUploadFile(InputStream fileStream,String key,long contentLength){
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setHeader("Accept", "*/*");
		metadata.setHeader("Content-Length", contentLength);
		metadata.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		metadata.setHeader("Accept-Encoding", "gzip, deflate, br");
		metadata.setHeader("Content-Type", "multipart/form-data; boundary=----"+StringUtil.getRndStr(10));
		PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, fileStream,metadata);
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		logger.info(putObjectResult.toString());
	}
	/**
	 * 简单文件下载
	 * @param filePath 指定要下载到的本地路径
	 * @param key 指定要下载的文件所在的 bucket 和路径
	 */
	public static void simpleDownloadFile(String filePath,String key){
		// 指定要下载到的本地路径
		File downFile = new File(filePath);
		// 指定要下载的文件所在的 bucket 和路径
		GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME, key);
		ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
		logger.info(downObjectMeta.getContentType());
	}
	/**
	 * 删除文件
	 * @param key 路径
	 */
	public static void deleteFile(String key){
		// 指定要删除的 bucket 和路径
		cosClient.deleteObject(BUCKET_NAME, "upload/1508731497986.jpg");
	}
	/**
	 * 关闭客户端
	 */
	public static void closeClient(){
		// 关闭客户端(关闭后台线程)
		cosClient.shutdown();
	}
	public static void main(String[] args) {
		//String filePath = "src/main/webapp/upload/1508731497986.jpg";
		String key = "upload/demo.mp4";
		//simpleUploadFile("src/main/resources/demo.mp4", key);
		File file1 = new File("src/main/resources/demo.mp4");
		try {
			simpleUploadFile(new FileInputStream(file1), key, file1.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//simpleDownloadFile("src/main/resources/demo.mp4", key);
		//deleteFile(key);
		closeClient();
	}
}
