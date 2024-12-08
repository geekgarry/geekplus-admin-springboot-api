package com.geekplus.webapp.common.service;

import com.alibaba.fastjson.JSONObject;
import com.geekplus.common.domain.ChatPrompt;
import com.geekplus.common.util.datetime.DateTimeUtils;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.common.util.base64.Base64Util;
import com.geekplus.common.util.file.FileUploadUtils;
import com.geekplus.common.util.file.FileUtils;
import com.geekplus.common.util.google.GeminiUtils;
import com.geekplus.common.util.http.IPUtils;
import com.geekplus.common.util.openai.GetClientName;
import com.geekplus.webapp.function.entity.ChatAILog;
import com.geekplus.webapp.function.service.IChatAILogService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 6/28/24 7:46 AM
 * description: 做什么的？
 */
@Slf4j
@Service
public class GeminiChatService {
    @Value("${google.gemini.api-key:}")
    private String geminiApiKey;

    @Resource
    private IChatAILogService chatgptLogService;

    private final StringRedisTemplate stringRedisTemplate;

    private HttpServletRequest httpServletRequest;
    private String ip;
    private UserAgent userAgent;
    int userAId=0;
    String osName=null;
    String browserName=null;

    //初始化redis操作类
    @Autowired
    public GeminiChatService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * @Author geekplus
     * @Description // Google Gemini AI 无记忆聊天和携带历史聊天记录
     * @Param String messageContent, String preMessageJson, String fromUser
     * @Throws
     * @Return {@link }
     */
    public Map<String, Object> getGeminiContent(ChatPrompt chatPrompt) throws IOException {
        // 默认信息
        ChatAILog chatAILog =new ChatAILog();
        Map<String,Object> mapResponse = new HashMap();
        String aiReplyText=null;
        //把msgcontent和fromuser转换成md5作为rediskey
        long chatDate= new Date().getTime();
        //获取用户的IP和MAC地址
        httpServletRequest= ServletUtil.getRequest();
        ip= IPUtils.getIpAddr(httpServletRequest);
        userAgent=GetClientName.getBrowser(httpServletRequest);
        userAId=userAgent.getId();
        osName=userAgent.getOperatingSystem().getName();
        browserName=userAgent.getBrowser().getName();
        StringBuilder strPreKey = new StringBuilder("GeekPlus");
        String md5Content = null;
        if("guest".equals(chatPrompt.getUsername()) || chatPrompt.getUsername().contains("guest")){
            strPreKey.append("_").append(ip)
                    .append("_").append(osName)
                    .append("_").append(browserName)
                    .append("_").append(userAId);//没有重新赋值
            md5Content = chatPrompt.getUsername() + ":" + DigestUtils.md5DigestAsHex(strPreKey.toString().getBytes());
        }else {
            md5Content = chatPrompt.getUsername() + ":" + DigestUtils.md5DigestAsHex(strPreKey.append("_"+chatPrompt.getUsername()).toString().getBytes());
        }
        if(chatPrompt.getHistoryId() != null && !chatPrompt.getHistoryId().isEmpty()) {
            md5Content = chatPrompt.getUsername() + ":" + chatPrompt.getHistoryId();
        }
        //System.out.println("MAC地址："+mac);
//        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//        log.info("MAC地址："+IPAndMACUtils.getAddressMac());
//        //System.out.println("消息内容："+messageContent);
//        log.info("消息内容："+messageContent);
//        System.out.println("加密的key："+md5Content);
        log.info("用户的临时tokenKey"+strPreKey);
        log.info("加密的key："+md5Content);
//        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        String fileUrl = "";
        if(!ObjectUtils.isEmpty(chatPrompt.getMediaData())) {
            //&&当第一个表达式的值为false的时候，则不再计算第二个表达式,&则怎么样都执行
            //||当第一个表达式的值为true，就不再计算后面的表达式，｜则也会都执行
            if(FileUtils.isStringType(chatPrompt.getMediaData()) && Base64Util.isBase64(chatPrompt.getMediaData().toString())) {
                fileUrl = FileUploadUtils.base64StrToFile(chatPrompt.getMediaData().toString());
            }
            //chatPrompt.setMediaData(Base64Util.getBase64Str(chatPrompt.getMediaData().toString()));
        }
        //Gemini AI 返回内容
        if(null == chatPrompt.getHistoryChatData()) {//|| chatPrompt.getHistoryChatData().isEmpty()
            aiReplyText = GeminiUtils.postGemini(chatPrompt, geminiApiKey);
        }else {
            aiReplyText = GeminiUtils.postGeminiHistory(chatPrompt, geminiApiKey);
        }

        //存储对话记录
        Date chatReplyDate = DateTimeUtils.getCurrentDateTime();//DateTimeUtils.getCurrentDate(LocalDate.now());
        //if (aiReplyText != null){
        long chatReplySeconds = new Date().getTime();
        chatAILog.setAskContent(fileUrl+"\n"+chatPrompt.getChatMsg());
        chatAILog.setReplyContent(aiReplyText.trim());//replaceAll("\\s*","").replaceAll(" +"," ")
        mapResponse.put("msg_data",aiReplyText.trim());
        //消息类型，text(文本/富文本),image(图片),video(视频),audio(音频),file(文件)
        mapResponse.put("msg_type","text");
        mapResponse.put("msg_date_time",chatReplySeconds);
        HashMap<String,Object> msgMap1=new HashMap<>();
        //用户发送的消息
        msgMap1.put("align","right");
        msgMap1.put("text",chatPrompt.getChatMsg());
        msgMap1.put("link","");
        //消息类型，text(文本/富文本),image(图片),video(视频),audio(音频),file(文件)
        msgMap1.put("type","text");
        msgMap1.put("time",chatDate);
        if(fileUrl!="" || !"".equals(fileUrl)) {
            if(chatPrompt.getMediaMimeType().startsWith("image/")) {
                msgMap1.put("type", "image");
            }else if(chatPrompt.getMediaMimeType().startsWith("video/")) {
                msgMap1.put("type", "video");
            }else if(chatPrompt.getMediaMimeType().startsWith("audio/")) {
                msgMap1.put("type", "audio");
            }else {
                msgMap1.put("type", "file");
            }
            msgMap1.put("mediaData", fileUrl);
            msgMap1.put("mediaMimeType",chatPrompt.getMediaMimeType());
            msgMap1.put("mediaFileName", chatPrompt.getMediaFileName());
        }
        //msgMapList.add(msgMap1);
        stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1));
        HashMap<String,Object> msgMap2=new HashMap<>();
        //用户接收的消息
        msgMap2.put("align","left");
        msgMap2.put("text",aiReplyText.trim());
        msgMap2.put("link","");
        msgMap2.put("type","text");
        msgMap2.put("time",chatReplySeconds);
        //msgMapList.add(msgMap2);
        //保存到redis里面 rightPush是从list列表尾部插入，先进后出
        //stringRedisTemplate.opsForHash().putAll(md5Content, msgMap2);
        stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1), JSONObject.toJSONString(msgMap2));
        //}
        stringRedisTemplate.expire(md5Content, 30, TimeUnit.HOURS);
        chatAILog.setUserName(chatPrompt.getUsername());
        chatAILog.setUserIp(ip);
        chatAILog.setUserMac("2060-XX-XX");
        //Date logDate= DateTimeUtils.getCurrentDate(LocalDate.now());
        chatAILog.setCreateTime(chatReplyDate);
        chatgptLogService.insertChatAILog(chatAILog);
        return mapResponse;
    }

    /**
     * @Author geekplus
     * @Description // Google Gemini AI 聊天/测试
     * @Param
     * @Throws
     * @Return {@link }
     */
    public Object streamGemini(ChatPrompt chatPrompt) {
        try {
            return GeminiUtils.postStreamGemini(chatPrompt, geminiApiKey);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //获取此前历史聊天记录
    public List<String> getOneHistoryMsgList(String userName){
        List<String> msgList=new ArrayList<>();
        //String msg=null;
        StringBuilder strPreKey=new StringBuilder("GeekPlus");
        //String contentPre = userName;
        //获取用户的IP和MAC地址
        httpServletRequest=ServletUtil.getRequest();
        ip= IPUtils.getIpAddr(httpServletRequest);
        userAgent=GetClientName.getBrowser(httpServletRequest);
        userAId=userAgent.getId();
        osName=userAgent.getOperatingSystem().getName();
        browserName=userAgent.getBrowser().getName();
        String md5Content = null;
        if("guest".equals(userName) || userName.contains("guest")) {
            strPreKey.append("_").append(ip)
                    .append("_").append(osName)
                    .append("_").append(browserName)
                    .append("_").append(userAId);
            md5Content = userName+":"+ DigestUtils.md5DigestAsHex(strPreKey.toString().getBytes());
        }else {
            md5Content = userName+":"+ DigestUtils.md5DigestAsHex(strPreKey.append("_"+userName).toString().getBytes());
        }
        log.info("用户的临时tokenKey："+strPreKey.toString());
        // 如果存在key，拿出来
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(md5Content))) {
            msgList = stringRedisTemplate.opsForList().range(md5Content, 0, -1);//获取所有值
        }
        log.info("我的历史消息："+msgList.toString());
        return msgList;
    }

    //根据redisKey获取此前历史聊天记录
    public List<String> getOneHistoryMsgListByKey(String redisKey){
        List<String> msgList=new ArrayList<>();
        // 如果存在key，拿出来
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(redisKey))) {
            msgList = stringRedisTemplate.opsForList().range(redisKey, 0, -1);//获取所有值
        }
        return msgList;
    }

    //获取所有聊天记录
    public List<Map<String, Object>> getAllHistoryMsgList(String userName){
        //定义一个所有历史消息纪录的list，根据userName去查询所有的相关的，
        List<Map<String, Object>> allMsgList=new ArrayList<>();
        //String msg=null;
        StringBuilder strPreKey=new StringBuilder("GeekPlus");
        //String contentPre = userName;
        //获取用户的IP和MAC地址
        httpServletRequest=ServletUtil.getRequest();
        ip= IPUtils.getIpAddr(httpServletRequest);
        userAgent=GetClientName.getBrowser(httpServletRequest);
        userAId=userAgent.getId();
        osName=userAgent.getOperatingSystem().getName();
        browserName=userAgent.getBrowser().getName();
        if("guest".equals(userName) || userName.contains("guest")) {
            strPreKey.append("_").append(ip)
                    .append("_").append(osName)
                    .append("_").append(browserName)
                    .append("_").append(userAId);
            String md5Content = userName + ":" + DigestUtils.md5DigestAsHex(strPreKey.toString().getBytes());
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(md5Content))){
                Map<String, Object> latestHistoryMsg = new HashMap<>();
                //每一条历史消息记录，用Map包裹，里面有历史消息的redis的key，还有历史聊天记录的最新一条消息
                latestHistoryMsg.put("historyMsgKey", md5Content);
                latestHistoryMsg.put("historyMsg", stringRedisTemplate.opsForList().range(md5Content,-1, -1).get(0));
                allMsgList.add(latestHistoryMsg);
            }
        }else {
//            stringRedisTemplate.
            Set<String> setKeys = stringRedisTemplate.keys(userName);
            for (String key : setKeys) {
                Map<String, Object> latestHistoryMsg = new HashMap<>();
                //每一条历史消息记录，用Map包裹，里面有历史消息的redis的key，还有历史聊天记录的最新一条消息
                latestHistoryMsg.put("historyMsgKey", key);
                latestHistoryMsg.put("historyMsg", stringRedisTemplate.opsForList().range(key, -1, -1).get(0));
                allMsgList.add(latestHistoryMsg);
            }
        }
        log.info("用户的临时tokenKey：" + strPreKey.toString());
        return allMsgList;
    }

    //删除历史聊天记录
    public Boolean deleteHistoryMsgList(String msgKey){
        // 如果存在key，拿出来
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(msgKey))) {
            return stringRedisTemplate.delete(msgKey);
        }else {
            return false;
        }
    }
}
