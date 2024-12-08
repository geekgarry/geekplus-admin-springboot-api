package com.geekplus.common.util;

import com.geekplus.common.util.http.HttpRequestUtil;
import com.geekplus.common.util.json.JsonObjectUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 阿里云银行卡检测 手机号 银行卡 身份证 姓名
 * @author zheng
 *
 */
public class AliBankCheckUtil {

	/**
	 * 检查银行卡合法性
	 * @param mobile 手机号码
	 * @param bankcard 银行卡
	 * @param cardNo 身份证号码
	 * @param realName 真实名称
	 * @return
	 */
	public static Map<String,Object> checkBankValidate(String mobile,String bankcard,String cardNo,String realName) {
		 Map<String,Object> backMap = null ;
		 String host = "https://bankpro.market.alicloudapi.com";
		 String path = "/bankcard";
		 String method = "POST";
		 String appcode = "f7583137ad5f40768a37b8afe97b0a28";
		 Map<String, String> headers = new HashMap<String, String>();
		 //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		 headers.put("Authorization", "APPCODE " + appcode);
		 Map<String, String> querys = new HashMap<String, String>();
		 querys.put("Mobile", mobile);
		 querys.put("bankcard", bankcard);
		 querys.put("cardNo", cardNo);
		 querys.put("realName", realName);
		 Map<String, String> bodys = new HashMap<String, String>();
		 try {
		    	/**
		    	* 重要提示如下:
		    	* HttpUtils请从
		    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
		    	* 下载
		    	*
		    	* 相应的依赖请参照
		    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
		    	*/
		    	HttpResponse response = HttpRequestUtil.doPost(host, path, method, headers, querys, bodys);
		    	System.out.println(response.toString());
		    	//获取response的body
		    	String jsonBody = EntityUtils.toString(response.getEntity());
		    	System.out.println(jsonBody);
		    	backMap = getResponObjec(jsonBody);
		 } catch (Exception e) {
		    e.printStackTrace();
		 }
		 return backMap;
	}

	public static Map<String,Object> getResponObjec(String jsonString) {
		Map<String,Object> backMap = (Map<String, Object>) JsonObjectUtil.jsonToMap(jsonString);
		return backMap;
	}
	public static void main(String[] args) {
		// {"error_code":0,"reason":"认证通过","result":{"bankcard":"6217730506090529","realName":"魏政","cardNo":"321321199511077216","mobile":"15651657858","information":{"bankname":"中信银行","banknum":"3020000","cardprefixnum":"621773","cardname":"理财宝IC卡","cardtype":"银联借记卡","cardprefixlength":6,"isLuhn":true,"iscreditcard":1,"cardlength":16,"bankurl":"http://bank.ecitic.com/","enbankname":"China Citic Bank","abbreviation":"CITIC","bankimage":"http://auth.apis.la/bank/10_CITIC.png","servicephone":"95558"}},"ordersign":"20181107114550164251015641"}
		String mobile = "15651657858";
		String bankcard = "6217730506090529";
		String cardNo = "321321199511077216";
		String realName = "魏政";
		//checkBankValidate(mobile, bankcard, cardNo, realName);
		Map<String,Object> backMap = getResponObjec("{\"error_code\":0,\"reason\":\"认证通过\",\"result\":{\"bankcard\":\"6217730506090529\",\"realName\":\"魏政\",\"cardNo\":\"321321199511077216\",\"mobile\":\"15651657858\",\"information\":{\"bankname\":\"中信银行\",\"banknum\":\"3020000\",\"cardprefixnum\":\"621773\",\"cardname\":\"理财宝IC卡\",\"cardtype\":\"银联借记卡\",\"cardprefixlength\":6,\"isLuhn\":true,\"iscreditcard\":1,\"cardlength\":16,\"bankurl\":\"http://bank.ecitic.com/\",\"enbankname\":\"China Citic Bank\",\"abbreviation\":\"CITIC\",\"bankimage\":\"http://auth.apis.la/bank/10_CITIC.png\",\"servicephone\":\"95558\"}},\"ordersign\":\"20181107114550164251015641\"}");
		Map<String,Object> result = (Map<String, Object>) backMap.get("result");
		Map<String,Object> information = (Map<String, Object>)result.get("information");
		System.out.println("银行卡名称："+(String)information.get("bankname"));
		System.out.println("其它信息："+information.get("abbreviation")+";"+information.get("cardtype"));
	}
}
