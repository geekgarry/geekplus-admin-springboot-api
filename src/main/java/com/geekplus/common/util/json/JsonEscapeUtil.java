package com.geekplus.common.util.json;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON操作工具类
 */
public class JsonEscapeUtil {
	/**
	 * Bean对象转JSON
	 *
	 * @param object
	 * @param dataFormatString
	 * @return
	 */
	public static String beanToJson(Object object, String dataFormatString) {
		if (object != null) {
			if (StringUtils.isEmpty(dataFormatString)) {
				return JSONObject.toJSONString(object);
			}
			return JSON.toJSONStringWithDateFormat(object, dataFormatString);
		} else {
			return null;
		}
	}

	/**
	 * Bean对象转JSON
	 *
	 * @param object
	 * @return
	 */
	public static String beanToJson(Object object) {
		if (object != null) {
			return JSON.toJSONString(object);
		} else {
			return null;
		}
	}

	/**
	 * String转JSON字符串
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static String stringToJsonByFastjson(String key, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(key, value);
		return beanToJson(map, null);
	}

	/**
	 * 将json字符串转换成对象
	 *
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static Object jsonToBean(String json, Object clazz) {
		if (StringUtils.isEmpty(json) || clazz == null) {
			return null;
		}
		return JSON.parseObject(json, clazz.getClass());
	}

	/**
	 * json字符串转map
	 *
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String json) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		return JSON.parseObject(json, Map.class);
	}

	public static boolean isJsonEscaped(String jsonString) {
		String unescapedString = StringEscapeUtils.unescapeJson(jsonString);
		return !jsonString.equals(unescapedString);
	}

	public static void main(String[] args) {
//        String json = "<!DOCTYPE html>\n" +
//                "<html lang=\\\"en\\\">\n" +
//                "<head>\n" +
//                "  <meta charset=\\\"UTF-8\\\">\n" +
//                "  <meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1.0\\\">\n" +
//                "  <title>Code Highlighter Plugin</title>";
//        boolean isEscaped = isJsonEscaped(json);
//        System.out.println("Is JSON escaped: " + isEscaped);
		HashMap<String,Object> promptJson = new HashMap<>();
		promptJson.put("role", "user");
		List<HashMap<String,Object>> partsList = new ArrayList<>();
		HashMap<String,Object> partsContentMap=new HashMap<>();
		partsContentMap.put("text","textContent");
		HashMap<String,Object> inlineDataMap=new HashMap<>();
		inlineDataMap.put("mime_type", "mimeType");
		inlineDataMap.put("data", "fileData");
		partsContentMap.put("inline_data", inlineDataMap);
		partsList.add(partsContentMap);
		promptJson.put("parts", partsList);
//        System.out.println(JSON.toJSONString(promptJson, SerializerFeature.DisableCircularReferenceDetect));
//        System.out.println(new JSONObject(promptJson).toString());
//        String safetySettings="\"safetySettings\": [\n" +
//                "{\"category\": 10, \"threshold\": 4},\n" +
//                "{\"category\": 9, \"threshold\": 4},\n" +
//                "{\"category\": 8, \"threshold\": 4},\n" +
//                "{\"category\": 7, \"threshold\": 4}\n" +
//                "],\n";
		String json="{\"user\":\"sakjnf\",\"name\":\"sdkfjk\\:geekplus,shnjkvs\\:\\\"dshfndsjk\\\"\",\"password\":\"123456\"}";
//        char char1='\u9000';//与char1=99;一样，一样是字符编码
		JSONObject jsonObj=JSONObject.parseObject(json);
		System.out.println(jsonObj);
	}

	/**
	 * 处理调用接口回参中的特殊字符
	 * @param str
	 * @return
	 */
	public static String replaceJSON(String str){
		String replaceAll3 = null;
		if(!com.geekplus.common.util.string.StringUtils.isBlank(str)){
			String replaceAll = str.replaceAll("\\\\", "");
			//System.out.println(replaceAll);
			String replaceAll2 = replaceAll.replaceAll("\"[{]", "{");
			replaceAll3 = replaceAll2.replaceAll("[}]\"", "}");
		}

		return replaceAll3;
	}

	/**
	 * 在拼接json字符串时，处理字符串中国中存在一些特殊富豪，也被转义了，例如双引号
	 * @param str
	 * @return
	 */
	public static String fasterXmlEscapeString(String str){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// 使用writeValueAsString方法将字符串转换为JSON字符串，会自动进行转义
			return objectMapper.writeValueAsString(str);
		} catch (JsonProcessingException e) {
			// 处理异常
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public static String commonsEscapeString(String str){
		String escapedString = StringEscapeUtils.escapeJson(str);
		return escapedString;
	}

	public static String replaceEscapeString(String str){
		String escapedString = str.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\b", "\\b")
				.replace("\f", "\\f")
				.replace("\n", "\\n")
				.replace("\r", "\\r")
				.replace("\t", "\\t");
		return escapedString;
	}
}
