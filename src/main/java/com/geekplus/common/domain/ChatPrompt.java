package com.geekplus.common.domain;

import java.io.Serializable;

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
    //聊天内容
    private String chatData;
    //此前聊天内容
    private String preChatData;
    //聊天媒体，图片/视频/音频
    private Object mediaData;
    //聊天代码
    private String code;

    private String openAiKey;

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

    public String getChatData() {
        return chatData;
    }

    public void setChatData(String chatData) {
        this.chatData = chatData;
    }

    public String getPreChatData() {
        return preChatData;
    }

    public void setPreChatData(String preChatData) {
        this.preChatData = preChatData;
    }

    public Object getMediaData() {
        return mediaData;
    }

    public void setMediaData(Object mediaData) {
        this.mediaData = mediaData;
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
