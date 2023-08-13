package com.geekplus.common.util;

import com.geekplus.common.constant.Constant;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.*;


/**
 * @ClassName StringUtil
 * @Description 字符串工具类
 * @Author Zheng
 * @Date 2017年12月31日 11:56:18
 */
public class StringUtil {

	private static final String RND_STRING = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";
	private static final String RND_NUMBER = "0123456789";
	private static final Random rnd = new Random();
	private static StringBuilder buf = new StringBuilder();
	/**
	 *使用UUID生成随机字符
	 * @return
	 */
    public static String getRndString(){
    	String uuid = UUID.randomUUID().toString();
    	return uuid;
    }
    /**
     * 随机字符串 UUID + Timestamp
     * @return
     */
    public static String getRndString2(){
    	String uuid = UUID.randomUUID().toString()+new Date().getTime();
    	return uuid;
    }
    /**
     * 获得主键ֵ key+uuid+timestamp
     * @param key  null 默认 zheng
     * @return 128 位加密
     */
    public static String getMainID(String key){
    	if(null == key){
    		key = Constant.APP_MAIN_ID_KEY;
    	}
    	StringBuffer sb = new StringBuffer();
    	sb.append(key);
    	sb.append(getRndString());
    	sb.append(new Date().getTime());
    	return SHAUtil.getMD5(sb.toString());
    }
    /**
     * 验证传入参数是否不为空后者正整数
     * @param object
     * @return true is null false not null
     */
    public static boolean validNotNull(Object object){
    	boolean flag = false;
    	/**
    	 * 判断是否为null
    	 */
    	if(null == object){
    		flag = true;
    	}else{
    		/**
        	 * 判断类型
        	 * TODO 后续可以进行添加其它类型判断
        	 */
        	if(object instanceof Integer){
        		Integer integer = (Integer)object;
        		if(integer<1){
        			flag = true;
        		}
        	}else if(object instanceof String){
        		String string = (String)object;
        		flag = string.isEmpty();
        	}
    	}
    	return flag;
    }
    /**
     * 获取随机字符串 较短数量
     * @param length
     * @return
     */
    public static String getRndStr(Integer length){
    	String backMsg = "";
    	for(int i=0;i<length;i++){
    		backMsg += RND_STRING.charAt(rnd.nextInt(RND_STRING.length()));
    	}
    	return backMsg;
    }
    public static Boolean testRndStr(Integer size,Integer strSize,Boolean isDetail){
    	long startTime = System.currentTimeMillis();
    	if(size < 100 || size >100001){
    		size = 5000;
    	}
    	if(strSize < 3 || strSize >100){
    		strSize = 10;
    	}
    	List<String> arrayList = new ArrayList<>();
    	Set<String> hashSet = new HashSet<>();
    	for(int i=0;i<size;i++){
    		String value = getRndStr(strSize);
    		arrayList.add(value);
    		hashSet.add(value);
    	}
    	if(isDetail){
    		System.out.println("预生成随机数个数: " + size + ",尺寸: " + strSize);
        	System.out.println("ArrayList size: " + arrayList.size());
        	System.out.println("HashSet size: " + hashSet.size());
        	System.out.println("总共消耗: "+(System.currentTimeMillis()-startTime)+" 毫秒");
    	}
    	//System.out.println("比率: " + hashSet.size() + "/" + arrayList.size());
    	return (arrayList.size() == hashSet.size());
    }
    public static String getRndNum(Integer length){
    	String backMsg = "";
    	for(int i=0;i<length;i++){
    		backMsg += RND_STRING.charAt(rnd.nextInt(RND_NUMBER.length()));
    	}
    	return backMsg;
    }
    /**
     * md5加密
     * @param key
     * @return
     */
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取用户分享码 全局唯一
     * timestamp + rnd 6 number
     * @return
     */
    public static String getShareKeyCode(){
    	return MD5(""+new Date().getTime()+getRndNum(6));
    }
    public static void main(String[] args) {
//    	String appString = "zheng";
//	    System.out.println(StringUtil.getMainID(appString));
//	    System.out.println(StringUtil.getMainID(appString));
//    	for(int i=0;i<10;i++){
//    		System.out.println(getRndStr(6));
//    	}
//    	for(int i=0;i<15;i++){
//    		System.out.println(StringUtil.getMainID(null));
//    	}
    	//System.out.println(getShareKeyCode());
    	//System.out.println(HloveyRC4("ÏnBÈöÒ2þß	y7"));
//    	Integer isTrue = 0;
//    	for (int i = 0; i < 100; i++) {
//    		if(testRndStr(100000, 8,false)){
//    			isTrue ++;
//    		}
//		}
//    	System.out.println("测试总数: "+100+"/"+isTrue);
    	//$2a$10$mYtRg282kepowy1XlRRJueO8YJ56S7IN8aw9BLiYTwBKvcs8g8.ru
    	//String encrptPWd = springSecurityEncrpt("Uwsu3wdW");
    	//String encrptPWd = springSecurityEncrpt("12345678");
    	//System.out.println(encrptPWd);
    	// $2a$10$kDMqweJAIZdOSXu/sp5Ew./JuBTDiq/6Tk1Pc9ff5xTWB8kNpkQ1m
    	//System.out.println("$2a$10$C8kXIo6dp5ZIo9I4YRBVsOuvLbRWrSQ4vu91B9djRT8Dh6RSJRzEW".equals(encrptPWd));
	}
    /**
     *
    * @Title: HloveyRC4
    * @Description:
    * @param aInput 输入的字符
    * @return String 返回加密、解密后的字符 可以自动获取方式
    * @author WeiZheng
    * @date 2018年8月8日上午11:29:35
     */
    public static String HloveyRC4(String aInput)
    {
    	String aKey = "Iloveyou.123";
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i=0;i<256;i++)
            iS[i]=i;

        int j = 1;

        for (short i= 0;i<256;i++)
        {
            iK[i]=(byte)aKey.charAt((i % aKey.length()));
        }
        j=0;
        for (int i=0;i<255;i++)
        {
            j=(j+iS[i]+iK[i]) % 256;
            int temp = iS[i];
            iS[i]=iS[j];
            iS[j]=temp;
        }
        int i=0;
        j=0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for(short x = 0;x<iInputChar.length;x++)
        {
            i = (i+1) % 256;
            j = (j+iS[i]) % 256;
            int temp = iS[i];
            iS[i]=iS[j];
            iS[j]=temp;
            int t = (iS[i]+(iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char)iY;
            iOutputChar[x] =(char)( iInputChar[x] ^ iCY) ;
        }
        return new String(iOutputChar);
    }
    /**
     * 拼接字符串
    * @Title: appendStr
    * @Description:
    * @param original
    * @param next
    * @param separator
    * @return String
    * @author WeiZheng
    * @date 2018年8月24日上午10:01:27
     */
    public static String appendStr(String original,String next,String separator){
    	buf.setLength(0);
    	buf.append(original);
    	buf.append(separator);
    	buf.append(next);
    	return buf.toString();
    }
    /**
     * 生成订单标题内容
    * @Title: genOrderSubject
    * @Description:
    * @param name 第一件商品名称
    * @param count 总订单商品数量
    * @return String **..等共**件商品
    * @author WeiZheng
    * @date 2018年8月24日上午10:03:22
     */
    public static String genOrderSubject(String name,Integer count){
    	buf.setLength(0);
    	buf.append(name);
    	buf.append("..等共");
    	buf.append(count);
    	buf.append("件商品");
    	return buf.toString();
    }
    public static String getIpAddress(HttpServletRequest request) {
    	String strUnknow = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || strUnknow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || strUnknow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || strUnknow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || strUnknow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || strUnknow.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(',') != -1) {
            ip = ip.substring(0, ip.indexOf(',')).trim();
        }

        return ip;
    }

    public static boolean isNotNull(Integer params) {
        boolean flag = true;
        /**
         * 判断是否为null
         */
        if(null == params|| params < 1){
            flag = false;
        }
        return flag;
    }
}
