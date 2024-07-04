package com.geekplus.common.core.scheduled;

import com.geekplus.common.config.WebAppConfig;
import com.geekplus.common.util.file.FileUtils;
import com.geekplus.webapp.function.service.impl.ChatAILogServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/25/23 01:09
 * description: 做什么的？
 */
@Component
public class TimeFileOperateTask {
    // 注入日志对象  TimeTask.class 为定时任务类
    private static final Logger logger = LoggerFactory.getLogger(TimeFileOperateTask.class);

    // 注入sysOperLogService ：实现操作的对象（接口）
    //@Autowired
    //private ChatAILogServiceImpl chatgptLogService;

    //@Scheduled(cron = "0 0 6 * * SUN") // 每周日早上6点触发一次
    @Scheduled(cron="0 0 2 4 * *") //表示：每月4号凌晨两点执行
    public void deleteFileByTimer(){
        //日志
        logger.info("---------您好，定时任务开始执行！---------"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
        //调用删除数据库数据方法
        deleteFile();
        logger.info("---------真不错，定时任务执行成功！---------"+new  SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    //删除服务器文件的方法
    private void deleteFile() {
        long currentTimeMillis =System.currentTimeMillis();//表示直接删除当前时间之前的
        //currentTimeMillis - (7 * 24 * 60 * 60 * 1000);//表示删除七天前的
        File file = new File(WebAppConfig.getProfile()+File.separator+"chatData");
        try {
            //调用FileUtils的方法 删除服务器文件
            FileUtils.deleteFilesOlderThan(file,currentTimeMillis);
        } catch (Exception e) {
            logger.error("清理数据失败，失败原因：" + e.getMessage());
        }
    }
}
