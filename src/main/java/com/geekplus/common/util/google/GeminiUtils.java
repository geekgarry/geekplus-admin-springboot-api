/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 5/26/24 11:27 PM
 * description: 做什么的？
 */
package com.geekplus.common.util.google;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geekplus.common.util.string.StringUtil;
import com.geekplus.common.util.string.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
public class GeminiUtils {

    //轻量级API
    String apiUrl="https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=YOUR_API_KEY";

    //Google Gemini AI请求
    public static String sendGeminiPost(String url, String chatContent, Map<String, String> headerMap) {
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headerMap);
        if(url==null||"".equals(url)){
            url="https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";
        }
        String requestJson="{\"text: \""+chatContent+"\"";//构造RequestBody请求体
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    //Gemini AI Chat请求方法
    public static String postGemini(String chatContent,String apiKey) {
        String geminiReply=null;
        String url="https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="+apiKey;
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+apiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置
        //Google Gemini AI REST安全设置参数
        String safetySettings="\"safetySettings\": [\n" +
                "{\"category\": 7, \"threshold\": 4}\n" +
                "],";
        String requestJson="{"+
                safetySettings+
                "\"contents\":[{\"parts\":[{\"text\":\""+chatContent+"\"}]}]" +
                "}";
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        String candidatesPart=response.getBody();
        //System.out.println(response.getBody());
        log.info(response.getBody());
        JSONObject jsonObject = JSONObject.parseObject(candidatesPart);
        if (!CollectionUtils.isEmpty(jsonObject.getJSONArray("candidates"))) {
            JSONArray candidates = jsonObject.getJSONArray("candidates");
            if (candidates.getJSONObject(0).containsKey("content")) {
                JSONObject candidatesContent = candidates.getJSONObject(0).getJSONObject("content");
                JSONArray contentParts = candidatesContent.getJSONArray("parts");
                String contentRole = candidatesContent.getString("role");
                geminiReply = contentParts.getJSONObject(0).getString("text");
                //String messagepre = messageResponseBody.getChoices().get(0).getText();
                //AIReplyText.substring(2);
            } else {
                geminiReply = "抱歉，我可能出了点问题，请稍后再试！";
            }
        }
        return geminiReply;
    }

    //Gemini AI Chat请求方法
    public static String postImageGemini(String chatContent,String base64Image,String apiKey) {
        String geminiReply=null;
        String url="https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="+apiKey;
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+apiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置

        //String requestJson="{\"contents\":[{\"parts\":[{\"text\":\""+chatContent+"\"}]}]}";
        //Google Gemini AI REST安全设置参数
        String safetySettings="\"safetySettings\": [\n" +
                "{\"category\": 7, \"threshold\": 4}\n" +
                "],";
        String requestJson = String.format(
                "{" + safetySettings +
                "\"contents\":[" +
                "{\"parts\":["+
                "{\"text\": \"%s\"}, %n" +
                "{\"inline_data\":{" +
                "\"mime_type\": \"image/jpeg\", %n" +
                "\"data\": \""+base64Image+"\" %n" +
                "}}" +
                "]}" +
                "]}", chatContent
        );
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        log.info(response.getBody());
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        if (!CollectionUtils.isEmpty(jsonObject.getJSONArray("candidates"))) {
            JSONArray candidates = jsonObject.getJSONArray("candidates");
            if (candidates.getJSONObject(0).containsKey("content")) {
                JSONObject candidatesContent = candidates.getJSONObject(0).getJSONObject("content");
                JSONArray contentParts = candidatesContent.getJSONArray("parts");
                String contentRole = candidatesContent.getString("role");
                geminiReply = contentParts.getJSONObject(0).getString("text");
                //String messagepre = messageResponseBody.getChoices().get(0).getText();
                //AIReplyText.substring(2);
            } else {
                geminiReply = "抱歉，我可能出了点问题，请稍后再试！";
            }
        }
        return geminiReply;
    }

    //Gemini AI Chat请求方法
    public static String postGeminiHistory(String chatContent,String chatContentJson,String apiKey) {
        String geminiReply=null;
        String url="https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="+apiKey;
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+apiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置
//        String requestJson1 = String.format(
//                "{\"contents\":[" +
//                        "{\"role\":\"user\",\n" +
//                        "\"parts\":[{\n" +
//                        "\"text\": \"%s\"}]" +
//                        "}, %n" +
//                        "{\"role\":\"model\",\n" +
//                        "\"parts\":[{\n" +
//                        "\"text\": \"%s\"}]" +
//                        "}, %n" +
//                "]}", chatContent,chatContent
//        );
        //Google Gemini AI REST安全设置参数
        String safetySettings="\"safetySettings\": [\n" +
                "{\"category\": 7, \"threshold\": 4}\n" +
                "],\n";
//        String requestJson="{" + safetySettings +
//                "\"contents\":"+ chatContentJson
//                +"}";
        String requestJson ="{" + safetySettings +
                "\"contents\":[\n" +
                chatContentJson + "\n"+
                  "{\"role\":\"user\",\n" +
                  "\"parts\":[{\n" +
                  "\"text\": \""+chatContent+"\"}]}\n" +
             "]}";
        log.info(requestJson);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        log.info(response.getBody());
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        if (!CollectionUtils.isEmpty(jsonObject.getJSONArray("candidates"))) {
            JSONArray candidates = jsonObject.getJSONArray("candidates");
            if (candidates.getJSONObject(0).containsKey("content")) {
                JSONObject candidatesContent = candidates.getJSONObject(0).getJSONObject("content");
                JSONArray contentParts = candidatesContent.getJSONArray("parts");
                String contentRole = candidatesContent.getString("role");
                geminiReply = contentParts.getJSONObject(0).getString("text");
                //String messagepre = messageResponseBody.getChoices().get(0).getText();
                //AIReplyText.substring(2);
            } else {
                geminiReply = "抱歉，我可能出了点问题，请稍后再试！";
            }
        }
        return geminiReply;
    }
}
