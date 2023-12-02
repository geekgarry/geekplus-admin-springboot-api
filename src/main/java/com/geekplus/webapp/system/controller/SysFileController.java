package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.config.WebAppConfig;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.util.DateUtils;
import com.geekplus.common.util.file.FileUploadUtils;
import com.geekplus.common.util.file.FileUtils;
import com.geekplus.common.util.uuid.IdUtils;
import com.geekplus.framework.config.ServerConfig;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 11/29/23 04:08
 * description: 做什么的？
 */
@RestController
@RequestMapping("/sysFile")
public class SysFileController {

    @Resource
    private ServerConfig serverConfig;

    /**
     * 查询文件里的所有文件，批量删除某个文件
     */
    @Log(title = "删除文件夹里的文件", businessType = BusinessType.DELETE)
    @PostMapping("/deleteSelectedFiles")
    public Result deleteSelectedFile(@RequestBody List<Map> filePaths)
    {
        int length=filePaths.size();
        for (int i = 0; i < filePaths.size(); i++) {
            String filePath=filePaths.get(i).get("filePath").toString();
            String profile= Constant.RESOURCE_PREFIX;//profile
            String allFilePath=WebAppConfig.getProfile()+filePath.replace(profile,"");
            if(filePath.contains(WebAppConfig.getProfile())) {
                allFilePath=filePath;
            }
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
     * 查询文件目录里的所有文件，递归删除文件
     */
    @Log(title = "删除文件夹里的文件", businessType = BusinessType.DELETE)
    @GetMapping("/deleteFileByRecursion")
    public Result deleteFileByRecursion(String filePath)
    {
        String profile= Constant.RESOURCE_PREFIX;//profile
        String allFilePath=WebAppConfig.getProfile()+filePath.replace(profile,"");
        if(filePath.contains(WebAppConfig.getProfile())) {
            allFilePath=filePath;
        }
        int count=FileUtils.deleteFileByRecursion(allFilePath);
        if(count>0){
            return Result.success("删除文件成功！");
        }else{
            return Result.success("删除文件失败！");
        }
    }

    /**
     * 查询文件里的所有文件，删除某个文件
     */
    @Log(title = "删除文件夹里的文件", businessType = BusinessType.DELETE)
    @GetMapping("/deleteFile")
    public Result deleteFileAnCategory(String filePath)
    {
        String profile= Constant.RESOURCE_PREFIX;//profile
        String allFilePath=WebAppConfig.getProfile()+filePath.replace(profile,"");
        if(filePath.contains(WebAppConfig.getProfile())) {
            allFilePath=filePath;
        }
        int flag=FileUtils.deleteFileCategory(allFilePath);
        if(flag>0){
            return Result.success("删除文件成功！");
        }else{
            return Result.success("删除文件失败！");
        }
    }

    /**
     * @Author geekplus
     * @Description //读取文件夹下的所有文件和文件夹
     * @Param
     * @Throws
     * @Return {@link }
     */
    @GetMapping("/readCurrentFileList")
    public Result readCurrentFileList(String folder) throws IOException {
        String readFolder=WebAppConfig.getProfile()+folder;
        if(folder==null||"".equals(folder)) {
            readFolder=WebAppConfig.getProfile();
        }if(folder=="/"||"/".equals(folder)) {
            readFolder=WebAppConfig.getProfile();
        }else if(folder.contains(WebAppConfig.getProfile())) {
            readFolder=folder;
        }
        //List<Map> mapList=FileUtils.readFileList(WebAppConfig.getProfile()+File.separator +folder);
        File file = new File(readFolder);
        List<Map<String,Object>> mapList=FileUtils.getAllFileDirectoryInfo(file);
        return Result.success(mapList);
    }

    /**
     * 查询文件里的所有图片，读取某个文件夹下的所有文件
     */
    @GetMapping("/getImageList")
    public Result listFileImage(String fileFolder)
    {
//        try{
//            List<String> listImage= FileUtils.readFileImage(WebAppConfig.getProfile(), File.separator + fileFolder);
//            return Result.success(listImage);
//        }catch(IOException e){
//            return Result.success(e.getMessage());
//        }
        String readFolder=WebAppConfig.getProfile()+fileFolder;
        if(fileFolder==null||"".equals(fileFolder)) {
            readFolder=WebAppConfig.getProfile();
        }if(fileFolder=="/"||"/".equals(fileFolder)) {
            readFolder=WebAppConfig.getProfile();
        }else if(fileFolder.contains(WebAppConfig.getProfile())) {
            readFolder=fileFolder;
        }
        File file=new File(readFolder);
        List<String> list= new ArrayList<>();
        FileUtils.getDirectoryAllFile(file,list);
        return Result.success(list);
    }
}
