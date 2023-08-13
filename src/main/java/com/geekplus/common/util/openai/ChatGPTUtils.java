/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/23/23 21:29
 * description: 做什么的？
 */
package com.geekplus.common.util.openai;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ChatGPTUtils {
//    public static String hutoolPost(String url, String data, Map<String, String> headerMap) {
//
//
//        /**
//         * hutool提供的http工具
//         */
//        String result = HttpRequest.post(url)
//                .headerMap(headerMap, false)
//                .body(data)
//                //超时5分钟（不能超过5分钟要报错，因为微信后台的限制）
//                .timeout(200000)
//                .execute().body();
//
//        return result;
//
//    }

    //ChatGPT请求
    public static String sendChatGPTPost(String url, String chatContent, Map<String, String> headerMap) {
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+openAiKey);
        //httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置
//        String requestJson="{\"model\":\"text-davinci-003\",\"prompt\": \""+
//                chatContent+"\"," +"\"temperature\":0,\"max_tokens\":3069}";
//        String requestJson = String.format(
//                "{"+
//                "\"model\": \"text-davinci-003\", %n" +
//                "\"prompt\": \"%s\", %n" +
//                "\"temperature\": 0, %n" +
//                "\"max_tokens\": 2048 %n" +
//                "}", data
//        );
        if(url==null||url==""){
            url="https://api.openai.com/v1/completions";
        }
        HttpEntity<String> entity = new HttpEntity<>(chatContent, httpHeaders);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
//        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//        JSONArray choices = jsonObject.getJSONArray("choices");
//        String text = choices.getJSONObject(0).getString("text");
        //Object o = jsonObject.get("\\"choices\\"");
        return response.getBody();
    }

    //ChatGPT请求第二种方法
    public static String chatGPTPost(String chatContent,String openAiKey) {
        String url="https://api.openai.com/v1/completions";
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        httpHeaders.add("Authorization", "Bearer "+openAiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置

        String requestJson="{\"model\":\"text-davinci-003\",\"prompt\": \""+chatContent+"\"," +
                "\"temperature\":0,\"max_tokens\":3069}";
//        String requestJson = String.format(
//                "{"+
//                "\"model\": \"text-davinci-003\", %n" +
//                "\"prompt\": \"%s\", %n" +
//                "\"temperature\": 0, %n" +
//                "\"max_tokens\": 2048 %n" +
//                "}", data
//        );
        HttpEntity<String> entity = new HttpEntity<>(chatContent, httpHeaders);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
//        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//        JSONArray choices = jsonObject.getJSONArray("choices");
//        String text = choices.getJSONObject(0).getString("text");
        //Object o = jsonObject.get("\\"choices\\"");
        return response.getBody();
    }
}
