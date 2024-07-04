package com.geekplus.common.core;

import org.slf4j.Logger;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/4/24 12:49 AM
 * description: 做什么的？
 */
public class LoggerFactory {

    /**
     * 根据线程自动获取类名称
     */
    public static Logger getLogger() {
        StackTraceElement[] el = Thread.currentThread().getStackTrace();
        if (el != null && el[2] != null)
            return org.slf4j.LoggerFactory.getLogger(el[2].getClassName());
        return org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

    /**
     * 针对同名类LoggerFactory重写getLogger方法
     */
    public static Logger getLogger(String name) {
        return org.slf4j.LoggerFactory.getLogger(name);
    }
}
