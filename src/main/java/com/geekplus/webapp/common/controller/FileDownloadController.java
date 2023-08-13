package com.geekplus.webapp.common.controller;

import com.geekplus.common.domain.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @program: spring-code-generate
 * @description: 文件下载
 * @author: GarryChan
 * @create: 2020-12-14 09:59
 **/
@RestController
@RequestMapping("/file")
public class FileDownloadController {
    @GetMapping("/download")
    public Result fileDownload(@RequestParam("fileName") String fileName, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
        response.setContentType("text/html;charset=utf-8");
        // request通过getParameter得到filename这个参数的值
        System.out.println(fileName);
        // response设置Content-Disposition
        //设置content-disposition响应头控制浏览器以下载的形式打开文件，中文文件名要使用URLEncoder.encode方法进行编码，否则会出现文件名乱码
        response.setHeader("content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
        //response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        String path = null;
        // jpg和mp3都存在于web应用目录下，mp4则存在于F盘的movie文件夹下
        // 如果是jpg或者mp3则通过它们的相对路径得到绝对路径
        if(fileName.endsWith("jpg")){
            path= request.getServletContext().getRealPath("/images/"+fileName);
        }else if (fileName.endsWith("mp3")){
            path= request.getServletContext().getRealPath("/musics/"+fileName);
        }else if(fileName.endsWith("mp4")){
            path = "c/"+fileName;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte buff[] = new byte[2048];
            int len = 0;
            OutputStream os = response.getOutputStream();
            while((len = fis.read(buff))>0){
                os.write(buff, 0, len);
            }
            os.close();
            fis.close();
            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("下载失败！");
        }
    }
}
