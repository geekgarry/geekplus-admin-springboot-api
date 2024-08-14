package com.geekplus.common.util.google;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geekplus.common.domain.ChatPrompt;
import com.geekplus.common.util.base64.Base64Util;
import com.geekplus.common.util.file.FileUtils;
import com.geekplus.common.util.json.JsonEscapeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.entity.ContentType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.util.*;

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
    //curl \
    //-H 'Content-Type: application/json' \
    //-d '{"contents":[{"parts":[{"text":"Explain how AI works"}]}]}' \
    //-X POST 'https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=YOUR_API_KEY'
    //streamGenerateContent?alt=sse
    String apiUrl="https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=YOUR_API_KEY";

    private JSONObject requestData;

    //Google Gemini AI REST安全设置参数
    private static String safetySettings = "\"safetySettings\": [\n" +
            "{\"category\": 10, \"threshold\": 4},\n" +
            "{\"category\": 9, \"threshold\": 4},\n" +
            "{\"category\": 8, \"threshold\": 4},\n" +
            "{\"category\": 7, \"threshold\": 4}\n" +
            "],\n";

    public GeminiUtils(){
    }
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

    //Gemini AI Chat请求方法,构造请求json或参数使用字符串拼接的方式
    public static String postGemini(ChatPrompt chatPrompt,String apiKey) throws IOException {
        String geminiReply = null;
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro:generateContent?key=" + apiKey;
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+apiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置
        HttpEntity<?> entity;
        //这里没有采用Map来构造消息请求体，而是直接拼接字符串
        //Google Gemini AI REST安全设置参数
        StringBuffer requestJson = new StringBuffer("{" + safetySettings);
        // 判断消息提示中是否带有媒体文件的数据内容，为空测没有，表示是一次普通的文本消息请求，
        // 否则就是带有文件数据，具体再分析数据类型
        if(ObjectUtils.isEmpty(chatPrompt.getMediaData())){
            requestJson.append("\"contents\":[\n" +
                    "{\"parts\":[" +
                    "{\"text\":\"" + JsonEscapeUtil.replaceEscapeString(chatPrompt.getChatMsg()) + "\"}\n" +
                    "]}\n" +
                    "]}");
            entity = new HttpEntity<String>(requestJson.toString(), httpHeaders);
        }else{
            String mimeType = chatPrompt.getMediaMimeType();//Base64Util.getFileMimeType(chatPrompt.getMediaData().toString())
            //Base64Util.isBase64(mediaData.toString()) && Base64Util.isImageFromBase64(mediaData.toString())
            //判断是否是字符串形式的数据类型，因为前端发送的是base64字符串编码后的文件
            if(chatPrompt.getMediaData() instanceof String || FileUtils.isStringType(chatPrompt.getMediaData())) {
                requestJson.append("\"contents\":[\n" +
                        "{\"parts\":[\n" +
                        "{\"text\":\"" + JsonEscapeUtil.replaceEscapeString(chatPrompt.getChatMsg()) + "\"},\n" +
                        "{\"inline_data\":\n" +
                        "{\"mime_type\": \"" + mimeType + "\",\n" +
                        "\"data\": \"" + Base64Util.getBase64Str(chatPrompt.getMediaData().toString()) + "\"\n" +
                        "}}\n" +
                        "]}\n" +
                        "]}");
//                requestJson = String.format("{" + safetySettings +
//                "\"contents\":[" +
//                "{\"parts\":["+
//                "{\"text\": \"%s\"}, %n" +
//                "{\"inline_data\":{" +
//                "\"mime_type\": \""+mimeType+"\", %n" +
//                "\"data\": \""+Base64Util.getBase64Str(chatPrompt.getMediaData().toString())+"\" %n" +
//                "}}" +
//                "]}" +
//                "]}", JsonEscapeUtil.replaceEscapeString(chatPrompt.getChatMsg()));
                entity = new HttpEntity<String>(requestJson.toString(), httpHeaders);
            }else{
                //byte[] fileToByte = (byte[]) chatPrompt.getMediaData();
                //ByteArrayResource resource = new ByteArrayResource(fileToByte){
                //@Override
                //public String getFilename() {
                //return fileName;
                //}
                //};
                httpHeaders.set("Content-Type","multipart/form-data");// 传递带有文件的请求
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//                objectOutputStream.writeObject(chatPrompt.getMediaData());
//                objectOutputStream.flush();
//                byte[] byteStream = byteArrayOutputStream.toByteArray();
                // 数组转输入流
                InputStream inputStream = new ByteArrayInputStream((byte[]) chatPrompt.getMediaData());
                // 输入流转MultipartFile对象
                MultipartFile multipartFile = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                // 把MultipartFile这个对象转成输入流资源(InputStreamResource)
                InputStreamResource isr = new InputStreamResource(multipartFile.getInputStream(), chatPrompt.getMediaFileName());
                requestJson.append("\"contents\":[\n" +
                        "{\"parts\":[\n" +
                        "{\"text\":\"" + JsonEscapeUtil.replaceEscapeString(chatPrompt.getChatMsg()) + "\"},\n" +
                        "{\"inline_data\":\n" +
                        "{\"mime_type\": \"" + mimeType + "\",\n" +
                        "\"data\": \"" + isr + "\"\n" +
                        "}}\n" +
                        "]}\n" +
                        "]}");
                MultiValueMap<String, Object> formChatPromptMap = new LinkedMultiValueMap<>();
                formChatPromptMap.setAll(createSafetySettingsMap());
                Map<String,Object> msgByteDataMap = createMsgPromptMap("user", chatPrompt.getChatMsg(), mimeType, isr);
                formChatPromptMap.put("contents", Collections.singletonList(msgByteDataMap));
                entity = new HttpEntity<MultiValueMap<String,Object>>(formChatPromptMap, httpHeaders);
            }
        }
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        String candidatesResponse=response.getBody();
        log.info("响应数据 {}", response.getBody());
        JSONObject jsonObject = JSONObject.parseObject(candidatesResponse);
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
        }else{
            log.info("错误消息："+jsonObject.toString());
            //{"error": { "code": 200, "message": "", "status": "" }}
            JSONObject errorData = jsonObject.getJSONObject("error");
            geminiReply = errorData.get("message").toString();
        }
        return geminiReply;
    }

    //Gemini AI Chat请求方法
    public static Object postStreamGemini(ChatPrompt chatPrompt, String apiKey) throws IOException {
        String geminiReply="";
        //https://generativelanguage.googleapis.com/v1beta/{model=models/*}:streamGenerateContent
        String url="https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:streamGenerateContent?alt=sse&key="+apiKey;
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+apiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置
        HttpEntity<?> entity;
        //历史消息列表
        List<Map<String,Object>> historyChatDataList = chatPrompt.getHistoryChatData();
        //构造了最外层的一个包含所有Json的key:value的包裹Map
        Map<String,Object> finalChatPromptMap = new HashMap<>();
        //首先向包裹填装基本不变的安全设置Map
        finalChatPromptMap.putAll(createSafetySettingsMap());//put("safetySettings",null);
        // 判断消息提示中是否带有媒体文件的数据内容，为空测没有，表示是一次普通的文本消息请求，
        // 否则就是带有文件数据，具体再分析数据类型
        if(ObjectUtils.isEmpty(chatPrompt.getMediaData())){
            //为发送的消息构造一个消息内容Map
            Map<String,Object> commonMsgDataMap = createMsgPromptMap("user", chatPrompt.getChatMsg(),null,null);
            //因为是聊天模式，所以向历史消息列表添加这个新构造的消息Map
            historyChatDataList.add(commonMsgDataMap);
            //最后把消息主题内容添加到key为contents的Map
            finalChatPromptMap.put("contents",historyChatDataList);
            //最后把构造的Map消息放入entity请求体，这里就相当于前端的json放入RequestBody
            entity = new HttpEntity<>(finalChatPromptMap, httpHeaders);
        }else{
            String mimeType= chatPrompt.getMediaMimeType();//Base64Util.getFileMimeType(chatPrompt.getMediaData().toString());
            //判断是否是字符串形式的数据类型，因为前端发送的是base64字符串编码后的文件
            if(chatPrompt.getMediaData() instanceof String || FileUtils.isStringType(chatPrompt.getMediaData())) {
                //因为发送消息携带base64文件，为发送的消息构造一个消息内容Map，里面再次添加inline_data等所需要的内容，这里重新设置一个新的消息Map，因为这个是携带媒体文件数据的消息
                Map<String,Object> msgMediaDataMap = createMsgPromptMap("user", chatPrompt.getChatMsg(), mimeType, Base64Util.getBase64Str(chatPrompt.getMediaData().toString()));
                //还是一样添加到所有聊天记录list
                historyChatDataList.add(msgMediaDataMap);
                //同样添加到最外的构造消息请求体的Map
                finalChatPromptMap.put("contents", historyChatDataList);
                entity = new HttpEntity<>(finalChatPromptMap, httpHeaders);
            }else{
                httpHeaders.set("Content-Type", "multipart/form-data"); // 传递请求体时必须设置
//                byte[] fileToByte = (byte[]) chatPrompt.getMediaData();
//                ByteArrayResource resource = new ByteArrayResource(fileToByte);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(chatPrompt.getMediaData());
                objectOutputStream.flush();
                byte[] byteStream = byteArrayOutputStream.toByteArray();
                //这里与上面一样，就是类型变成MultiValueMap，这是上传大型文件
                MultiValueMap<String, Object> formChatPromptMap = new LinkedMultiValueMap<>();
                formChatPromptMap.setAll(createSafetySettingsMap());
                Map<String,Object> msgByteDataMap = createMsgPromptMap("user", chatPrompt.getChatMsg(), mimeType, byteStream);
                historyChatDataList.add(msgByteDataMap);
                formChatPromptMap.addAll("contents", historyChatDataList);
                //formChatPromptMap.setAll(JSONObject.parseObject(requestJson));
                entity = new HttpEntity<MultiValueMap<String,Object>>(formChatPromptMap, httpHeaders);
            }
        }
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        String candidatesPart=response.getBody();
        log.info("响应数据 {}",response.getBody());
        //JSONObject jsonObject = JSONObject.parseObject(candidatesPart);
//        if (!CollectionUtils.isEmpty(jsonObject.getJSONArray("candidates"))) {
//            JSONArray candidates = jsonObject.getJSONArray("candidates");
//            if (candidates.getJSONObject(0).containsKey("content")) {
//                JSONObject candidatesContent = candidates.getJSONObject(0).getJSONObject("content");
//                JSONArray contentParts = candidatesContent.getJSONArray("parts");
//                String contentRole = candidatesContent.getString("role");
//                geminiReply = contentParts.getJSONObject(0).getString("text");
//                //String messagepre = messageResponseBody.getChoices().get(0).getText();
//                //AIReplyText.substring(2);
//            } else {
//                geminiReply = "抱歉，我可能出了点问题，请稍后再试！";
//            }
//        }
        return candidatesPart;
    }

    //Gemini AI Chat请求方法
    public static String postGeminiHistory(ChatPrompt chatPrompt, String apiKey) throws IOException {
        String geminiReply=null;
        String url="https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key="+apiKey;
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAll(headerMap);
        //httpHeaders.add("Authorization", "Bearer "+apiKey);
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置
        HttpEntity<?> entity;
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
        //json字符串拼接
        String requestJson ="{" + safetySettings +
                "\"contents\":[\n" +
                chatPrompt.getPreChatData() + "\n"+
                "{\"role\":\"user\",\n" +
                "\"parts\":[{\n" +
                "\"text\": \""+chatPrompt.getChatMsg()+"\"}]}\n" +
                "]}";
        //历史消息列表
        List<Map<String,Object>> historyChatDataList = chatPrompt.getHistoryChatData();
        //构造了最外层的一个包含所有Json的key:value的包裹Map
        Map<String,Object> finalChatPromptMap = new HashMap<>();
        //首先向包裹填装基本不变的安全设置Map
        finalChatPromptMap.putAll(createSafetySettingsMap());//put("safetySettings",null);
        // 判断消息提示中是否带有媒体文件的数据内容，为空测没有，表示是一次普通的文本消息请求，
        // 否则就是带有文件数据，具体再分析数据类型
        if(ObjectUtils.isEmpty(chatPrompt.getMediaData())){
            //为发送的消息构造一个消息内容Map
            Map<String,Object> commonMsgDataMap = createMsgPromptMap("user", chatPrompt.getChatMsg(),null,null);
            //因为是聊天模式，所以向历史消息列表添加这个新构造的消息Map
            historyChatDataList.add(commonMsgDataMap);
            //最后把消息主题内容添加到key为contents的Map
            finalChatPromptMap.put("contents",historyChatDataList);
            //最后把构造的Map消息放入entity请求体，这里就相当于前端的json放入RequestBody
            entity = new HttpEntity<>(finalChatPromptMap, httpHeaders);
        }else{
            String mimeType= chatPrompt.getMediaMimeType();//Base64Util.getFileMimeType(chatPrompt.getMediaData().toString());
            //判断是否是字符串形式的数据类型，因为前端发送的是base64字符串编码后的文件
            if(chatPrompt.getMediaData() instanceof String || FileUtils.isStringType(chatPrompt.getMediaData())) {
                requestJson="{" + safetySettings +
                        "\"contents\":[\n" +
                        chatPrompt.getPreChatData() + "\n"+
                        "{\"role\":\"user\",\n" +
                        "\"parts\":[\n" +
                        "{\"text\": \""+chatPrompt.getChatMsg()+"\"},\n" +
                        "{\"inline_data\":\n" +
                        "{\"mime_type\": \""+mimeType+"\",\n" +
                        "\"data\": \""+Base64Util.getBase64Str(chatPrompt.getMediaData().toString())+"\"\n" +
                        "}}\n]}\n" +
                        "]}";
                //因为发送消息携带base64文件，为发送的消息构造一个消息内容Map，里面再次添加inline_data等所需要的内容，这里重新设置一个新的消息Map，因为这个是携带媒体文件数据的消息
                Map<String,Object> msgMediaDataMap = createMsgPromptMap("user", chatPrompt.getChatMsg(), mimeType, Base64Util.getBase64Str(chatPrompt.getMediaData().toString()));
                //还是一样添加到所有聊天记录list
                historyChatDataList.add(msgMediaDataMap);
                //同样添加到最外的构造消息请求体的Map
                finalChatPromptMap.put("contents", historyChatDataList);
                entity = new HttpEntity<>(finalChatPromptMap, httpHeaders);
            }else{
                httpHeaders.set("Content-Type", "multipart/form-data"); // 传递请求体时必须设置
//                byte[] fileToByte = (byte[]) chatPrompt.getMediaData();
//                ByteArrayResource resource = new ByteArrayResource(fileToByte);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(chatPrompt.getMediaData());
                objectOutputStream.flush();
                byte[] byteStream = byteArrayOutputStream.toByteArray();
                requestJson="{" + safetySettings +
                        "\"contents\":[\n" +
                        chatPrompt.getPreChatData() + "\n"+
                        "{\"role\":\"user\",\n" +
                        "\"parts\":[\n" +
                        "{\"text\": \""+chatPrompt.getChatMsg()+"\"},\n" +
                        "{\"inline_data\":\n" +
                        "{\"mime_type\": \""+mimeType+"\",\n" +
                        "\"data\": \""+byteStream+"\"\n" +
                        "}}\n]}\n" +
                        "]}";
                //这里与上面一样，就是类型变成MultiValueMap，这是上传大型文件
                MultiValueMap<String, Object> formChatPromptMap = new LinkedMultiValueMap<>();
                formChatPromptMap.setAll(createSafetySettingsMap());
                Map<String,Object> msgByteDataMap = createMsgPromptMap("user", chatPrompt.getChatMsg(), mimeType, byteStream);
                historyChatDataList.add(msgByteDataMap);
                formChatPromptMap.addAll("contents", historyChatDataList);
                //formChatPromptMap.setAll(JSONObject.parseObject(requestJson));
                entity = new HttpEntity<MultiValueMap<String,Object>>(formChatPromptMap, httpHeaders);
            }
        }
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, entity, String.class);
        log.info("响应数据 {}",response.getBody());
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
        }else{
            log.info("错误消息：{}", jsonObject.toString());
            //{"error": { "code": 200, "message": "", "status": "" }}
            JSONObject errorData = jsonObject.getJSONObject("error");
            geminiReply = errorData.get("message").toString();
        }
        return geminiReply;
    }

    //创建安全参数设置的Map
    private static Map<String,Object> createSafetySettingsMap(){
        Map<String,Object> safetySettingsMap = new HashMap<>();
        List<Map<String,Object>> safetyList=new ArrayList<>();
        Map<String,Object> safetyMap10=new HashMap<>();
        safetyMap10.put("category",10);
        safetyMap10.put("threshold",4);
        safetyList.add(safetyMap10);
        Map<String,Object> safetyMap9=new HashMap<>();
        safetyMap9.put("category",9);
        safetyMap9.put("threshold",4);
        safetyList.add(safetyMap9);
        Map<String,Object> safetyMap8=new HashMap<>();
        safetyMap8.put("category",8);
        safetyMap8.put("threshold",4);
        safetyList.add(safetyMap8);
        Map<String,Object> safetyMap7=new HashMap<>();
        safetyMap7.put("category",7);
        safetyMap7.put("threshold",4);
        safetyList.add(safetyMap7);
        safetySettingsMap.put("safetySettings",safetyList);
//        System.out.println(JSON.toJSONString(promptJson, SerializerFeature.DisableCircularReferenceDetect));
//        System.out.println(new JSONObject(safetySettingsJson).toString());;
        return safetySettingsMap;
    }

    // 创建消息主题内容的Map
    // {\"role\": \"user/model\", \"parts\":[{\"text\": \"消息内容\", \"inline_data\": { \"mime_type\":\"image/png\",\"data\": \"String/byte\" }}]}
    private static Map<String,Object> createMsgPromptMap(String role, String textContent, String mimeType, Object fileData){
        Map<String,Object> msgPromptMap = new HashMap<>();
        //没有role就不添加Map
        if(role !=null && !role.isEmpty()) {
            msgPromptMap.put("role", role);
        }
        //消息数组parts，和role是同样在一个层级
        //属于parts消息数组部分，包括text消息内容文本，以及可选携带媒体文件数据inline_data
        List<Map<String,Object>> partsList = new ArrayList<>();
        // 添加 text 元素
        Map<String,Object> textContentMap = new HashMap<>();
        textContentMap.put("text", textContent);
        //parts数组，里面包括重要的消息text，另外可以选择是否携带文件数据inline_data，里面包括数据类型和数据内容
        partsList.add(textContentMap);
        //表示如果没有文件就添加文件的相关Map
        if(mimeType !=null && !mimeType.isEmpty()) {
            Map<String, Object> inlineDataMap = new HashMap<>();
            Map<String, Object> dataContentMap = new HashMap<>();
            inlineDataMap.put("mime_type", mimeType);
            inlineDataMap.put("data", fileData);
            dataContentMap.put("inline_data", inlineDataMap);
            partsList.add(dataContentMap);
        }
        msgPromptMap.put("parts", partsList);
        return msgPromptMap;
    }

    // 创建文件上传后返回Uri消息主题内容的Map
    // {\"parts\":[{\"text\": \"消息内容\", \"file_data\": { \"mime_type\":\"video/mp4\",\"file_uri\": \"fileUri\" }}]}
    private static Map<String,Object> createMsgFileDataMap(String textContent, String mimeType, String fileUri){
        Map<String,Object> msgFileDatatMap = new HashMap<>();
        //消息数组parts，和role是同样在一个层级
        List<Map<String,Object>> partsList = new ArrayList<>();
        //属于parts消息数组部分，包括text消息内容文本，以及可选携带媒体文件数据inline_data
        // 添加 text 元素
        Map<String,Object> textContentMap = new HashMap<>();
        textContentMap.put("text", textContent);
        partsList.add(textContentMap);
        //表示如果没有文件就添加文件的相关Map
        if(mimeType !=null && !mimeType.isEmpty()) {
            Map<String, Object> inlineDataMap = new HashMap<>();
            Map<String, Object> dataContentMap = new HashMap<>();
            inlineDataMap.put("mime_type", mimeType);
            inlineDataMap.put("file_uri", fileUri);
            dataContentMap.put("file_data", inlineDataMap);
            partsList.add(dataContentMap);
        }
        //parts数组，里面包括重要的消息text，另外可以选择是否携带文件数据inline_data，里面包括数据类型和数据内容
        msgFileDatatMap.put("parts", partsList);
        return msgFileDatatMap;
    }
}
