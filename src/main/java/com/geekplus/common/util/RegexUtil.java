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
    public  boolean validate(String valStr,String regex){
    	Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(valStr);
    	return matcher.matches();
    }
    /**
	 * 用于将过滤关键词列表 转化成正则格式
	 * @param lists 字符数组列表 使用 | 分割  例如 : hello|world
	 * @return
	 */
	public  String stringListToRegexList(String lists){
		StringBuilder sb = new StringBuilder();
		sb.append(".*(");
		sb.append(lists);
		sb.append(")+.*");
		return sb.toString();
	}
    public static void main(String[] args) {
    	RegexUtil regext = new RegexUtil();
    	System.out.println(regext.validate("15651657858",RegexUtil.IS_PHONE_NUMBER_PATTERN));
	}
	 /**
     * 验证设备类型
     * @param userAgent
     * @return true 移动端 false pc端
     */
    public  boolean checkDeviceType(String userAgent){
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
}
