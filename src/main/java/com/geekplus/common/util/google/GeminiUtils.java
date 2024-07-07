package com.geekplus.common.util.google;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geekplus.common.util.base64.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 5/26/24 11:27 PM
 * description: 做什么的？
 */
@Slf4j
public class GeminiUtils {
    //v1：API 的稳定版。稳定版本中的功能在主要版本的生命周期内完全受支持。如果存在任何重大更改，则会创建 API 的下一个主要版本，并在合理的一段时间后弃用现有版本。可以在不更改主要版本的情况下为该 API 引入非破坏性更改。
    //v1beta：此版本包含抢先体验版功能，这些功能可能正在开发中，并且可能会随时发生快速重大更改。也无法保证 Beta 版中的功能将迁移到稳定版。由于这种不稳定性，您不应使用此版本发布生产应用。
    //HARM_CATEGORY_DANGEROUS_CONTENT	<HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT: 10>
    //HARM_CATEGORY_HARASSMENT	<HarmCategory.HARM_CATEGORY_HARASSMENT: 7>
    //HARM_CATEGORY_HATE_SPEECH	<HarmCategory.HARM_CATEGORY_HATE_SPEECH: 8>
    //HARM_CATEGORY_SEXUALLY_EXPLICIT	<HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT: 9>
    //HARM_CATEGORY_UNSPECIFIED	<HarmCategory.HARM_CATEGORY_UNSPECIFIED: 0>
    //HARM_BLOCK_THRESHOLD_UNSPECIFIED	0 Threshold is unspecified.
    //BLOCK_LOW_AND_ABOVE	1 Content with NEGLIGIBLE will be allowed.
    //BLOCK_MEDIUM_AND_ABOVE	2 Content with NEGLIGIBLE and LOW will be allowed.
    //BLOCK_ONLY_HIGH	3 Content with NEGLIGIBLE, LOW, and MEDIUM will be allowed.
    //BLOCK_NONE	4 All content will be allowed.
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
        String requestJson="{\"text: \""+chatContent+"\"}";//构造RequestBody请求体
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    //Gemini AI Chat请求方法
    public static String postGemini(String chatContent,Object mediaData,String apiKey) {
        String geminiReply=null;
        String url="https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key="+apiKey;
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+apiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置
        //Google Gemini AI REST安全设置参数
        String safetySettings="\"safetySettings\": [\n" +
                "{\"category\": 10, \"threshold\": 4},\n" +
                "{\"category\": 9, \"threshold\": 4},\n" +
                "{\"category\": 8, \"threshold\": 4},\n" +
                "{\"category\": 7, \"threshold\": 4}\n" +
                "],";
        String requestJson="{"+
                safetySettings+
                "\"contents\":[\n" +
                "{\"parts\":[" +
                "{\"text\":\""+chatContent+"\"}\n" +
                "]}\n" +
                "]}";
        if(mediaData != null && !"".equals(mediaData)){
            //Base64Util.isBase64(mediaData.toString()) && Base64Util.isImageFromBase64(mediaData.toString())
            String mimeType= Base64Util.getFileMimeType(mediaData.toString());
            mediaData=Base64Util.getBase64Str(mediaData.toString());
            requestJson="{"+
                    safetySettings+
                    "\"contents\":[\n" +
                    "{\"parts\":[\n" +
                    "{\"text\":\""+chatContent+"\"},\n" +
                    "{\"inline_data\":\n" +
                    "{\"mime_type\": \""+mimeType+"\",\n" +
                    "\"data\": \""+mediaData+"\"\n" +
                    "}}\n" +
                    "]}\n" +
                    "]}";
        }
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
        String url="https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro:generateContent?key="+apiKey;
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+apiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置

        //String requestJson="{\"contents\":[{\"parts\":[{\"text\":\""+chatContent+"\"}]}]}";
        //Google Gemini AI REST安全设置参数
        String safetySettings="\"safetySettings\": [\n" +
                "{\"category\": 10, \"threshold\": 4},\n" +
                "{\"category\": 9, \"threshold\": 4},\n" +
                "{\"category\": 8, \"threshold\": 4},\n" +
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
            } else {
                geminiReply = "抱歉，我可能出了点问题，请稍后再试！";
            }
        }
        return geminiReply;
    }

    //Gemini AI Chat请求方法
    public static String postGeminiHistory(String chatContent,String chatContentJson,Object mediaData,String apiKey) {
        String geminiReply=null;
        String url="https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro:generateContent?key="+apiKey;
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
                "{\"category\": 10, \"threshold\": 4},\n" +
                "{\"category\": 9, \"threshold\": 4},\n" +
                "{\"category\": 8, \"threshold\": 4},\n" +
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
        if(mediaData != null && !"".equals(mediaData)){
            //Object tempMediaData = null;
            String mimeType= Base64Util.getFileMimeType(mediaData.toString());
            mediaData=Base64Util.getBase64Str(mediaData.toString());
            requestJson="{" + safetySettings +
                    "\"contents\":[\n" +
                    chatContentJson + "\n"+
                    "{\"role\":\"user\",\n" +
                    "\"parts\":[\n" +
                    "{\"text\": \""+chatContent+"\"},\n" +
                    "{\"inline_data\":\n" +
                    "{\"mime_type\": \""+mimeType+"\",\n" +
                    "\"data\": \""+mediaData+"\"\n" +
                    "}}\n" +
                    "]}\n" +
                    "]}";
        }
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
            } else {
                geminiReply = "抱歉，我可能出了点问题，请稍后再试！";
            }
        }
        return geminiReply;
    }
}
