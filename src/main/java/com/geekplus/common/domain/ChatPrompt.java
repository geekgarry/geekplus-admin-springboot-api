package com.geekplus.common.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/18/23 21:28
 * description: 做什么的？
 */
public class ChatPrompt implements Serializable {
    //聊天用户
    private String userId;
    //聊天用户
    private String username;
    //聊天消息内容
    private String chatMsg;
    //此前聊天内容
    private String preChatData;//字符串形式，用了historyChatDataList代替了
    //历史聊天记录
    private List<Map<String,Object>> historyChatData;
    //聊天媒体，图片/视频/音频
    private Object mediaData;
    //媒体数据类型
    private String mediaMimeType;
    //媒体文件名称
    private String mediaFileName;
    //聊天代码
    private String code;//没有被使用

    private String openAiKey;//没有被使用

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getPreChatData() {
        return preChatData;
    }

    public void setPreChatData(String preChatData) {
        this.preChatData = preChatData;
    }

    public List<Map<String, Object>> getHistoryChatData() {
        return historyChatData;
    }

    public void setHistoryChatData(List<Map<String, Object>> historyChatData) {
        this.historyChatData = historyChatData;
    }

    public Object getMediaData() {
        return mediaData;
    }

    public void setMediaData(Object mediaData) {
        this.mediaData = mediaData;
    }

    public String getMediaMimeType() {
        return mediaMimeType;
    }

    public void setMediaMimeType(String mediaMimeType) {
        this.mediaMimeType = mediaMimeType;
    }

    public String getMediaFileName() {
        return mediaFileName;
    }

    public void setMediaFileName(String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenAiKey() {
        return openAiKey;
    }

    public void setOpenAiKey(String openAiKey) {
        this.openAiKey = openAiKey;
    }
}
