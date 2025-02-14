package com.geekplus.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName: FileUtils
 * @Description:
 * @author: Garry
 * @date: 2018年8月30日 下午4:24:30
 */
public class FileUtils {

	/**
     * 获取网络图片流
     *
     * @param url
     * @return
     */
    public static InputStream getInputStreamByGet(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            System.out.println("获取网络图片出现异常，图片路径为：" + url);
            e.printStackTrace();
        }
        return null;
    }
}
