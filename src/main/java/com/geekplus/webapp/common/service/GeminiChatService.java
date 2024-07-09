package com.geekplus.webapp.common.service;

import com.alibaba.fastjson.JSONObject;
import com.geekplus.common.domain.ChatPrompt;
import com.geekplus.common.util.DateTimeUtils;
import com.geekplus.common.util.ServletUtils;
import com.geekplus.common.util.file.FileUploadUtils;
import com.geekplus.common.util.google.GeminiUtils;
import com.geekplus.common.util.ip.IpUtils;
import com.geekplus.common.util.openai.GetClientName;
import com.geekplus.webapp.function.entity.ChatAILog;
import com.geekplus.webapp.function.service.IChatAILogService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public Map getGeminiContent(ChatPrompt chatPrompt){
        // 默认信息
        ChatAILog chatAILog =new ChatAILog();
        Map mapMsg = new HashMap();
        String aiReplyText=null;
        //把msgcontent和fromuser转换成md5作为rediskey
        long chatDate= new Date().getTime();
        Date chatReplyDate= new Date();
        //获取用户的IP和MAC地址
        httpServletRequest= ServletUtils.getRequest();
        ip= IpUtils.getIpAddr(httpServletRequest);
        userAgent= GetClientName.getBrowser(httpServletRequest);
        userAId=userAgent.getId();
        osName=userAgent.getOperatingSystem().getName();
        browserName=userAgent.getBrowser().getName();
        String mac= "2024-XXX";

        String contentPre = chatPrompt.getUsername();
        if("guest".equals(chatPrompt.getUsername()) || chatPrompt.getUsername()=="guest"){
            contentPre = chatPrompt.getUsername()+"_"+ip+"_"+osName+"_"+browserName+"_"+userAId;//没有重新赋值
        }
        String md5Content = DigestUtils.md5DigestAsHex(contentPre.getBytes());
        //System.out.println("MAC地址："+mac);
//        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//        log.info("MAC地址："+IPAndMACUtils.getAddressMac());
//        //System.out.println("消息内容："+messageContent);
//        log.info("消息内容："+messageContent);
//        System.out.println("加密的key："+md5Content);
        log.info("用户的临时tokenKey"+contentPre);
        log.info("加密的key："+md5Content);
//        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        String fileUrl = "";
        if(chatPrompt.getMediaData() != null && !"".equals(chatPrompt.getMediaData())) {
            fileUrl = FileUploadUtils.base64StrToFile(chatPrompt.getMediaData().toString());
            //chatPrompt.setMediaData(Base64Util.getBase64Str(chatPrompt.getMediaData().toString()));
        }
        //Gemini AI 返回内容
        if(chatPrompt.getPreChatData()==null || "".equals(chatPrompt.getPreChatData())) {
            aiReplyText = GeminiUtils.postGemini(chatPrompt.getChatData(), chatPrompt.getMediaData(), geminiApiKey);
        }else {
            aiReplyText = GeminiUtils.postGeminiHistory(chatPrompt.getChatData(), chatPrompt.getPreChatData(), chatPrompt.getMediaData(), geminiApiKey);
        }

        //存储对话记录
        //if (aiReplyText != null){
        chatReplyDate = DateTimeUtils.getCurrentDateTime();//DateTimeUtils.getCurrentDate(LocalDate.now());
        long chatReplySeconds = new Date().getTime();
        chatAILog.setChatContent(fileUrl+"\n"+chatPrompt.getChatData()+"-Q&A-"+aiReplyText.replaceAll("\\s*",""));
        mapMsg.put("msg_data",aiReplyText.trim().replaceFirst("\\s*",""));
        //消息类型，1代表图片，0代表普通文本
        mapMsg.put("msg_type","0");
        mapMsg.put("msg_date_time",chatReplySeconds);
        HashMap<String,Object> msgMap1=new HashMap<>();
        msgMap1.put("align","right");
        msgMap1.put("text",chatPrompt.getChatData());
        if(fileUrl!="") {
            msgMap1.put("extraData", fileUrl);
        }
        msgMap1.put("link","");
        msgMap1.put("type","0");
        msgMap1.put("time",chatDate);
        //msgMapList.add(msgMap1);
        stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1));
        HashMap<String,Object> msgMap2=new HashMap<>();
        msgMap2.put("align","left");
        msgMap2.put("text",aiReplyText.trim().replaceFirst("\\s*",""));
        msgMap2.put("link","");
        msgMap2.put("type","0");
        msgMap2.put("time",chatReplySeconds);
        //msgMapList.add(msgMap2);
        //保存到redis里面 rightPush是从list列表尾部插入，先进后出
        stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1), JSONObject.toJSONString(msgMap2));
        //}
        stringRedisTemplate.expire(md5Content, 12, TimeUnit.HOURS);
        chatAILog.setUserName(chatPrompt.getUsername());
        chatAILog.setUserIp(ip);
        chatAILog.setUserMac(mac);
        //Date logDate= DateTimeUtils.getCurrentDate(LocalDate.now());
        chatAILog.setCreateTime(chatReplyDate);
        chatgptLogService.insertChatAILog(chatAILog);
        return mapMsg;
    }

    /**
     * @Author geekplus
     * @Description // Google Gemini AI 聊天/测试
     * @Param
     * @Throws
     * @Return {@link }
     */
    public Map testGemini(String messageContent){
        JSONObject jsonObject = JSONObject.parseObject(GeminiUtils.postGemini(messageContent,null,geminiApiKey));
        return jsonObject;
    }

    /**
     * @Author geekplus
     * @Description // Google Gemini AI 聊天/图片
     * @Param
     * @Throws
     * @Return {@link }
     */
    public String getGeminiImage(String messageContent,Object base64Image, String fromUser){
        return GeminiUtils.postImageGemini(messageContent,base64Image.toString(),geminiApiKey);
    }

//    public Map getGeminiImageJson(String messageContent,String base64Image, String fromUser){
//        JSONObject jsonObject = JSONObject.parseObject(GeminiUtils.postImageGemini(messageContent,base64Image,geminiApiKey));
//        return jsonObject;
//    }

    public List<String> getHistoryMsgList(String userName){
        List<String> msgList=new ArrayList<>();
        //String msg=null;
        //获取用户的IP和MAC地址
        httpServletRequest=ServletUtils.getRequest();
        ip=IpUtils.getIpAddr(httpServletRequest);
        userAgent=GetClientName.getBrowser(httpServletRequest);
        userAId=userAgent.getId();
        osName=userAgent.getOperatingSystem().getName();
        browserName=userAgent.getBrowser().getName();
        StringBuilder strPre=new StringBuilder();
        //String contentPre = userName;
        strPre.append(userName);
        if("guest".equals(userName) || userName=="guest") {
            strPre.append("_")
                    .append(ip)
                    .append("_")
                    .append(osName)
                    .append("_")
                    .append(browserName)
                    .append("_")
                    .append(userAId);
        }
        log.info("用户的临时tokenKey："+strPre.toString());
        String md5Content = DigestUtils.md5DigestAsHex(strPre.toString().getBytes());
        // 如果存在key，拿出来
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(md5Content))) {
            msgList = stringRedisTemplate.opsForList().range(md5Content, 0, -1);//获取所有值
        }
        log.info("我的历史消息："+msgList.toString());
        return msgList;
    }
}
