package com.geekplus.webapp.common.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.geekplus.webapp.function.entity.ChatAILog;
import com.geekplus.common.util.DateTimeUtils;
import com.geekplus.common.util.ServletUtils;
import com.geekplus.common.util.baiduai.BaiduASRUtil;
import com.geekplus.common.util.baiduai.BaiduTTSUtil;
import com.geekplus.common.util.ip.IPAndMACUtils;
import com.geekplus.common.util.ip.IpUtils;
import com.geekplus.common.util.openai.ChatGPTUtils;
import com.geekplus.common.util.openai.GetClientName;
import com.geekplus.webapp.function.service.IChatAILogService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/23/23 21:23
 * description: 做什么的？
 */
@Service
@Slf4j
public class ChatGPTService {
//    private static final Logger log = LoggerFactory.getLogger(GetClientName.class);

    //@Value("${openai.api.key}")
    //private String openAiKey;

    @Resource
    private IChatAILogService chatgptLogService;

    private final StringRedisTemplate stringRedisTemplate;

    private HttpServletRequest httpServletRequest;
    private String ip;
    private UserAgent userAgent;
    int userAId=0;
    String osName=null;
    String browserName=null;

    /**
     * 接口请求地址
     */
    private final String chatUrl = "https://api.openai.com/v1/completions";
    private final String genImageUrl = "https://api.openai.com/v1/images/generations";

    @Autowired
    public ChatGPTService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 调用openAI的接口获取回复
     * @param messageContent 用户发送的消息
     * @param fromUser 用户id
     * @return String
     */
    public Map reply(String messageContent, String fromUser,String openAiKey) throws SocketException, UnknownHostException {
        // 默认信息
        ChatAILog chatAILog =new ChatAILog();
        Map mapMsg = new HashMap();
        String message="";
        //把msgcontent和fromuser转换成md5作为rediskey
        //获取用户的IP和MAC地址
        httpServletRequest=ServletUtils.getRequest();
        ip=IpUtils.getIpAddr(httpServletRequest);
        userAgent=GetClientName.getBrowser(httpServletRequest);
        userAId=userAgent.getId();
        osName=userAgent.getOperatingSystem().getName();
        browserName=userAgent.getBrowser().getName();
        String mac= "2023";
        String contentPre = fromUser+"_"+ip+"_"+osName+"_"+browserName+"_"+userAId;
        String md5Content = DigestUtils.md5DigestAsHex(contentPre.getBytes());
        //System.out.println("MAC地址："+mac);
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        log.info("MAC地址："+IPAndMACUtils.getAddressMac());
        //System.out.println("消息内容："+messageContent);
        log.info("消息内容："+messageContent);
        //System.out.println("加密的key："+md5Content);
        log.info("用户的临时tokenKey"+contentPre);
        log.info("加密的key："+md5Content);
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        // 如果存在key，拿出来
//        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(md5Content))) {
//
//            mapMsg.put("msg_data",stringRedisTemplate.opsForValue().get(md5Content));
//            //消息类型，1代表图片，0代表普通文本
//            mapMsg.put("msg_type","0");
//            return mapMsg;
//        }

        String body = "";
        String url = "";

        if (messageContent.length() > 3) {
            String keyword = messageContent.substring(0, 4);
            String subContent = "";
            int length = 0;
            if ("draw".equalsIgnoreCase(keyword)){
                //生成图片
                url = this.genImageUrl;
                length = messageContent.length();
                subContent = messageContent.substring(4, length);
                body = buildImageConfig(subContent);
                // 调用接口获取数据
                String obj = getChatReply(url, body,openAiKey);

                //ImageResponseBody imageResponseBody = JSONObject.parseObject(obj, ImageResponseBody.class);
                JSONObject jsonObject = JSONObject.parseObject(obj);
                if (jsonObject != null) {
                    if (!CollectionUtils.isEmpty(jsonObject.getJSONArray("data"))) {
                        String imageurl = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
                        message = "图片已生成，<a href=\"" + imageurl + "\" target='_blank'>点击这里</a>查看";
                        //List<HashMap<String,String>> msgMapList=new ArrayList<>();
                        //HashMap<String,String> msgMap=new HashMap<>();
                        HashMap<String,String> msgMap1=new HashMap<>();
                        msgMap1.put("align","right");
                        msgMap1.put("text",messageContent);
                        msgMap1.put("link","");
                        msgMap1.put("type","0");
                        //msgMapList.add(msgMap1);
                        stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1));
                        //从左侧也就是最上面移除过期时间到了的数据
                        //stringRedisTemplate.opsForList().leftPop(md5Content,8,TimeUnit.HOURS);
                        HashMap<String,String> msgMap2=new HashMap<>();
                        msgMap2.put("align","left");
                        msgMap2.put("text",message);
                        msgMap2.put("link","");
                        msgMap2.put("type","1");
                        //msgMapList.add(msgMap2);
                        //保存到redis里面 rightPush是从list列表尾部插入，先进后出
                        stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1), JSONObject.toJSONString(msgMap2));
                        //封装消息
                        //Map<String,Object> map=new HashMap();
                        chatAILog.setAskContent(messageContent);
                        chatAILog.setReplyContent(message.trim());//replaceAll("\\s*","")//替换内容中所有的空格空白字符制表符，换页符
                        mapMsg.put("msg_data",message);
                        //消息类型，1代表图片，0代表普通文本
                        mapMsg.put("msg_type","1");
                        chatAILog.setUserName(fromUser);
                        chatAILog.setUserIp(ip);
                        chatAILog.setUserMac(mac);
                        Date date= DateTimeUtils.getCurrentDate(LocalDate.now());
                        chatAILog.setCreateTime(date);
                        chatgptLogService.insertChatAILog(chatAILog);
                        return mapMsg;
                    }
                }
            }else if(messageContent.substring(0,1).equals("画")){
                //生成图片
                url = this.genImageUrl;
                length = messageContent.length();
                subContent = messageContent.substring(1, length);
                body = buildImageConfig(subContent);
                // 调用接口获取数据
                String obj = getChatReply(url, body,openAiKey);

                //ImageResponseBody imageResponseBody = JSONObject.parseObject(obj, ImageResponseBody.class);
                JSONObject jsonObject = JSONObject.parseObject(obj);
                if (jsonObject != null) {
                    if (!CollectionUtils.isEmpty(jsonObject.getJSONArray("data"))) {
                        String imageurl = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
                        message = "图片已生成，<a href=\"" + imageurl + "\" target='_blank'>点击这里</a>查看";
                        //List<HashMap<String,String>> msgMapList=new ArrayList<>();
                        HashMap<String,String> msgMap1=new HashMap<>();
                        msgMap1.put("align","right");
                        msgMap1.put("text",messageContent);
                        msgMap1.put("link","");
                        msgMap1.put("type","0");
                        //msgMapList.add(msgMap1);
                        stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1));
                        HashMap<String,String> msgMap2=new HashMap<>();
                        msgMap2.put("align","left");
                        msgMap2.put("text",message);
                        msgMap2.put("link","");
                        msgMap2.put("type","1");
                        //msgMapList.add(msgMap2);
                        //保存到redis里面 rightPush是从list列表尾部插入，先进后出
                        stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1), JSONObject.toJSONString(msgMap2));
                        //封装消息
                        //Map<String,Object> map=new HashMap();
                        chatAILog.setAskContent(messageContent);
                        chatAILog.setReplyContent(message.trim());//replaceAll("\\s*","")
                        mapMsg.put("msg_data",message);
                        //消息类型，1代表图片，0代表普通文本
                        mapMsg.put("msg_type","1");
                        chatAILog.setUserName(fromUser);
                        chatAILog.setUserIp(ip);
                        chatAILog.setUserMac(mac);
                        Date date= DateTimeUtils.getCurrentDate(LocalDate.now());
                        chatAILog.setCreateTime(date);
                        chatgptLogService.insertChatAILog(chatAILog);
                        return mapMsg;
                    }
                }
            }
        }

        url = this.chatUrl;
        body = buildConfig(messageContent);
        // 调用接口获取数据
        String obj = getChatReply(url, body,openAiKey);
        //MessageResponseBody messageResponseBody = JSONObject.parseObject(obj, MessageResponseBody.class);
        JSONObject jsonObject = JSONObject.parseObject(obj);

        // 存储对话内容
        if (jsonObject != null) {
            if (!CollectionUtils.isEmpty(jsonObject.getJSONArray("choices"))) {
                JSONArray choices = jsonObject.getJSONArray("choices");
                String text = choices.getJSONObject(0).getString("text");
                //String messagepre = messageResponseBody.getChoices().get(0).getText();
                message = text.substring(2);
                chatAILog.setAskContent(messageContent);
                chatAILog.setReplyContent(text.trim());//replaceAll("\\s*","")//替换内容中所有的空格空白字符制表符，换页符
                mapMsg.put("msg_data",text.trim().replaceFirst("\\s*",""));
                //消息类型，1代表图片，0代表普通文本
                mapMsg.put("msg_type","0");
                HashMap<String,String> msgMap1=new HashMap<>();
                msgMap1.put("align","right");
                msgMap1.put("text",messageContent);
                msgMap1.put("link","");
                msgMap1.put("type","0");
                //msgMapList.add(msgMap1);
                stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1));
                HashMap<String,String> msgMap2=new HashMap<>();
                msgMap2.put("align","left");
                msgMap2.put("text",text.trim().replaceFirst("\\s*",""));
                msgMap2.put("link","");
                msgMap2.put("type","0");
                //msgMapList.add(msgMap2);
                //保存到redis里面 rightPush是从list列表尾部插入，先进后出
                stringRedisTemplate.opsForList().rightPush(md5Content, JSONObject.toJSONString(msgMap1), JSONObject.toJSONString(msgMap2));
            }
        }
        stringRedisTemplate.expire(md5Content, 8, TimeUnit.HOURS);
        chatAILog.setUserName(fromUser);
        chatAILog.setUserIp(ip);
        chatAILog.setUserMac(mac);
        Date date= DateTimeUtils.getCurrentDate(LocalDate.now());
        chatAILog.setCreateTime(date);
        chatgptLogService.insertChatAILog(chatAILog);
        return mapMsg;

    }

    private String getChatReply(String url, String body,String openAiKey) {

        Map<String, String> header = new HashMap();
        header.put("Authorization", "Bearer " + openAiKey);
        header.put("Content-Type", "application/json");

        log.info("发送的数据：" + body);
        // 发送请求
        String data = ChatGPTUtils.sendChatGPTPost(url, body, header);

        return data;

    }

    /**
      * @Author geekplus
      * @Description //没有封装的,Controller直接调用没有封装，只有文字聊天功能
      * @Param [url, prompt, chatUser]
      * @Throws
      * @Return {@link String}
      */
    public String getChatContent(String url,String prompt,String chatUser,String openAiKey) {
        String ip= IpUtils.getIpAddr(ServletUtils.getRequest());
        String mac=IPAndMACUtils.getAddressMac();
        Map<String, String> httpHeaders = new HashMap();
        httpHeaders.put("Authorization", "Bearer "+openAiKey);
        httpHeaders.put("Content-Type", "application/json"); // 传递请求体时必须设置
        String requestJson="{\"model\":\"text-davinci-003\",\"prompt\": \""+prompt+"\"," +
                "\"temperature\":0.6,\"max_tokens\":3069}";
        String textBody = ChatGPTUtils.sendChatGPTPost(url, requestJson,httpHeaders);
        JSONObject jsonObject = JSONObject.parseObject(textBody);
        JSONArray choices = jsonObject.getJSONArray("choices");
        String text = choices.getJSONObject(0).getString("text");
        ChatAILog chatAILog =new ChatAILog();
        chatAILog.setUserName(chatUser);
        chatAILog.setUserIp(ip);
        chatAILog.setUserMac(mac);
        Date date= DateTimeUtils.getCurrentDate(LocalDate.now());
        chatAILog.setCreateTime(date);
        chatAILog.setAskContent(prompt);
        chatAILog.setReplyContent(text);
        chatgptLogService.insertChatAILog(chatAILog);
        System.out.println(text);
        return text;
    }

    /**
     * 构建请求体
     *
     * @return
     */
    private String buildConfig(String prompt) {
        //MessageSendBody messageSendBody = new MessageSendBody();
        Map<String,Object> messageSendBody=new HashMap<>();
        messageSendBody.put("model","text-davinci-003");
        messageSendBody.put("temperature",0.9);
        messageSendBody.put("max_tokens",2500);
        messageSendBody.put("top_p",1);
        messageSendBody.put("frequency_penalty",0.0);
        messageSendBody.put("presence_penalty",0.6);
        messageSendBody.put("prompt",prompt);
        List<String> stop = new ArrayList<>();
        stop.add(" ayu:");
        stop.add(" Human:");
        stop.add(" AI:");
        messageSendBody.put("stop",stop);
        String res = JSON.toJSONString(messageSendBody, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        return res;
    }

    private String buildImageConfig(String prompt) {
        //ImageSendBody imageSendBody = new ImageSendBody();
        Map<String,String> imageSendBody=new HashMap<>();
        //imageSendBody.setPrompt(prompt);
        imageSendBody.put("prompt",prompt);
        String res = JSON.toJSONString(imageSendBody, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);

        return res;
    }

    public String getTextToVoice(String text) throws IOException {
        String token="24.270948c59fcd0c70913aea41ecf533bd.2592000.1680199081.282335-30837213";
        BaiduTTSUtil baiduTTSUtil=new BaiduTTSUtil();
        String result=baiduTTSUtil.textToVoice(text,token);
        return result;
    }

    public JSONObject getVoiceToText(byte[] fileContent) throws IOException {
        String token="24.270948c59fcd0c70913aea41ecf533bd.2592000.1680199081.282335-30837213";
        BaiduASRUtil baiduASRUtil=new BaiduASRUtil();
        //baiduASRUtil.runJsonPostMethod(fileName,token);
        JSONObject jsonObject= JSONObject.parseObject(baiduASRUtil.runJsonPostMethod(fileContent,token));
        //JSONArray jsonArray=jsonObject.getJSONArray("result");
        return jsonObject;
    }

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
        strPre.append(userName);
        if("guest".equals(userName) || userName=="guest") {
            //strPre.append(userName + "_" + ip + "_" + osName + "_" + browserName + "_" + userAId);
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
