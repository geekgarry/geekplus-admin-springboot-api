package com.geekplus.common.util.baidu;

import com.baidu.aip.ocr.AipOcr;
import com.geekplus.common.util.PictureMerge;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度文字接口识别工具类
 *
 * @author geekplus
 *
 */
public class BaiduTextApiUtil {

	private static final Logger logger = LoggerFactory.getLogger(BaiduTextApiUtil.class);
	/**
	 * App ID
	 */
	public static final String APP_ID = "15261325";
	/**
	 * Api Key
	 */
	public static final String API_KEY = "1S8Ex4LKnafUmZRdGUMkY8C4";
	/**
	 * Secret
	 */
	public static final String SECRET_KEY = "B3WQzpLnfBMIi6DVVkWuN8xtUPV1NpP4";

	public static void main(String[] args) throws Exception {
        String path = "http://cdn.juheyinhang.com/1611f79f-6534-4961-98e0-492cc3fb67411544339769884.jpg";
        validateTextImg(path);
	}
	/**
	 * 验证识别远程文件 返回简化的map对象
	 * @param remotePath
	 * @return 识别成功的对象信息 反之返回 null
	 * @throws NullPointerException
	 */
    public static Map<String,Object> getValidateImgMap(String remotePath) throws NullPointerException{
    	if(null == remotePath || "".equals(remotePath.trim())) {
    		return null;
    	}
    	JSONObject jsonObject = validateTextImg(remotePath);
    	if(null != jsonObject) {
    		return textJsonObjectToMap(jsonObject);
    	}
    	return null;
    }
	public static JSONObject validateTextImg(String remotePath) {
		// 初始化一个AipOcr
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);// 建立连接的超时时间（单位：毫秒）
		client.setSocketTimeoutInMillis(60000);// 通过打开的连接传输数据的超时时间（单位：毫秒）
		// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
		// client.setHttpProxy("proxy_host", proxy_port); // 设置http代理
		// client.setSocketProxy("proxy_host", proxy_port); // 设置socket代理
		// 可选：设置log4j日志输出格式，若不设置，则使用默认配置
		// 也可以直接通过jvm启动参数设置此环境变量
		// System.setProperty("aip.log4j.conf",
		// "path/to/your/log4j.properties");
		// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<>();
		options.put("detect_direction", "true");// 是否检测图像朝向，默认不检测，即：false。
		options.put("detect_risk", "false");// 是否开启身份证风险类型(身份证复印件、临时身份证、身份证翻拍、修改过的身份证)功能，默认不开启，即：false。
		// front - 身份证含照片的一面(back - 身份证带国徽的一面)
		String idCardSide = "front";
		// 参数为本地图片二进制数组
		byte[] file = readImageFile2(remotePath);
		JSONObject res = client.idcard(file, idCardSide, options);
		System.out.println(res.toString(2));
		return res;
	}
    // http://cdn.juheyinhang.com/1611f79f-6534-4961-98e0-492cc3fb67411544339769884.jpg
	public static byte[] readImageFile2(String filePath) {
		String localPath = PictureMerge.saveImgToLocalFromRemote(filePath, "idcard");
		File file = new File(localPath);
		byte[] content = new byte[(int) file.length()];
		FileInputStream finputstream = null;
		try {
			finputstream = new FileInputStream(file);
			finputstream.read(content);
			finputstream.close();
		} catch (FileNotFoundException e) {
			logger.error("文件不存在");
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error("文件流异常");
			logger.error(e.getMessage());
		}
		return content;
	}
	/**
	 * 将智能识别的结果转化成map
	 * @param jsonObject 解析传回的jsonObject对象
	 * @return map code 200 401
	 */
	public static Map<String,Object> textJsonObjectToMap(JSONObject jsonObject){
		Map<String,Object> params = new HashMap<>();
		String status = jsonObject.getString("image_status");
		params.put("msg", status);
		if("normal".equals(status)){
			params.put("code", "200");
			JSONObject result = jsonObject.getJSONObject("words_result");
			if(null != result) {
				params.put("name", result.getJSONObject("姓名").getString("words"));
				params.put("nation", result.getJSONObject("民族").getString("words"));
				params.put("address", result.getJSONObject("住址").getString("words"));
				params.put("cardNo", result.getJSONObject("公民身份号码").getString("words"));
				params.put("birth", result.getJSONObject("出生").getString("words"));
				params.put("sex", result.getJSONObject("性别").getString("words"));
			}
			params.put("logId", jsonObject.getLong("log_id"));
		}else {
			params.put("code", "401");
		}
		return params;
	}
}
