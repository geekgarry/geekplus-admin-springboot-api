package com.geekplus.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName RegexUtil
 * @Description 正则表达式工具类
 * @Author GeekPlus
 * @Date 2017年10月6日 下午8:29:34
 */
@Component
public class RegexUtil {
	// 日志
	public static final Logger logger = LoggerFactory.getLogger(RegexUtil.class);
	// 邮箱正则表达式
    public static final String IS_EMAIL_PATTERN = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    // 数字正则表达式
    public static final String IS_NUMBER_PATTERN = "[1-9]\\d*";
    // 链接地址正则表达式
    public static final String IS_URL_PATTERN = "/^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$/";
    // 手机号正则表达式 "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
    public static final String IS_PHONE_NUMBER_PATTERN = "^1(3|4|5|6|7|8|9)\\d{9}$";
    // 固话
    public static final String IS_TEL_PHONE_PATTERN = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
    // pc端
    public static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
	        +"|windows (phone|ce)|blackberry"
	        +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
	        +"|laystation portable)|nokia|fennec|htc[-_]"
	        +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    // 移动端
	public static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
	        +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	//移动设备正则匹配：手机端、平板
	public static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
	public static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);
    /**
     * 正则表达式验证
     * @param valStr 将要验证的字符
     * @param regex 正则表达式
     * @return true: 正确 满足表达式约束   false:不合法
     */
    public static boolean validate(String valStr,String regex){
    	Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(valStr);
    	return matcher.matches();
    }
    /**
	 * 用于将过滤关键词列表 转化成正则格式
	 * @param lists 字符数组列表 使用 | 分割  例如 : hello|world
	 * @return
	 */
	public static String stringListToRegexList(String lists){
		StringBuilder sb = new StringBuilder();
		sb.append(".*(");
		sb.append(lists);
		sb.append(")+.*");
		return sb.toString();
	}
    public static void main(String[] args) {
//		String input = "Here are some URLs: http://tech.geekplus.cc/api/dsnfkdgnj, https://geekplus.cc/api/dnghg, /geekplus-app/上课呢芳, and a normal word.";
//		String input2 = "https://geekplus.cc/api/profile/article/2023/12/01/7a7d087c.png";
//		System.out.println(RegexUtil.validate("15651657858",RegexUtil.IS_PHONE_NUMBER_PATTERN));
//		System.out.println(RegexUtil.getRegexStr(input, "geekplus", ""));
//		System.out.println(RegexUtil.getReplaceRegexUrl(input, "", "/geekplus-app", "/gp-api"));
//		System.out.println(RegexUtil.isHasUrl1(input, "/geekplus-app"));
		String input = "https://maike.geekplus.xyzhttps://maike.geekplus.xyzhttps://maike.geekplus.xyzhttps://maike.geekplus.xyzhttps://maike.geekplus.xyzhttps://maike.geekplus.xyz/profile/snfjksndk.img";
		String toReplace = "https://maike.geekplus.xyz";
		String replacement = "https://maike.geekplus.xyz";

		// 将toReplace中的特殊字符转义
		String regex = Pattern.quote(toReplace);

		// 替换字符串
		String result = input.replaceAll(regex, "");

		System.out.println(result); // 输出: 这是一段重复的文本这是一段重复的文本 -> 这是一段重复的文本
	}
	 /**
     * 验证设备类型
     * @param userAgent
     * @return true 移动端 false pc端
     */
    public static boolean checkDeviceType(String userAgent){
	    if(null == userAgent){
	        userAgent = "";
	    }
	    // 匹配
	    Matcher matcherPhone = phonePat.matcher(userAgent);
	    Matcher matcherTable = tablePat.matcher(userAgent);
	    if(matcherPhone.find() || matcherTable.find()){
	        return true;
	    } else {
	        return false;
	    }
	}


	/**
	  * @Author geekplus
	  * @Description //把正则匹配的部分替换为自己定义的
	  * @Param url 需要被替换的网址，myWebDomain 我的网站域名去掉顶级域名后的，proxyApi 如果有就是代理/api，replaceUrlStr 替换的字符串
	  * @Throws
	  * @Return {@link }
	  */
	public static String getReplaceRegexUrl(String urlContent, String myWebDomain, String proxyApi, String replacement){
//		if(proxyApi == null || proxyApi.isEmpty()){
//			proxyApi = "";
//		}
		String regex = "https?://[\\w\\.-]+\\.[\\w\\.-]+"+proxyApi;//匹配所有网址
		if(myWebDomain != null && !myWebDomain.isEmpty()) {
			regex = "https?://([\\w\\.-]+\\.)?"+myWebDomain+"(\\.[\\w\\.-]+)?" + proxyApi;//匹配网址固定域名xxx
			//String regex = "https?://(www\\.)?geekplus\\.[\\w\\.-]+";//匹配网址带www前缀//([a-zA-Z0-9.-]+) ([a-zA-Z0-9]+\.)
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(urlContent);

		while (matcher.find()) {
			urlContent = urlContent.replace(matcher.group(), replacement);
		}
		return urlContent;
	}

	/**
	  * @Author geekplus
	  * @Description //通用方法，得到被替换后的内容
	  * @Param
	  * @Throws
	  * @Return {@link }
	  */
	public static String getReplaceRegexContent(String replaceContent, String replaceStr, String replacement){
		String regex = replaceStr;//匹配字符
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(replaceContent);

		while (matcher.find()) {
			replaceContent = replaceContent.replace(matcher.group(), replacement);
		}
		return replaceContent;
	}

	/**
	  * @Author geekplus
	  * @Description //获取网址中匹配的部分
	  * @Param url 需要被替换的网址，myWebDomain 我的网站域名去掉顶级域名后的，proxyApi 如果有就是代理/api
	  * @Throws
	  * @Return {@link }
	  */
	public static String getRegexStr(String url, String myWebDomain, String proxyApi){
//		if(proxyApi == null || proxyApi.isEmpty()){
//			proxyApi = "";
//		}
		String resultUrl = "";
		String regex = "https?://[\\w\\.-]+\\.[\\w\\.-]+"+proxyApi;//匹配所有网址
		if(myWebDomain != null && !myWebDomain.isEmpty()) {
			regex = "https?://([\\w\\.-]+\\.)?"+myWebDomain+"(\\.[\\w\\.-]+)?" + proxyApi;//匹配网址固定域名xxx
			//String regex = "https?://(www\\.)?geekplus\\.[\\w\\.-]+";//匹配网址带www前缀//([a-zA-Z0-9.-]+) ([a-zA-Z0-9]+\.)
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);

		while (matcher.find()) {
			resultUrl = matcher.group();
		}
		return resultUrl;
	}

	/**
	  * @Author geekplus
	  * @Description //判断url中是否拥有正则匹配的部分
	  * @Param url 需要被替换的网址，myWebDomain 我的网站域名去掉顶级域名后的，proxyApi 如果有就是代理/api
	  * @Throws
	  * @Return {@link }
	  */
	public static Boolean isHasUrl(String url, String myWebDomain, String proxyApi){
//		if(proxyApi == null || proxyApi.isEmpty()){
//			proxyApi = "";
//		}
		String regex = "https?://[\\w\\.-]+\\.[\\w\\.-]+"+proxyApi; //匹配所有网址
		if(myWebDomain != null && !myWebDomain.isEmpty()) {
			regex = "https?://([\\w\\.-]+\\.)?"+myWebDomain+"(\\.[\\w\\.-]+)?" + proxyApi;//匹配网址固定域名xxx
			//String regex = "https?://(www\\.)?geekplus\\.[\\w\\.-]+";//匹配网址带www前缀
			//String regex = "^(www\\.)?[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);

		return matcher.find();
	}

	public static Boolean isHasUrl1(String url, String proxyApi){
//		if(proxyApi == null || proxyApi.isEmpty()){
//			proxyApi = "";
//		}
		String regex = proxyApi; //匹配所有网址
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);

		return matcher.find();
	}

	/**
	  * @Author geekplus
	  * @Description //通用方法，判断是否包含有替换的内容
	  * @Param
	  * @Throws
	  * @Return {@link }
	  */
	public static Boolean isHasReplaceStr(String replaceContent, String replaceStr){
		String regex = replaceStr; //匹配字符
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(replaceContent);

		return matcher.find();
	}

	/**
	  * @Author geekplus
	  * @Description //判断url是否合法
	  * @Param
	  * @Throws
	  * @Return {@link }
	  */
	public static boolean isUrlValid(String url) {
		String urlPattern = "^((https?|ftp|file)://)?([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,6})(:[0-9]+)?(/[^\\s]*)?$";
		Pattern pattern = Pattern.compile(urlPattern);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	/**
	  * @Author geekplus
	  * @Description //获取url中IP和端口port
	  * @Param
	  * @Throws
	  * @Return {@link }
	  */
	public static String[] getHttpPort(String urlStr){
		String[] ipAddressPort = new String[2];
		// 定义 IP 地址和端口号的正则表达式
		String ipRegex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
		String portRegex = "(\\d{1,5})";
		// 构建完整的 URL 正则表达式
		String urlRegex = "^(http|https)://" + ipRegex + ":" + portRegex + "/.*$";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(urlRegex);
		// 创建 Matcher 对象
		Matcher matcher = pattern.matcher(urlStr);
		// 进行匹配
		if (matcher.matches()) {
			// 提取 IP 地址和端口号
			String ipAddress = matcher.group(1);
			String port = matcher.group(2);
			ipAddressPort[0] = ipAddress;
			ipAddressPort[1] = port;
		}
		return ipAddressPort;
	}
}
