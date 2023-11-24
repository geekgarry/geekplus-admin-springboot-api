package com.geekplus.framework.socketio;

import java.io.Serializable;

/**
 * @author: geekgarry
 * @date: 8/7 14:58
 * @description:
 */
public class ChatObject implements Serializable {

    private static final long serialVersionUID = 1842341935944229820L;
    private String userName;
    private String message;

    public ChatObject() {
    }

    public ChatObject(String userName, String message) {
        super();
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
