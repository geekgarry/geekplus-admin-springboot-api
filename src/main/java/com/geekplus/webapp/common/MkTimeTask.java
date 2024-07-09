/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/25/23 01:09
 * description: 做什么的？
 */
package com.geekplus.webapp.common;

import com.geekplus.webapp.function.service.impl.ChatAILogServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MkTimeTask {
    // 注入日志对象  TimeTask.class 为定时任务类
    private static final Logger logger = LoggerFactory.getLogger(MkTimeTask.class);

    // 注入sysOperLogService ：实现操作的对象（接口）
    @Autowired
    private ChatAILogServiceImpl chatAILogService;

    //@Scheduled(cron = "0 0 6 * * SUN") // 每周日早上6点触发一次
    @Scheduled(cron="0 0 2 3 * *") //表示：每月三号凌晨两点执行
    public void clearDataByDesgin(){
        //日志
        logger.info("---------您好，定时任务开始执行！---------"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
        //调用删除数据库数据方法
        clearData();
        logger.info("---------真不错，定时任务执行成功！---------"+new  SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    //删除数据库数据方法
    private void clearData() {
        try {
            //调用service层的方法 删除数据库数据
            chatAILogService.deleteAll();
        } catch (Exception e) {
            logger.error("清理数据失败，失败原因：" + e.getMessage());
        }
    }
}
