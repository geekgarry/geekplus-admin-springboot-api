package com.geekplus.framework.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * @author: geekgarry
 * @date: 8/7 15:11
 * @description:
 */
public class ChatEventListener implements DataListener<ChatObject> {

    private SocketIOServer server;

    public void setServer(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) throws Exception {
        System.out.println("hhhhhhhh");
        //chatevent为 事件的名称，data为发送的内容
        this.server.getBroadcastOperations().sendEvent("chatevent", data);
    }
}
