package com.geekplus.common.util.location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.geekplus.common.constant.Constant;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @projectname BeautyClothes
 * @author GEEKCJJ
 * @date 2019年8月13日 上午11:54:55
 * @description:
 *
 */
//java版计算signature签名
public class BaiduMapLocationUtils {
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		BaiduMapLocationUtils baidumaplocation = new BaiduMapLocationUtils();

		// 计算sn跟参数对出现顺序有关，get请求请使用LinkedHashMap保存<key,value>，该方法根据key的插入顺序排序；post请使用TreeMap保存<key,value>，该方法会自动将key按照字母a-z顺序排序。所以get请求可自定义参数顺序（sn参数必须在最后）发送请求，但是post请求必须按照字母a-z顺序填充body（sn参数必须在最后）。以get请求为例：http://api.map.baidu.com/geocoder/v2/?address=百度大厦&output=json&ak=yourak，paramsMap中先放入address，再放output，然后放ak，放入顺序必须跟get请求中对应参数的出现顺序保持一致。

		Map<String, String> paramsMap = new LinkedHashMap<String, String>();
		paramsMap.put("address", "江苏省南京市清江苏宁广场");
		paramsMap.put("output", "json");
		paramsMap.put("ak", "8AxZDuCHACGEIzHKhZYuVqGTs5t7QMNL");

		// 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
		String paramsStr = baidumaplocation.toQueryString(paramsMap);

		// 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
		String wholeStr = new String("/geocoder/v2/?" + paramsStr + "yoursk");

		// 对上面wholeStr再作utf8编码
		String tempStr = URLEncoder.encode(wholeStr, "UTF-8");

		// 调用下面的MD5方法得到最后的sn签名7de5a22212ffaa9e326444c75a58f9a0
		System.out.println(baidumaplocation.MD5(tempStr));
		String ll = sendGetLatLng("江苏省南京市清江苏宁广场");
		System.out.println(ll);
		int index = ll.indexOf("lng");
		int lastindex = ll.lastIndexOf("precise");
		String jsonstr = ll.substring(index - 2, lastindex - 2);
		// List<Map<String,Object>> listll=jsonToListMap(jsonstr);
		Gson gson = new Gson();
		Map<?, ?> map = gson.fromJson(jsonstr, Map.class);
		System.out.println(map.get("lng"));
		System.out.println(map.get("lat"));
		System.out.println(sendGetAdd("32.05063510034472,118.74739682750372"));

	}

	/**
	 * 转换为Map类型
	 *
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> jsonToListMap(String jsonString) {
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
		List<Map<String, Object>> retMap = gson.fromJson(jsonString, new TypeToken<List<Map<String, Object>>>() {
		}.getType());
		return retMap;
	}

	// 对Map内所有value作utf8编码，拼接返回结果
	public String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
		StringBuffer queryString = new StringBuffer();
		for (Entry<?, ?> pair : data.entrySet()) {
			queryString.append(pair.getKey() + "=");
			queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
		}
		if (queryString.length() > 0) {
			queryString.deleteCharAt(queryString.length() - 1);
		}
		return queryString.toString();
	}

	// 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
	public String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}

	/**
	 * 发送get请求获取经纬度
	 *
	 * @param address
	 * @return
	 * @throws IOException
	 */
	public static String sendGetLatLng(String address) throws IOException {
		URL url = new URL(Constant.Baidu_Map_Api + "&address=" + address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置连接方式
		conn.setRequestMethod("GET");
		// 设置主机连接时间超时时间3000毫秒
		conn.setConnectTimeout(3000);
		// 设置读取远程返回数据的时间3000毫秒
		conn.setReadTimeout(3000);
		// 发送请求
		conn.connect();
		// 获取输入流
		InputStream is = conn.getInputStream();
		// 封装输入流
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		// 接收读取数据
		StringBuffer sb = new StringBuffer();

		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\r\n");
		}
		if (null != br) {
			br.close();
		}
		if (null != is) {
			is.close();
		}
		// 关闭连接
		conn.disconnect();
		return sb.toString();
	}

	/**
	 * 发送get请求有经纬度获取地址
	 *
	 * @param latlng
	 * @return
	 * @throws IOException
	 */
	public static String sendGetAdd(String latlng) throws IOException {
		URL url = new URL(Constant.Baidu_map_Api_Geocode + "&location=" + latlng);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置连接方式
		conn.setRequestMethod("GET");
		// 设置主机连接时间超时时间3000毫秒
		conn.setConnectTimeout(3000);
		// 设置读取远程返回数据的时间3000毫秒
		conn.setReadTimeout(3000);
		// 发送请求
		conn.connect();
		// 获取输入流
		InputStream is = conn.getInputStream();
		// 封装输入流
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		// 接收读取数据
		StringBuffer sb = new StringBuffer();

		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\r\n");
		}
		if (null != br) {
			br.close();
		}
		if (null != is) {
			is.close();
		}
		// 关闭连接
		conn.disconnect();
		return sb.toString();
	}

	/**
	 * Http post请求
	 *
	 * @throws IOException
	 */
	public String doPost(String httpurl, Map<Object, Object> param) throws IOException {
		String jsonparam = "";// JSON.toString(param);

		URL url = new URL(httpurl);
		// 获取httpurlConnection连接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置读取超时
		conn.setConnectTimeout(3000);
		// 设置读取超时
		conn.setReadTimeout(3000);

		// 传送数据
		conn.setDoOutput(true);
		// 读取数据
		conn.setDoInput(true);
		// 设置请求方式
		conn.setRequestMethod("POST");
		// 设置传入参数格式
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		// 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
		conn.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");

		// 获取输出流
		OutputStream os = conn.getOutputStream();
		// 输出数据
		os.write(jsonparam.getBytes());
		// 获取输入流
		InputStream is = conn.getInputStream();
		// 封装输入流
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

		StringBuffer sb = new StringBuffer();
		String line = null;

		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\r\n");
		}
		if (null != br) {
			br.close();
		}
		if (null != is) {
			is.close();
		}
		if (null != os) {
			os.close();
		}
		// 关闭连接
		conn.disconnect();

		return sb.toString();
	}
}
