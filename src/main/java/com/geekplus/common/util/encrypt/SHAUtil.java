package com.geekplus.common.util.encrypt;

import com.geekplus.common.constant.Constant;
import com.geekplus.common.util.uuid.UUIDUtil;

import java.security.MessageDigest;
import java.util.Date;


public class SHAUtil {
	public static String getSHA(String str){
		if(null == str || "".equals(str.trim())){
			str = Constant.DEFAULT_PASSWORD;
		}
    	try {
			MessageDigest sha = MessageDigest.getInstance("SHA");
			sha.update(str.getBytes());
			return bytesToString(sha.digest());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    private static String bytesToString(byte [] data){
    	// 非线程安全的
    	StringBuilder sb =new StringBuilder();
    	for(int i=0;i<data.length;i++){
    		int temp = 0;
    		if(data[i]<0){
    			temp = 256+data[i];
    		}else{
    			temp = data[i];
    		}
    		if(temp<16){
    			sb.append("0");
    		}
    		sb.append(Integer.toString(temp, 16));
    	}
    	return sb.toString().toUpperCase();
    }
    private static final Integer [] SALT_POSITION_ARR = {3,4,6,8,7,9,5,5,7,8};
    private static final String RND_STR = "C8E5D3F2A410B976";
    private static final StringBuffer STRING_BUFFER = new StringBuffer();
    /**
     * 获取加盐加hash加hash 替换hash值 的加密方式
    * @Title: getSHAStrBySaltAndNewMethod
    * @Description:
    * @param originalStr 欲加密的字符串
    * @return String return the encode string else return null if something is invalidate
    * @author WeiZheng
    * @date 2018年9月18日上午11:04:40
     */
    public static String getSHAStrBySaltAndNewMethod(String originalStr){
    	if(null == originalStr || "".equals(originalStr.trim())){
    		return null;
    	}
    	String newPwd = originalStr + originalStr + originalStr;
    	StringBuilder tailPwd = new StringBuilder();
    	/**
    	 * 截取密码中的七个字符生成新的密码串
    	 */
    	STRING_BUFFER.setLength(0);
    	for(int i=0;i<14;i++,i++){
    		Character tmp = newPwd.charAt(i);
    		tailPwd.append(tmp+SALT_POSITION_ARR[tmp%10]);
    		STRING_BUFFER.append(tmp);
		}
    	String subPwdStr = STRING_BUFFER.toString();
    	/**
    	 * 根据截取的字符串获取hashCode,尾部密码是7位字符添加当前字符形成的,最后根据当前员字符串长度进行截取相应字符 提高密码复杂度
    	 */
    	Integer startTailIndex = SALT_POSITION_ARR[originalStr.length()%10];
    	String hashRndStr = subPwdStr.hashCode()+"@*&"+tailPwd.toString()+"!%$"+newPwd.substring(startTailIndex, startTailIndex+originalStr.length()-2);
    	String hashRndStrSHA = getSHA(hashRndStr);
    	if(null == hashRndStrSHA){
    		return null;
    	}
    	/**
    	 * 进行加密后的hash值的替换
    	 */
    	Integer index = originalStr.length() % 10 + 3;
    	String []hashPwdArr = hashRndStrSHA.split("");
		for(int i=0;i<hashRndStrSHA.length();i=i+index){
			hashPwdArr[i] = RND_STR.charAt((i+index)%16)+"";
		}
		STRING_BUFFER.setLength(0);
		for(String tmp : hashPwdArr){
			STRING_BUFFER.append(tmp);
		}
    	return STRING_BUFFER.toString();
    }

    public static void main(String[] args) {
    	Long startTime = System.currentTimeMillis();
    	System.out.println("开始加密");
    	// 493E012F6E6CE75F4902AC6DDCADA732F5899740
    	System.out.println(getSHAStrBySaltAndNewMethod("123456"));;
		System.out.println("结束加密，耗时："+(System.currentTimeMillis() - startTime) +" 毫秒");
	}

}
