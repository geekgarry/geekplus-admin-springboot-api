/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 11/19/23 12:40
 * description: 做什么的？
 */
package com.geekplus.common.util.image;

import com.geekplus.common.config.WebAppConfig;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.util.DateUtils;
import com.geekplus.common.util.file.FileUploadUtils;
import com.geekplus.common.util.html.ArticleUtil;
import com.geekplus.common.util.uuid.IdUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Random;
import java.util.Set;

public class ThumbnailUtils {

    public static void getThumbnail(String originalImg){
        try {
            //new一个URL对象
            URL imageURL = new URL(originalImg);
//            //打开链接
//            HttpURLConnection conn = (HttpURLConnection)imageURL.openConnection();
//            //设置请求方式为"GET"
//            conn.setRequestMethod("GET");
//            //超时响应时间为5秒
//            conn.setConnectTimeout(5 * 1000);
//            //通过输入流获取图片数据
//            InputStream inStream = conn.getInputStream();

            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            //byte[] data = readInputStream(inStream);
            InputStream inputStream = imageURL.openStream();
            // 读取原始图片 new File(originalImg)
            BufferedImage originalImage = ImageIO.read(inputStream);

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            // 计算缩略图的大小
            int thumbnailWidth = (int) Math.round(width*0.23);
            int thumbnailHeight = (int) Math.round(height*0.23);

            // 创建缩略图
            BufferedImage thumbnailImage = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = thumbnailImage.createGraphics();
            graphics2D.drawImage(originalImage.getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH), 0, 0, null);
            graphics2D.dispose();
            // 将BufferedImage转换为字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnailImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // 将字节数组转换为base64字符串
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            // 保存缩略图
            //ImageIO.write(thumbnailImage, "JPEG", new File("thumbnail.jpg"));
            System.out.println("base64图片："+"data:image/png;base64,"+base64Image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //转变base64图片缩略图
    public static String getThumbnailBase64Image(String imgPath){
        String base64Str="";
        String serverImgPath;
        try {
            if(imgPath.contains("geekplus.xyz")){
                serverImgPath=imgPath.substring(imgPath.indexOf(Constant.RESOURCE_PREFIX)).replaceAll(Constant.RESOURCE_PREFIX, WebAppConfig.getProfile());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedImage originalImage = ImageIO.read(new File(serverImgPath));
                if(ImageUtil.calculateSharpness(originalImage)>=60) {
                    // 生成缩略图 inputStream
                    Thumbnails.of(new File(serverImgPath))
                            .scale(0.4d)
                            .outputQuality(0.5d)
                            //.toFile(new File("thumbnail.jpeg"));
                            .toOutputStream(baos);
                }else {
                    // 生成缩略图 inputStream
                    Thumbnails.of(new File(serverImgPath))
                            .scale(0.5d)
                            .outputQuality(0.9d)
                            //.toFile(new File("thumbnail.jpeg"));
                            .toOutputStream(baos);
                }
                //输出文件流
                byte[] imageBytes = baos.toByteArray();
                // 将字节数组转换为base64字符串
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                base64Str="data:image/png;base64,"+base64Image;
            }else{
                //new一个URL对象
                URL imageURL = new URL(imgPath);
                //得到图片的二进制数据，以二进制封装得到数据，具有通用性
                InputStream inputStream = imageURL.openStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedImage originalImage = ImageIO.read(inputStream);
                if(ImageUtil.calculateSharpness(originalImage)>=60) {
                    // 生成缩略图 new File(serverImgPath)
                    Thumbnails.of(inputStream)
                            .scale(0.4d)
                            .outputQuality(0.5d)
                            //.toFile(new File("thumbnail.jpeg"));
                            .toOutputStream(baos);
                }else {
                    // 生成缩略图 new File(serverImgPath)
                    Thumbnails.of(inputStream)
                            .scale(0.5d)
                            .outputQuality(0.9d)
                            //.toFile(new File("thumbnail.jpeg"));
                            .toOutputStream(baos);
                }
                //输出文件流
                byte[] imageBytes = baos.toByteArray();
                // 将字节数组转换为base64字符串
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                base64Str="data:image/png;base64,"+base64Image;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Str;
    }

    //服务器端存储缩略图
    public static String getThumbnailImage(String imgPath){
        String thumbnailImg="";
        String serverImgPath;
        String suffixExtension=".png";//getThumbnailSuffixExtension(imgPath);
        try {
            if(imgPath.contains("geekplus.xyz")){
                String prefixUrl=imgPath.substring(0,imgPath.indexOf(Constant.RESOURCE_PREFIX));
                //System.out.println("服务器图片地址url："+imgPath);
                serverImgPath=imgPath.substring(imgPath.indexOf(Constant.RESOURCE_PREFIX)).replaceAll(Constant.RESOURCE_PREFIX, WebAppConfig.getProfile());
                BufferedImage originalImage = ImageIO.read(new File(serverImgPath));
                String thumbnailPath=File.separator+"thumbnail"+File.separator + DateUtils.dateTime() + File.separator + IdUtils.fastUUID() + suffixExtension;
                //String serverThumbnailPath=WebAppConfig.getProfile()+thumbnailPath;
                File serverDesc = FileUploadUtils.getAbsoluteFile(WebAppConfig.getProfile(), thumbnailPath);
                if(ImageUtil.calculateSharpness(originalImage)>=60) {
                    // 生成缩略图 inputStream
                    Thumbnails.of(new File(serverImgPath))
                            .scale(0.4d)
                            .outputQuality(0.5d)
                            .toFile(serverDesc);
                }else {
                    // 生成缩略图 inputStream
                    Thumbnails.of(new File(serverImgPath))
                            .scale(0.5d)
                            .outputQuality(0.9d)
                            .toFile(serverDesc);
                }
                thumbnailImg=prefixUrl+Constant.RESOURCE_PREFIX+thumbnailPath;
            }else{
                //System.out.println("网络图片地址url："+imgPath);
//                //new一个URL对象
//                URL imageURL = new URL(imgPath);
//                HttpURLConnection urlConnection= (HttpURLConnection) imageURL.openConnection();
//                urlConnection.setRequestProperty("User-Agent", "Mozilla/4.76");
//                urlConnection.connect();
//                //得到图片的二进制数据，以二进制封装得到数据，具有通用性
//                InputStream inputStream = urlConnection.getInputStream();//imageURL.openStream();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                BufferedImage originalImage = ImageIO.read(inputStream);
//                String thumbnailPath=File.separator+"thumbnail"+File.separator + DateUtils.dateTime() + File.separator + IdUtils.fastUUID() + suffixExtension;
//                //String serverThumbnailPath=WebAppConfig.getProfile()+thumbnailPath;
//                File serverDesc = FileUploadUtils.getAbsoluteFile(WebAppConfig.getProfile(), thumbnailPath);
//                if(ImageUtil.calculateSharpness(originalImage)>=60) {
//                    // 生成缩略图 new File(serverImgPath)
//                    Thumbnails.of(inputStream)
//                            .scale(0.4d)
//                            .outputQuality(0.5d)
//                            .toFile(serverDesc);
//                }else {
//                    // 生成缩略图 new File(serverImgPath)
//                    Thumbnails.of(inputStream)
//                            .scale(0.5d)
//                            .outputQuality(0.9d)
//                            .toFile(serverDesc);
//                }
//                thumbnailImg=Constant.RESOURCE_PREFIX+thumbnailPath;
                thumbnailImg=imgPath;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumbnailImg;
    }

    /**
      * @Author geekplus
      * @Description //获取网络URL源文件后缀名
      * @Param
      * @Throws
      * @Return {@link }
      */
    public static String getThumbnailSuffixExtension(String imgPath){
        //imgPath.replaceAll("\\","/");
        String suffixExtension="";
        try {
            URL url = new URL(imgPath);
            String fileName = url.getFile();
            //suffixExtension= Paths.get(fileName).getFileName().toString();
            //suffixExtension= StringUtils.getFilenameExtension(fileName);
            //suffixExtension= FilenameUtils.getExtension(fileName);
            suffixExtension=fileName.substring(fileName.lastIndexOf("."));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return suffixExtension;
    }

    /**
     * 要创建分辨率为原始图片 25% 的缩略图
     */
    public static void halfResolution(File source,File target) throws IOException {
        Thumbnails
                //传入源文件
                .of(source)
                //设置分辨率比例因子，不能小于0.0
                .scale(0.25d)
                //目标文件
                .toFile(target);
    }

    /**
     * 创建分辨率为640x480的略缩图，并在此基础上，保留原始图像的纵横比
     */
    public static void formatResolution(File source,File target) throws IOException {
        Thumbnails
                //传入源文件
                .of(source)
                //生成缩略图的宽高
                .size(640,480)
                //保留原始图像纵横比
                .keepAspectRatio(true)
                //目标文件
                .toFile(target);
    }

    /**
     *  保持原始分辨率，减低图片质量
     */
    public static void outputQuality(File source,File target) throws IOException {
        Thumbnails
                //传入源文件
                .of(source)
                //原始分辨率
                .scale(1.0d)
                //输出图片质量，该值介于 double 和 1.0d 和 0.0d之间，值越高质量越高
                .outputQuality(0.3d)
                //目标文件
                .toFile(target);
    }

    //把正文中的第一张图片作为封面
    public static String getThumbnailPicture(String articleContent){
        String thumbnailImage="";
        Set<String> fp = ArticleUtil.getImgStr(articleContent);
        if (!fp.isEmpty()) {
            //System.out.println(fp.iterator().next());//获取第一张
            //thumbnailImage=fp.iterator().next();
            Random random = new Random();
            int index = random.nextInt(fp.size());
            String[] fps= fp.toArray(new String[0]);
            thumbnailImage=fps[index];
        }
        return thumbnailImage;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();

    }

    public static void main(String[] args) throws IOException {
        String imgPath="https://www.geekplus.xyz/article/20230410/4ec058f6-496d-4d0f-adfb-803d44120ec5.png";
//        getThumbnail(imgPath);
        //getThumbnailImage(imgPath);
//        System.out.println(imgPath.substring(0,imgPath.indexOf(Constant.RESOURCE_PREFIX)));
//        System.out.println(imgPath.substring(imgPath.indexOf(Constant.RESOURCE_PREFIX)).replaceAll(Constant.RESOURCE_PREFIX, "home/maike"));
//        System.out.println(getThumbnailBase64Image(imgPath));
        //new一个URL对象
//        URL imageURL = new URL(imgPath);
        //byte[] data = readInputStream(inStream);
//        InputStream inputStream = imageURL.openStream();
        // 读取原始图片 new File(originalImg)
//        BufferedImage originalImage = ImageIO.read(inputStream);
//        System.out.println(ImageUtil.calculateSharpness(originalImage));
//        String fileName = imageURL.getFile();
//        System.out.println(fileName.substring(fileName.lastIndexOf(".")));
    }
}
