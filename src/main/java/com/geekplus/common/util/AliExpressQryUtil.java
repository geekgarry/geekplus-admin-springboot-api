package com.geekplus.common.util;

import com.geekplus.common.util.http.HttpRequestUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AliExpressQryUtil
 * @Description: 阿里快递查询接口工具类
 * @author: Garry
 * @date: 2018年9月27日 下午2:35:32
 */
public class AliExpressQryUtil {
    private static final String EXPRESS_HOST = "https://wuliu.market.alicloudapi.com";
    private static final String REAL_PATH = "/kdi";
    private static final String REQ_METHOD = "GET";
    private static final String APPCODE = "f7583137ad5f40768a37b8afe97b0a28";
	public static String  expressQryFuc(String number ,String type){
		String  bodyJson = "{}";
	        Map<String, String> headers = new HashMap<String, String>();
	        headers.put("Authorization", "APPCODE " + APPCODE); //格式为:Authorization:APPCODE 83359fd73fe11248385f570e3c139xxx
	        Map<String, String> querys = new HashMap<String, String>();
	        querys.put("no", number);// !!! 请求参数
	        querys.put("type", type);// !!! 请求参数
	        //JDK 1.8示例代码请在这里下载：  http://code.fegine.com/Tools.zip
	        try {
	                HttpResponse response = HttpRequestUtil.doGet(EXPRESS_HOST, REAL_PATH, REQ_METHOD, headers, querys);
	                //System.out.println(response.toString()); //输出头部
	                bodyJson = EntityUtils.toString(response.getEntity());
	         // System.out.println(bodyJson); //输出json
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return bodyJson;
	}
	public static void main(String[] args) {
		String number = "3932092817495";
		//String type = "SFEXPRESS";
		String type = "";
		expressQryFuc(number,type);
	}
}
