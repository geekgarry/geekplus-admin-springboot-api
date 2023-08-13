package com.geekplus.common.util;

//import com.alibaba.fastjson.JSON;
//import com.qiniu.common.QiniuException;
//import com.qiniu.common.Zone;
//import com.qiniu.http.Response;
//import com.qiniu.storage.Configuration;
//import com.qiniu.storage.UploadManager;
//import com.qiniu.storage.model.DefaultPutRet;
//import com.qiniu.util.Auth;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.io.InputStream;
//
///**
// *
// * @ClassName QiniuUtil
// * @Description 七牛云 对象存储 存储小文件 图片
// * @Author Zheng
// * @Date 2018年5月23日 上午10:24:01
// */
//@Component
//public class QiniuUtil {
//
//	private static final Logger logger = LoggerFactory.getLogger(QiniuUtil.class);
//
//    //@Value("${qiniu.accessKey}")
//    private String accessKey = "_pjgCzAxoM8FL0z0U6t_mX3RjVLaY_o0CwJ224Uj";
//    //@Value("${qiniu.secretKey}")
//    private String secretKey = "BFVlzXwRe47ajKlZ9Oa2U68jZ4dW6DVaw2ZHPtPx";
//    //@Value("${qiniu.bucket}")
//    private String bucket = "goldretail";
//    //@Value("${qiniu.path}")
//    private String path = "http://cdn.juheyinhang.com";
//    /**
//     * 将图片上传到七牛云
//     * @param file
//     * @param key 保存在空间中的名字，如果为空会使用文件的hash值为文件名
//     * @return
//     */
//    public  String uploadImg(InputStream file, String key) {
//    	 /**
//    	  * 华东    Zone.zone0()
//          * 华北    Zone.zone1()
//          * 华南    Zone.zone2()
//          * 北美    Zone.zoneNa0()
//    	  */
//        Configuration cfg = new Configuration(Zone.zone0());
//        UploadManager uploadManager = new UploadManager(cfg);
//        /**
//         * 默认不指定key的情况下，以文件内容的hash值作为文件名
//         */
//        try {
//            Auth auth = Auth.create(accessKey, secretKey);
//            String upToken = auth.uploadToken(bucket);
//            try {
//                Response response = uploadManager.put(file, key, upToken, null, null);
//                //解析上传成功的结果
//                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
//                String returnPath = path+'/'+putRet.key;
//                logger.info("保存地址={}",returnPath);
//                return returnPath;
//            } catch (QiniuException ex) {
//                Response r = ex.response;
//                logger.error(r.toString());
//                try {
//                    logger.error(r.bodyString());
//                } catch (QiniuException ex2) {
//                    //ignore
//                }
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return "";
//    }
//}
