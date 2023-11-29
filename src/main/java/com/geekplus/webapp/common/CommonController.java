package com.geekplus.webapp.common;

import cn.hutool.core.codec.Base64;
import com.geekplus.common.annotation.Log;
import com.geekplus.common.config.WebAppConfig;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.util.DateUtils;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.common.util.file.FileUploadUtils;
import com.geekplus.common.util.file.FileUtils;
import com.geekplus.common.util.uuid.IdUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用请求处理
 *
 * @author
 */
@RestController
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private WebAppConfig serverConfig;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = WebAppConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFileCategory(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public Result uploadFile(@RequestPart("file") MultipartFile file, @RequestParam(name = "pathName", required = false) String pathName) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = WebAppConfig.getUploadPath();
            if(pathName==null||"".equals(pathName)){
                if(FileUtils.isImageFile(file)){
                    filePath=WebAppConfig.getProfile() + File.separator+"article";
                }else if(FileUtils.isVideoFile(file)){
                    filePath=WebAppConfig.getProfile() + File.separator+"video";
                }else if(FileUtils.isAudioFile(file)){
                    filePath=WebAppConfig.getProfile() + File.separator+"music";
                }else {
                    filePath=WebAppConfig.getProfile() + File.separator+"document";
                }
                //filePath = WebAppConfig.getProfile() + File.separator + pathName;
            }else{
                filePath = WebAppConfig.getProfile() + File.separator + pathName;
            }
            // 上传并返回新文件名称
            //String fileName = FileUploadUtils.upload(filePath, file);
            Map fileMap = FileUploadUtils.upload2(filePath, file);
            //String url = serverConfig.getUrl() + fileName;
            Result ajax = Result.success();
            ajax.put("fileName", fileMap.get("fileName"));
            ajax.put("originalName",file.getOriginalFilename());
            ajax.put("url", fileMap.get("fileUrl"));
            return ajax;
        }
        catch (Exception e)
        {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 上传文件请求适用于文章等特殊场景
     */
    @PostMapping("/common/uploadFile")
    public Result uploadFileForArticle(@RequestPart("file") MultipartFile file, @RequestParam(name = "fileTitle", required = false) String fileTitle) throws Exception
    {
//        if(!checkFormats(file.getOriginalFilename())){
//            return Result.error("上传图片格式不是png,jpg或jpeg！");
//        }
        try
        {
            // 上传文件路径,加上以日期为路径的一个目录
            //String filePath = WebAppConfig.getUploadPath();
            String realFilePath;
            if(FileUtils.isImageFile(file)){
                realFilePath= File.separator+"article"+File.separator+ DateUtils.datePath();
            }else if(FileUtils.isVideoFile(file)){
                realFilePath=File.separator+"video"+File.separator+DateUtils.datePath();
            }else if(FileUtils.isAudioFile(file)){
                realFilePath=File.separator+"music"+File.separator+DateUtils.datePath();
            }else {
                realFilePath=File.separator+"document"+File.separator+DateUtils.datePath();
            }

            String uploadDir= WebAppConfig.getProfile()+realFilePath;
            // 上传并获取文件名称
            String fileName = "";
            String originalName=file.getOriginalFilename();
            String extension = FileUploadUtils.getExtension(file);
            //String uuidFileName = UUID.randomUUID().toString() + ".png";
            //目标文件
            //File dest = new File(uploadDir + "head_img" ,uuidFileName);
            //保存文件
            //file.transferTo(dest);
            fileName = IdUtils.fastUUID() + "." + extension;

            // 上传并返回新文件名称
            //String fileName = FileUploadUtils.upload(filePath, file);
            //File desc = new File(uploadDir + File.separator + fileName);
            File desc =FileUtils.getExistFileCategory(uploadDir + File.separator + fileName);
            file.transferTo(desc);
            //String pathFileName = getPathFileName(baseDir, fileName);
            String resultFileName= Constant.RESOURCE_PREFIX+realFilePath+File.separator+fileName;
            //String url = serverConfig.getUrl() + resultFileName;
            //log.info("用户请求URL信息："+serverConfig.getUrl());
            Result ajax = Result.success();
            ajax.put("fileName", fileName);
            //ajax.put("imgTitle",title);
            ajax.put("originalFileName", originalName);
            ajax.put("url", resultFileName);
            return ajax;
        }
        catch (Exception e)
        {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = WebAppConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constant.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 删除单个文件
     */
    @Log(title = "删除文件夹里的文件", businessType = BusinessType.DELETE)
    @GetMapping("/common/deleteFile")
    public Result deleteFile(String filePath)
    {
        String profile= Constant.RESOURCE_PREFIX;//profile
        String allFilePath=WebAppConfig.getProfile()+filePath.replace(profile,"");
        int flag=FileUtils.deleteFileCategory(allFilePath);
        if(flag>0){
            return Result.success("删除文件成功！");
        }else{
            return Result.success("删除文件失败！");
        }
    }

    /**
     * 批量删除文件List
     */
    @Log(title = "删除文件夹里的文件List", businessType = BusinessType.DELETE)
    @PostMapping("/common/deleteFileList")
    public Result deleteFileList(@RequestBody List<Map> filePaths)
    {
        int length=filePaths.size();
        for (int i = 0; i < filePaths.size(); i++) {
            String filePath=filePaths.get(i).get("filePath").toString();
            String profile= Constant.RESOURCE_PREFIX;//profile
            String allFilePath=WebAppConfig.getProfile()+filePath.replace(profile,"");
            int ds=FileUtils.deleteFileCategory(allFilePath);
            length-=ds;
        }
        if(length==0){
            return Result.success("删除文件成功！");
        }else{
            return Result.success("删除文件失败！");
        }
    }

    /**
     * 查询文件里的所有图片，删除某个图片文件
     */
    @Log(title = "删除文件夹里的图片文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/common/batchDeleteFile")
    public Result batchDeleteFile(String[] filePaths)
    {
        for(String filePath:filePaths) {
            String profile = Constant.RESOURCE_PREFIX;//profile
            String allFilePath = WebAppConfig.getProfile() + filePath.replace(profile, "");
            FileUtils.deleteFileCategory(allFilePath);
//            if (flag == true) {
//                return Result.success("删除文件成功！");
//            } else {
//                return Result.success("删除文件失败！");
//            }
        }
        return Result.success("删除文件成功！");
    }

    /**
      * @Author geekplus
      * @Description //文字生成二维码图片
      * @Param [qrCodeText]
      * @Throws
      * @Return {@link java.lang.String}
      */
    @GetMapping("/common/getQRCode")
    public Result getQRCodeImg(@RequestParam String qrCodeText){
        String base64 = "";
        // 需要生成的二维码的文字、地址
        String QrCodeStr = qrCodeText;
        // 创建二维码
        try {
            Map<EncodeHintType, String> character = new HashMap<>();
            // 设置字符集
            character.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // 设置二维码的四个参数
            // 需要生成的字符串，类型设置为二维码，二维码宽度，二维码高度，字符串字符集
            BitMatrix bitMatrix = new MultiFormatWriter().encode(QrCodeStr,
                    BarcodeFormat.QR_CODE, Constant.QRCODE_SIZE, Constant.QRCODE_SIZE, character);
            // 二维码像素，也就是上面设置的 500
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            // 创建二维码对象
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // 按照上面定义好的二维码颜色编码生成二维码
                    image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            // 1、第一种方式// 生成的二维码图片对象转 base64
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // 设置图片的格式
            ImageIO.write(image, "png", stream);
            // 生成的二维码base64
            base64 = Base64.encode(stream.toByteArray());
            } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success("生成二维码成功",base64);
    }

}
