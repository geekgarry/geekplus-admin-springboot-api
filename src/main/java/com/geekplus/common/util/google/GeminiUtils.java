/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 5/26/24 11:27 PM
 * description: 做什么的？
 */
package com.geekplus.common.util.google;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class GeminiUtils {

    //Google Gemini AI请求
    public static String sendGeminiPost(String url, String chatContent, Map<String, String> headerMap) {
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headerMap);
        if(url==null||url==""){
            url="https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";
        }
        HttpEntity<String> entity = new HttpEntity<>(chatContent, httpHeaders);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    //Gemini AI Chat请求方法
    public static String postGemini(String chatContent,String apiKey) {
        String url="https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="+apiKey;
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+apiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置

        String requestJson="{\"contents\":[{\"parts\":[{\"text\":\""+chatContent+"\"}]}]}";
//        String requestJson = String.format(
//                "{"+
//                "\"model\": \"text-davinci-003\", %n" +
//                "\"prompt\": \"%s\", %n" +
//                "\"temperature\": 0, %n" +
//                "\"max_tokens\": 2048 %n" +
//                "}", data
//        );
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
//        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//        JSONArray choices = jsonObject.getJSONArray("choices");
//        String text = choices.getJSONObject(0).getString("text");
        //Object o = jsonObject.get("\\"choices\\"");
        return response.getBody();
    }
}
