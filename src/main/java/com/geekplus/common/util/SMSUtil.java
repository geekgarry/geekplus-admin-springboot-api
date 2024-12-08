//package com.geekplus.common.util;
//
//import org.json.JSONObject;
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @projectname AllProjectUtils
// * @author GEEKCJJ
// * @date 2019年8月2日 下午6:13:03
// * @description:
// *
// */
//public class SMSUtil {
//	private static final String addr = "http://api.sms.cn/sms/?ac=send";
//	// 接口服务注册的用户id
//	private static final String userId = "geekcjj";
//
//	/*
//	 * 如uid是：test，登录密码是：123123 pwd=md5(123123test),即
//	 * pwd=b9887c5ebb23ebb294acab183ecf0769
//	 *
//	 * 线生成地址：http://www.sms.cn/password
//	 */
//	// 接口注册的密码:经过拼接字符串和md5加密后的结果
//	private static final String pwd = "37d67e4d5e730b7adad94ba276a6c937";
//	private static final String encode = "utf8"; // 编码
//
//	public static String send(String msgContent, String mobile) throws Exception {
//		// String msgContent="{\"code\":"+code+",\"username\":"+username+"}";
//		// 组建请求
//		String straddr = addr + "&uid=" + userId + "&pwd=" + pwd + "&template=" + 100005 + "&encode=" + encode
//				+ "&mobile=" + mobile + // 手机号
//				"&content=" + URLEncoder.encode(msgContent, "UTF-8"); // 编码格式要对应上，不然就收不到短信
//
//		StringBuffer sb = new StringBuffer(straddr);// 发送请求
//		URL url = new URL(sb.toString());
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod("POST");
//		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//
//		// 返回结果
//		String inputline = in.readLine();
//		String msg = inputline.substring(9, 12);
//		System.out.println("smstest=" + msg);
//		return msg;
//	}
//
//	public static String sendverifycode(String code, String phone) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("code", code);
//		map.put("username", phone);
//		JSONObject json = new JSONObject(map);
//		try {
//			return send(json.toString(), phone);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			return e.getMessage();
//		}
//	}
//
//	public static String exportaa(String vcode, String mobile) throws IOException {
////		Random random = new Random();
////		String result="";
////		for (int i=0;i<6;i++)
////		{
////		    result+=random.nextInt(10);
////		}
////      String resulta ="\""+result+"\""; 随机验证码6位
//		// String codea ="code";
//		// String code ="\""+codea+"\"";
//		// String valuesa ="123456";//验证码数字
//		// String value ="\""+vcode+"\"";
//		String timestamp = timeStamp2Date(timeStamp(), null);
//		System.out.println(timestamp);
//		String md5 = MD5Util.MD5Encode("geekcjj123456cjj" + timeStamp(), "utf-8");
//		System.out.println(md5);
//		// 组建请求
//		String straddr = "http://116.62.155.121:8888/v2sms.aspx?action=send" +
//		// "http://cloud.yunsms.cn/v2sms.aspx" +
//				"&userid=" + 1248 + "&timestamp=" + timestamp + "&sign=" + md5 + "&mobile=" + mobile + "&content="
//				+ "【麦壳千元科技】你好，您的验证码：" + vcode + "&extno=";
//		System.out.println(straddr);
//		StringBuffer sb = new StringBuffer(straddr);
//		System.out.println("URL:" + sb);
//
//		// 发送请求
//		URL url;
//		String inputline = "";
//		try {
//			url = new URL(sb.toString());
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("GET");
//			InputStream is = url.openStream();
//			InputStreamReader isr = new InputStreamReader(is);
//			BufferedReader in = new BufferedReader(isr);
//			// 1.创建DocumentBuilderFactory对象
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			// 2.创建DocumentBuilder对象
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document d = builder.parse(is);
//			NodeList sList = d.getElementsByTagName("returnstatus");
//			// element(sList);
//			inputline = ReadXmlByDom.node(sList);
//			// 返回结果
//			// inputline = in.readLine();
//			// System.out.println( inputline+"a" );
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		// System.out.println("Response:"+inputline);
//		// String msg=inputline.substring(9, 12);
//		return inputline;
//	}
//
//	/**
//	 * 取得当前时间戳（精确到秒）
//	 *
//	 * @return
//	 */
//	public static String timeStamp() {
//		long time = System.currentTimeMillis();
//		String t = String.valueOf(time / 1000);
//		return t;
//	}
//
//	/**
//	 * 时间戳转换成日期格式字符串
//	 *
//	 * @param seconds   精确到秒的字符串
//	 * @param formatStr
//	 * @return
//	 */
//	public static String timeStamp2Date(String seconds, String format) {
//		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
//			return "";
//		}
//		if (format == null || format.isEmpty()) {
//			format = "yyyyMMddHHmmss";
//		}
//		SimpleDateFormat sdf = new SimpleDateFormat(format);
//		return sdf.format(new Date(Long.valueOf(seconds + "000")));
//	}
//
//	/**
//	 * 日期格式字符串转换成时间戳
//	 *
//	 * @param date   字符串日期
//	 * @param format 如：yyyy-MM-dd HH:mm:ss
//	 * @return
//	 */
//	public static String date2TimeStamp(String date_str, String format) {
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat(format);
//			return String.valueOf(sdf.parse(date_str).getTime() / 1000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "";
//	}
//
//	public static void main(String[] args) {
//		// 生成验证码
//		int verifycode = (int) ((Math.random() * 9 + 1) * 100000);// 随机生成的6位数（验证码）//EMailUtil.getNumberLetter();
//		// System.out.println(sendverifycode(String.valueOf(verifycode),"13082540095"));
//		try {
//			System.out.println("发送" + exportaa(String.valueOf(verifycode), "13082540095"));
//			System.out.println("时间戳" + System.currentTimeMillis());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		Map<String,Object> map=new HashMap<String, Object>();
////		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
////		map.put("code",verifycode);
////		map.put("username","陈加加");
////		list.add(map);
//		// JSONObject json = new JSONObject(map);
//		// new JSONObject().toJSONString(root)；
//		// System.out.println("list"+list.toString());
//		// System.out.println("map转json"+json.toString());
//		// System.out.println("map添加到list"+map.toString());
////		try {
////			System.out.println(send(json.toString(),"13082540095"));
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//	}
//}
