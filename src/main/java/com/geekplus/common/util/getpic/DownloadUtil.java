package com.geekplus.common.util.getpic;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadUtil {

// 下载指定路径图片
public static boolean downloadImg(String netImg,String filePath){
	//第一步，创建网络连接
	try {
		URL url = new URL(netImg);
	//第二步，打开连接
		URLConnection urlconnection = url.openConnection();
		//第三步，获取流对象
		InputStream inputStream = urlconnection.getInputStream();
		//第四步，创建本地文件路径
		File file = new File(filePath);
		//第五步，下载
		OutputStream outputStream = new FileOutputStream(file);
		int temp = 0;
		while((temp = inputStream.read())!=-1){
			outputStream.write(temp);
		}
		//第六步，关闭IO
		outputStream.close();
		inputStream.close();
		return true;
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
}
// 获取网页源代码
public static String htmlSource(String link,String encoding){
	StringBuilder stringBuilder = new StringBuilder();
	//1，创建连接
	try {
		URL url=new URL(link);
		//2，打开连接
		URLConnection urlConnection =url.openConnection();
		//3，打开流
		InputStream inputStream = urlConnection.getInputStream();
		//4,利用转换流将字节流转换为字符流，并存入缓冲区
		InputStreamReader isr = new InputStreamReader(inputStream,encoding);
		BufferedReader bufferedReader = new BufferedReader(isr);
		String line = null;
		while((line = bufferedReader.readLine())!=null){
			stringBuilder.append(line+"\r\n");
		}
		//5,关闭
		bufferedReader.close();
		inputStream.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return stringBuilder.toString();
}
}
